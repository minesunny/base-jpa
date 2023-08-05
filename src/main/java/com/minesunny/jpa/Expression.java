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
package com.minesunny.jpa;


import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Expression<T extends Comparable<T>> {
    protected String fieldName;
    protected T value;
    protected Operator operator;
    protected boolean and;

    @SuppressWarnings("unchecked")
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {

        Path<T> expression = root.get(fieldName);
        return switch (operator) {
            case EQ -> builder.equal(expression, value);
            case NE -> builder.notEqual(expression, value);
            case LIKE -> builder.like((Path<String>) expression, "%" + value + "%");
            case LT -> builder.lessThan(expression, value);
            case GT -> builder.greaterThan(expression, value);
            case LTE -> builder.lessThanOrEqualTo(expression, value);
            case GTE -> builder.greaterThanOrEqualTo(expression, value);
            case IS_NULL -> builder.isNull(expression);
            case NOT_NULL -> builder.isNotNull(expression);
        };
    }

}
