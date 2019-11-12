package org.dragonli.jpatools.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dragonli.jpatools.IdEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Base entity class.
 * 
 * @author liaoxuefeng
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable, IdEntity {

	/**
	 * Primary key: auto-increment long.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * Created time (milliseconds).
	 */
	@Column(name = "createdAt",nullable = false, updatable = false)
	private Long createdAt;

	/**
	 * Updated time (milliseconds).
	 */
	@Column(name = "updatedAt",nullable = false)
	private Long updatedAt;

	/**
	 * Entity version: increment when update.
	 */
	@Column(nullable = false)
	@JsonIgnore
	@Version
	private Integer version;

	// hook for pre-insert:
	@PrePersist
	void preInsert() {
		if (this.createdAt == 0) {
			this.createdAt = this.updatedAt = System.currentTimeMillis();
		}
		this.version = 0;
	}

	// hook for pre-update:
	@PreUpdate
	protected void preUpdate() {
		this.updatedAt = System.currentTimeMillis();
		this.version++;
	}

//	@Transient
	@Override
	public Object selectEntityId() {
		return id;
	}

	@Override
	public void putEntityId(Object pk) {
		this.id = (Long) pk;
	}
}
