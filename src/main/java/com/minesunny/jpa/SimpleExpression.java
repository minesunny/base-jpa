package com.minesunny.jpa;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SimpleExpression extends Expression<String> {

    public SimpleExpression(String fieldName, String value, Operator operator, boolean and) {
        super(fieldName, value, operator, and);
    }
}