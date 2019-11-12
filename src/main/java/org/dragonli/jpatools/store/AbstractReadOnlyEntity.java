package org.dragonli.jpatools.store;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;

/**
 * Base read-only entity class.
 * 
 * @author liaoxuefeng
 */
@MappedSuperclass
public class AbstractReadOnlyEntity implements Serializable {

	/**
	 * Primary key: auto-increment long.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	/**
	 * Created time (milliseconds).
	 */
	@Column(name = "created_at",nullable = false, updatable = false)
	private long createdAt;

	// hook for pre-insert:
	@PrePersist
	void preInsert() {
		if (this.createdAt == 0) {
			this.createdAt = System.currentTimeMillis();
		}
	}

	// hook for pre-update:
	@PreUpdate
	void preUpdate() {
		throw new UnsupportedOperationException("Cannot update read-only entity: " + getClass().getSimpleName());
	}
}
