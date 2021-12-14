package com.starlab.withreact.todo.service;

import com.starlab.withreact.todo.model.TodoEntity;
import com.starlab.withreact.todo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    // Create(PostMapping) 파트
    public List<TodoEntity> create(final TodoEntity entity) {
        // Validations
        validate(entity);

        repository.save(entity);

        log.info("Entity Id : {} is saved", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }

    // retrieve(GetMapping) 파트
    public List<TodoEntity> retrieve(final String userId) {
        log.info("retrieve userId -> {}", userId);
        return repository.findByUserId(userId);
    }

    private void validate(TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("UnKnown user.");
        }
    }

    // update 파트
    public List<TodoEntity> update(final TodoEntity entity) {
        // 저장할 엔티티가 유효한지 확인한다.
        validate(entity);

        // 넘겨받은 엔티티 ID를 이용해 TodoEntity를 가져온다. [ 존재해야 업데이트가 가능하다. ]
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo -> {
            // 새로운 entity 값으로 덮어씌운다.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            repository.save(todo);
        });

        log.info("update : Entity Id : {} is updated", entity.getId());

        return retrieve(entity.getUserId());
    }

    // Delete 파트
    public List<TodoEntity> delete(final TodoEntity entity) {
        validate(entity);

        try {
            repository.delete(entity);
        } catch(Exception e) {
            log.error("error deleting entity ", entity.getId(), e);

            // 컨트롤러로 exception을 던진다. [ DB 내부 로직을 캡슐화하기 위해서는 e를 리턴하지않고 새로운 exception 오브젝트를 리턴한다. ]
            throw new RuntimeException("error deleting entity " + entity.getId());
        }

        return retrieve(entity.getUserId());
    }
}
