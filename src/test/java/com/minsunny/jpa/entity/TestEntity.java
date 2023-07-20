package com.minsunny.jpa.entity;

import com.minesunny.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_entity")
@Getter
@Setter
@SQLDelete(sql = "update test_entity set delete_at = current_timestamp where delete_at is null")
@Where(clause = "delete_at is null")
@ToString
public class TestEntity extends BaseEntity {
    @Id
    @GeneratedValue()
    private Long id;
    @Column
    private String name = "testEntity";
}
