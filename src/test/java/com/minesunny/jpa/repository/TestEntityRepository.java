package com.minesunny.jpa.repository;

import com.minesunny.jpa.entity.TestEntityCached;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepository extends JpaRepositoryImplementation<TestEntityCached, Long> {
}
