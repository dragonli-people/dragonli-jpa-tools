package org.dragonli.jpatools.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface IRepositoryExpand<T, ID extends Serializable> extends JpaRepository<T, ID> {

    String testFindExpandBase();

    <R> R findOne(Specification<T, R> spec, Class<R> rClass) ;

    <R> List<R> findAll(Specification<T, R> spec, Class<R> rClass) ;

    T get(ID id);

    int update(SpecificationUpdate<T> spec);

    interface Specification<T, R> {
        void toCriteriaQuery(Root<T> root, CriteriaQuery<R> query, CriteriaBuilder cb);
    }

    interface SpecificationUpdate<T> {
        void toCriteriaQuery(Root<T> root, CriteriaUpdate<T> query, CriteriaBuilder cb) throws Exception;
    }
    
    void refresh(T entity);
}
