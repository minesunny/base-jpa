package com.minesunny.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
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


	@Column(name = "message")
	@Comment(value = "about of the record")
	private String message;

    public Long getId() {
        return null;
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

    @Transient
    private  MineSpecification<? extends BaseEntity> specification;
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
    public <T extends BaseEntity> T expr(String fieldName, Object value, Operator operator, boolean and) {
        addExpr(new SimpleExpression(fieldName, value.toString(), operator, and));
        return (T) this;
    }
    @SuppressWarnings("unchecked")
    public  <T extends BaseEntity> T expr(String fieldName, Operator operator, boolean and) {
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
    public <T extends BaseEntity> MineSpecification<T> specification(){
        return (MineSpecification<T>) specification;
    }
}
