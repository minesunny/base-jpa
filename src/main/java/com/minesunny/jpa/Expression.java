package com.minesunny.jpa;


import com.minesunny.jpa.Operator;
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
                                 CriteriaBuilder builder){

        Path<T> expression = root.get(fieldName);
        return switch (operator) {
            case EQ -> builder.equal(expression, value);
            case NE -> builder.notEqual(expression, value);
            case LIKE -> builder.like((Path<String>)expression, "%" + value + "%");
            case LT -> builder.lessThan(expression, value);
            case GT -> builder.greaterThan(expression,  value);
            case LTE -> builder.lessThanOrEqualTo(expression, value);
            case GTE -> builder.greaterThanOrEqualTo(expression, value);
            case IS_NULL -> builder.isNull(expression);
            case NOT_NULL -> builder.isNotNull(expression);
        };
    }

}
