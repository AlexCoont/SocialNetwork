package project.models.util.entity;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class EntityViolation<T> {

    private Set<ConstraintViolation<T>> violations;

    public EntityViolation(Set<ConstraintViolation<T>> violations) {
        this.violations = violations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(ConstraintViolation<T> violation : violations)
            sb.append("field ").append(violation.getPropertyPath()).append(" - ").append(violation.getMessage()).append("; ");
        return sb.toString();
    }
}
