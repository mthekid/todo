package com.starlab.withreact.todo.service;

import com.starlab.withreact.todo.model.TodoEntity;
import com.starlab.withreact.todo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public String testService() {
        TodoEntity entity = TodoEntity.builder()
                .title("My first todo item")
                .build();

        // 저장
        repository.save(entity);

        // 검색
        TodoEntity savedEntity = repository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }

}
