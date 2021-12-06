package com.starlab.withreact.todo.controller;

import com.starlab.withreact.todo.dto.ResponseDTO;
import com.starlab.withreact.todo.dto.TestRequestBodyDTO;
import com.starlab.withreact.todo.dto.TodoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test") // URI 경로
public class TestController {

    @GetMapping
    public String testController() {
        return "Hello World";
    }

    @GetMapping("/{id}")
    public String testControllerWithPathVariable(@PathVariable(required = false) int id)
    {
        return "Hello World! ID " + id;
    }

    // http://localhost:8084/test/testRequestParam?name=%EB%AC%B8%ED%98%84%EC%A4%80&id=1234
    @GetMapping("/testRequestParam")
    public String testControllerWithRequestParam(@RequestParam("name") String name, @RequestParam("id") int id) {
        return "Hello World! Name " + name + " Id " + id;
    }

    @GetMapping("/testRequestBody")
    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
        return "Hello World! ID " + testRequestBodyDTO.getId() + " Message " + testRequestBodyDTO.getMessage();
    }

    @GetMapping("/testResponseBody")
    public ResponseDTO<String> testControllerResponseBody() {
        List<String> list = new ArrayList<>();
        list.add("Hello World!. moon's response DTO!");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return response;
    }

    @GetMapping("/testResponseEntity")
    public ResponseEntity<?> testControllerResponseEntity() {
        List<String> list = new ArrayList<>();
        list.add("Hello World! I'm entitiy. you got 400 error");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        // http status를 400으로 설정한다.
        return ResponseEntity.badRequest().body(response);
    }

}
