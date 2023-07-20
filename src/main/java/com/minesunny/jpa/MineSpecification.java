package com.minesunny.jpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MineSpecification<T extends BaseEntity> implements Specification<T> {
    private final List<Expression<?>> expressions;



    public MineSpecification() {
        this.expressions = new ArrayList<>();
    }

    public void addExpr(Expression<?> expr) {
        expressions.add(expr);
    }
    @Override
    public Specification<T> and(Specification<T> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<T> or(Specification<T> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (!expressions.isEmpty()) {
            List<Predicate> andPredicates = new ArrayList<>();
            List<Predicate> orPredicates = new ArrayList<>();
            for (Expression<?> expr : expressions) {
                Predicate predicate = expr.toPredicate(root, query, criteriaBuilder);
                if (expr.isAnd()) {
                    andPredicates.add(predicate);
                }else{
                    orPredicates.add(predicate);
                }
            }
            if (!andPredicates.isEmpty()) {
                orPredicates.add(criteriaBuilder.and(andPredicates.toArray(new Predicate[0])));
            }
            if (!andPredicates.isEmpty()) {
                return criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
            }
        }
        return criteriaBuilder.conjunction();
    }
}
