package org.dragonli.jpatools.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepositoryExpand<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements IRepositoryExpand<T, ID>
{

    protected final EntityManager em ;
    protected final JpaEntityInformation<T, ?> entityInformation;
    protected final Class<T> domainClass;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AbstractRepositoryExpand(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
        this.domainClass = domainClass;
        //这行话是有风险的，如果代码逻辑将来有更新……
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
    }

    public AbstractRepositoryExpand(JpaEntityInformation<T, ?> entityInformation, EntityManager em){
        super(entityInformation,em);
        this.entityInformation = entityInformation;
        this.em = em;
        this.domainClass = entityInformation.getJavaType();
    }

    public T get(ID id){
        Optional<T> optional = this.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    public AbstractExpand(JpaEntityInformation<T, ?> entityInformation
//            , @Autowired @Qualifier("entityManagerAssets") //这里有问题，有个选择数据源的过程，可能需要分父
//                                          EntityManager entityManager) {
////        super(entityInformation, entityManager);
//        this.em = entityManager;
//        this.entityInformation = entityInformation;
//    }
    
    @Override
    @Transactional
    public void refresh(T entity) {
        em.refresh(entity);
    }

    public <R> R findOne(Specification<T, R> spec, Class<R> rClass) {
        return createQuery(spec, rClass).getSingleResult();
    }

    public <R> List<R> findAll(Specification<T, R> spec, Class<R> rClass) {
        return createQuery(spec, rClass).getResultList();
    }

    public String testFindExpandBase(){return "findExpandBase";}

    public int update(SpecificationUpdate<T> spec) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(domainClass);
        Root<T> root = criteriaUpdate.from(domainClass);
        try {
            spec.toCriteriaQuery(root, criteriaUpdate, criteriaBuilder);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
        return em.createQuery(criteriaUpdate).executeUpdate();
    }

    private <R> TypedQuery<R> createQuery(Specification<T, R> spec, Class<R> rClass) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<R> criteriaQuery = criteriaBuilder.createQuery(rClass);
        Root<T> root = criteriaQuery.from(domainClass);
        spec.toCriteriaQuery(root, criteriaQuery, criteriaBuilder);
        return em.createQuery(criteriaQuery);
    }

    
    
}
