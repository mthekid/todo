package com.starlab.withreact.todo.controller;

import com.starlab.withreact.todo.dto.ResponseDTO;
import com.starlab.withreact.todo.dto.TodoDTO;
import com.starlab.withreact.todo.model.TodoEntity;
import com.starlab.withreact.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user"; // 임시 사용자 식별번호

            // TodoEntity로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            // id를 null로 초기화 [ 생성 당시에는 ID가 있어서 안된다 ]
            entity.setId(null);

            // 임시 사용자 아이디 설정.
            entity.setUserId(temporaryUserId);

            // 서비스를 이용해 Todo 엔티티 생성
            List<TodoEntity> entities = service.create(entity);

            // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new)
                    .collect(Collectors.toList());

            // 변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .data(dtos)
                    .build();

            // ResponseDTO 리턴
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }


}
