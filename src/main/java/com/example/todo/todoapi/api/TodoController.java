package com.example.todo.todoapi.api;

import com.example.todo.auth.TokenUserInfo;
import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    private final TodoService todoService;

    //할 일 등록 요청
    @PostMapping
    public ResponseEntity<?> createTodo(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody TodoCreateRequestDTO requestDTO,
            BindingResult result
    ) {
        if(result.hasErrors()) {
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());
            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldError());
        }

        try {
            TodoListResponseDTO responseDTO = todoService.create(requestDTO,userInfo);
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (IllegalStateException e){
            log.warn(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        }
        catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));
        }
    }

    //할 일 삭제 요청
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable("id") String todoId
    ) {
        log.info("/api/todos/{} DELETE request!", todoId);

        if(todoId == null || todoId.trim().equals("")) {
            return ResponseEntity
                    .badRequest()
                    .body(TodoListResponseDTO.builder().error("ID를 전달해 주세요."));
        }

        try {
            TodoListResponseDTO responseDTO = todoService.delete(todoId,userInfo.getUserId());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));
        }

    }


    //할 일 목록 요청
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(
            //토큰에 인증된 사용자 정보를 불러올수 있음
            @AuthenticationPrincipal TokenUserInfo userInfo
            ) {
        log.info("/api/todos GET request");
        log.info("--------------------"+userInfo);
        TodoListResponseDTO responseDTO = todoService.retrieve(userInfo.getUserId());

        return ResponseEntity.ok().body(responseDTO);
    }


    //할 일 수정 요청
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateTodo(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody TodoModifyRequestDTO requestDTO,
            BindingResult result,
            HttpServletRequest request
    ) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldError());
        }

        log.info("/api/todos {} request!", request.getMethod());
        log.info("modifying dto: {}", requestDTO);

        try {
            TodoListResponseDTO responseDTO = todoService.update(requestDTO,userInfo.getUserId());
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body(TodoListResponseDTO.builder().error(e.getMessage()));
        }

    }

}
