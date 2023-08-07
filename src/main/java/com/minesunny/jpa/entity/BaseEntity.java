/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package com.minesunny.jpa.entity;

import com.minesunny.jpa.Expression;
import com.minesunny.jpa.MineSpecification;
import com.minesunny.jpa.Operator;
import com.minesunny.jpa.exception.SimpleExpression;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;


@ToString(callSuper = true)
@MappedSuperclass
@SuperBuilder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    @Column(name = "create_at")
    @Comment(value = "create time of the record")
    @CreationTimestamp
    private Date createAt;


    @Column(name = "update_at")
    @Comment(value = "update time of the record")
    @LastModifiedDate
    private Date updateAt;

    @Column(name = "delete_at")
    @Comment(value = "delete time of the record")
    private Date deleteAt;

    @Version
    private Integer version;

    @Column(name = "message")
    @Comment(value = "about of the record")
    private String message;
    @Transient
    private MineSpecification<? extends BaseEntity> specification;

    public Long getId() {
        return null;
    }

    public void setId(Long id) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BaseEntity baseEntity = (BaseEntity) o;
        return getId() != null && Objects.equals(getId(), baseEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    private void addExpr(Expression<?> expr) {
        if (expr == null) {
            return;
        }
        if (specification == null) {
            specification = new MineSpecification<>();
        }
        specification.addExpr(expr);
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseEntity> T expr(String fieldName, Object value, Operator operator, boolean and) {
        addExpr(new SimpleExpression(fieldName, value.toString(), operator, and));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseEntity> T expr(String fieldName, Operator operator, boolean and) {
        if (Objects.isNull(fieldName)) {
            return (T) this;
        }
        Field field = ReflectionUtils.findField(this.getClass(), fieldName);
        if (Objects.isNull(field)) {
            return (T) this;
        }
        field.setAccessible(true);
        Object value = ReflectionUtils.getField(field, this);
        if (Objects.isNull(value)) {
            return (T) this;
        }
        return expr(fieldName, value, operator, and);
    }

    public <T extends BaseEntity> T eq(String fieldName, boolean and) {
        return expr(fieldName, Operator.EQ, and);
    }

    public <T extends BaseEntity> T andEq(String fieldName) {
        return eq(fieldName, true);
    }

    public <T extends BaseEntity> T orEq(String fieldName) {
        return eq(fieldName, false);

    }

    public <T extends BaseEntity> T ne(String fieldName, boolean and) {
        return expr(fieldName, Operator.NE, and);
    }

    public <T extends BaseEntity> T andNe(String fieldName) {
        return ne(fieldName, true);
    }

    public <T extends BaseEntity> T orNeq(String fieldName) {
        return ne(fieldName, false);

    }


    public <T extends BaseEntity> T like(String fieldName, boolean and) {
        return expr(fieldName, Operator.LIKE, and);
    }

    public <T extends BaseEntity> T andlike(String fieldName) {
        return expr(fieldName, Operator.LIKE, true);
    }

    public <T extends BaseEntity> T orlike(String fieldName) {
        return expr(fieldName, Operator.LIKE, false);
    }

    public <T extends BaseEntity> T gt(String fieldName, boolean and) {
        return expr(fieldName, Operator.GT, and);
    }

    public <T extends BaseEntity> T andGt(String fieldName) {
        return expr(fieldName, Operator.GT, true);
    }

    public <T extends BaseEntity> T orGt(String fieldName) {
        return expr(fieldName, Operator.GT, false);
    }

    public <T extends BaseEntity> T gte(String fieldName, boolean and) {
        return expr(fieldName, Operator.GTE, and);
    }

    public <T extends BaseEntity> T andGte(String fieldName) {
        return expr(fieldName, Operator.GTE, true);
    }

    public <T extends BaseEntity> T orGte(String fieldName) {
        return expr(fieldName, Operator.GTE, false);
    }


    public <T extends BaseEntity> T lt(String fieldName, boolean and) {
        return expr(fieldName, Operator.LT, and);
    }


    public <T extends BaseEntity> T andLt(String fieldName) {
        return expr(fieldName, Operator.LT, true);
    }


    public <T extends BaseEntity> T orLt(String fieldName) {
        return expr(fieldName, Operator.LT, false);
    }


    public <T extends BaseEntity> T lte(String fieldName, boolean and) {
        return expr(fieldName, Operator.LTE, and);
    }


    public <T extends BaseEntity> T andLte(String fieldName) {
        return expr(fieldName, Operator.LTE, true);
    }


    public <T extends BaseEntity> T orLte(String fieldName) {
        return expr(fieldName, Operator.LTE, false);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> MineSpecification<T> specification() {
        return (MineSpecification<T>) specification;
    }
}
