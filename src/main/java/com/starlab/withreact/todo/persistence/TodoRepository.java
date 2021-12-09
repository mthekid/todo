package com.starlab.withreact.todo.persistence;

import com.starlab.withreact.todo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    // nativeQuery = true로 지정하면 value로 지정한 쿼리가 Native 쿼리로 사용된다.
    @Query(value = "FROM TodoEntity t " +
            "WHERE t.userId = :userId")
    List<TodoEntity> findAllByUserId(
            @Param("userId") String userId);
}
