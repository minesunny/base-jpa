package com.minsunny.jpa.repository;

import com.minsunny.jpa.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepository extends JpaRepositoryImplementation<TestEntity,Long> {
}
