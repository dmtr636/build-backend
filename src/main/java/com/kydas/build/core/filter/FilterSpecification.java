package com.kydas.build.core.filter;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class FilterSpecification<T> implements Specification<T> {

    private final Map<String, Object> filters;

    public FilterSpecification(Map<String, Object> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate finalPredicate = cb.conjunction();

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                Map<String, Object> conditions = (Map<String, Object>) value;
                for (Map.Entry<String, Object> condition : conditions.entrySet()) {
                    switch (condition.getKey()) {
                        case "ilike":
                            finalPredicate = cb.and(finalPredicate,
                                    cb.like(cb.lower(root.get(fieldName)), "%" + condition.getValue().toString().toLowerCase() + "%"));
                            break;
                        case "values":
                            finalPredicate = cb.and(finalPredicate, root.get(fieldName).in((List<?>) condition.getValue()));
                            break;
                        case "from":
                            finalPredicate = cb.and(finalPredicate, cb.greaterThanOrEqualTo(root.get(fieldName), ZonedDateTime.parse(condition.getValue().toString())));
                            break;
                        case "to":
                            finalPredicate = cb.and(finalPredicate, cb.lessThanOrEqualTo(root.get(fieldName), ZonedDateTime.parse(condition.getValue().toString())));
                            break;
                    }
                }
            }
        }

        return finalPredicate;
    }
}
