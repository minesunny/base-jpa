package com.minesunny.jpa.repository;

import com.minesunny.jpa.MineSpecification;
import com.minesunny.jpa.autoconfigure.BaseJpaAutoConfiguration;
import com.minesunny.jpa.entity.TestEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ImportAutoConfiguration(BaseJpaAutoConfiguration.class)
class BaseEntityEqTest {
    @Autowired
    TestEntityRepository entityRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    @Commit
    @Order(value = 1)
    void testLongAndEq() {
        TestEntity testSave = TestEntity.builder()
                .name("testSave")
                .build();
        entityRepository.saveAndFlush(testSave);
        MineSpecification<TestEntity> specification = testSave.andEq("id").specification();
        List<TestEntity> all = entityRepository.findAll(specification);
        assertEquals(1, all.size());
    }

    @Test
    @Commit
    @Order(value = 2)
    void testLongOrEq() {
        TestEntity testSave = TestEntity.builder()
                .name("testSave")
                .build();
        entityRepository.saveAndFlush(testSave);
        List<TestEntity> all = entityRepository.findAll(testSave.orEq("id").specification());
        assertEquals(2, all.size());
    }

}