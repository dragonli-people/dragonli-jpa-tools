package org.dragonli.jpatools.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface IRepository<T, ID extends Serializable> extends IRepositoryExpand<T, ID>,
        JpaSpecificationExecutor<T> {

}
