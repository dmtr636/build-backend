package com.kydas.build.core.crud;

import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.NotFoundException;
import com.kydas.build.core.filter.FilterDTO;
import com.kydas.build.core.filter.FilterOrderDTO;
import com.kydas.build.core.filter.FilterSpecification;
import com.kydas.build.core.filter.FilterV2Request;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class BaseService<E, DTO> {
    private final Class<E> entityClass;

    @Getter
    @Autowired
    private BaseRepository<E> repository;
    @Autowired
    private EntityManager entityManager;

    public List<E> getAll() throws ApiException {
        return repository.findAll();
    }

    public Long getAllCount() {
        return repository.count();
    }

    public E getById(UUID id) throws ApiException {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public abstract E makeEntity(DTO dto) throws ApiException;

    public abstract E create(DTO dto) throws ApiException;

    public List<E> create(List<DTO> dtos) throws ApiException {
        var entities = new ArrayList<E>();
        for (var dto : dtos) {
            entities.add(makeEntity(dto));
        }
        return repository.saveAll(entities);
    }

    public abstract E update(DTO dto) throws ApiException;

    public E save(E entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) throws ApiException {
        repository.deleteById(id);
    }

    public List<E> getByFilter(FilterV2Request filterRequest) {
        Specification<E> specification = new FilterSpecification<>(filterRequest.getFilter());

        Sort sort = Sort.by(Sort.Direction.fromString(filterRequest.getOrder().getDirection()), filterRequest.getOrder().getField());
        PageRequest pageRequest = PageRequest.of(filterRequest.getOffset() / filterRequest.getLimit(), filterRequest.getLimit(), sort);

        return repository.findAll(specification, pageRequest).getContent();
    }

    public List<E> getByFilter(FilterDTO<HashMap<String, Object>> dto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(entityClass);
        Root<E> root = query.from(entityClass);

        List<Predicate> predicates = new ArrayList<>();
        predicates = getFilterQueryPredicates(predicates, dto.getFilter(), cb, root);
        query.where(predicates.toArray(new Predicate[0]));

        var order = dto.getOrder();
        if (order != null) {
            if (order.getDirection().equals(FilterOrderDTO.Direction.ASC)) {
                query.orderBy(cb.asc(root.get(order.getField())));
            } else {
                query.orderBy(cb.desc(root.get(order.getField())));
            }
        }

        return entityManager.createQuery(query)
            .setFirstResult(dto.getOffset())
            .setMaxResults(dto.getLimit())
            .getResultList();
    }

    public long getCountByFilter(FilterDTO<HashMap<String, Object>> dto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(entityClass);
        Root<E> root = query.from(entityClass);

        List<Predicate> predicates = new ArrayList<>();
        predicates = getFilterQueryPredicates(predicates, dto.getFilter(), cb, root);
        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query)
            .setFirstResult(dto.getOffset())
            .setMaxResults(dto.getLimit())
            .getResultStream()
            .count();
    }

    public List<Predicate> getFilterQueryPredicates(
        List<Predicate> predicates, HashMap<String, Object> filterObject, CriteriaBuilder cb, Root<E> root
    ) {
        return predicates;
    }
}
