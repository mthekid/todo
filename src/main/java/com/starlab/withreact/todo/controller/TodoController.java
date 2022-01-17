package com.starlab.withreact.todo.controller;

import com.starlab.withreact.todo.dto.ResponseDTO;
import com.starlab.withreact.todo.dto.TodoDTO;
import com.starlab.withreact.todo.model.TodoEntity;
import com.starlab.withreact.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService(); // 테스트 서비스 사용
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    // TodoEntity 생성
    @PostMapping
    public ResponseEntity<?> createTodo(
            @AuthenticationPrincipal String userId, // 권한과 관련된 사용자
            @RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user"; // 임시 사용자 식별번호

            // TodoEntity로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            // id를 null로 초기화 [ 생성 당시에는 ID가 있어서 안된다 ]
            entity.setId(null);

            // 임시 사용자 아이디 설정.
            entity.setUserId(userId);

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

    // TodoDTO 얻어오기
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(
            @AuthenticationPrincipal String userId) {
//        String temporaryUserId = "temporary-user"; // 임시 사용자 ID

        // 1 서비스 메서드의 retrieve로 ToDo엔티티 리스트 가져오기
        List<TodoEntity> entities = service.retrieve(userId);

        // 2 자바의 스트림 메커ㅣ즘으로 DTO로 변환
        List<TodoDTO> dtos = entities.stream()
                .map(TodoDTO::new)
                .collect(Collectors.toList());

        // 3 변환된 TodoDTO 리스트를 이용해서 ResponseDTO를 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(dtos)
                .build();

        // 4 반환
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto) {
//        String temporaryUserId = "temporary-user";

        log.info("Request Body dto -> {} ", dto);

        // 요청으로 넘어온 DTO를 영속성 엔티티 형식 데이터 전환
        TodoEntity entity = TodoDTO.toEntity(dto);

        log.info("dto -> entity {} ", entity);

        // id 초기화
        entity.setUserId(userId);

        // 서비스 파트를 거쳐 업데이트
        List<TodoEntity> entities = service.update(entity);

        log.info("entities ->  {} ", entities);

        // 스트림을 활용해 전환
        List<TodoDTO> dtos = entities.stream()
                .map(TodoDTO::new)
                .collect(Collectors.toList());

        log.info("dtos ->  {} ", dtos);

        // 변환된 DTO로 ResponseDTO 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(dtos)
                .build();

        log.info("response -> {} ", response);

        // DTO 리턴
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto) {
        try {
//            String temporaryUserId = "temporary-user";

            // DTO를 엔티티로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            // 임시 사용자 아이디 지정
            entity.setUserId(userId);

            // 삭제 [ JPA에서 기본 delete 메서드를 구현해준다. ]
            List<TodoEntity> entities = service.delete(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new)
                    .collect(Collectors.toList());

            // 변환된 DTO 리스트를 이용해 ResponseDTO 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .data(dtos)
                    .build();

            // ResponseDTO를 리턴
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
