package com.fullstack.movies.backend.repositories;

import com.fullstack.movies.backend.models.entities.MyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MyEntityRepository extends JpaRepository<MyEntity, UUID> {

    List<MyEntity> findAllByName(String name);

    @Query("SELECT e FROM my_entity e WHERE e.age = :age")
    List<MyEntity> findAllByAge(Integer age);

    @Query(nativeQuery = true, value = "SELECT * FROM my_entity WHERE name = ?1")
    List<MyEntity> findAllByNameNative(String name);
}
