package com.kydas.build.core.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.HashMap;
import java.util.List;

public class FilterUtils {
    public static <E> void equal(
        String field, List<Predicate> predicates, HashMap<String, Object> filter, CriteriaBuilder cb, Root<E> root
    ) {
        if (filter.containsKey(field)) {
            predicates.add(cb.equal(root.get(field), filter.get(field)));
        }
    }
}
