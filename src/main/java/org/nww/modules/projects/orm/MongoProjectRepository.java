/**
 * 
 */
package org.nww.modules.projects.orm;

import java.util.List;

import org.nww.core.data.mongo.AbstractMongoPersistentObjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * The MongoDB implementation of the {@link ProjectRepository} interface.
 * @author mga
 */
public class MongoProjectRepository extends AbstractMongoPersistentObjectRepository<ProjectImpl>
		implements ProjectRepository<ProjectImpl> {

	/* (non-Javadoc)
	 * @see org.nww.core.data.PersistentObjectRepository#createNew()
	 */
	@Override
	public ProjectImpl createNew() {
		return new ProjectImpl();
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectRepository#findByNameAndOwner(java.lang.String, java.lang.String)
	 */
	@Override
	public ProjectImpl findByNameAndOwner(String name, String ownerUUID) {
		Query q = new Query(Criteria.where("name").is(name.trim()).and("ownerUUID").is(ownerUUID));
		
		return getMongoOperations().findOne(q, getEntityClass());
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectRepository#findAllByOwnerUUID(java.lang.String)
	 */
	@Override
	public List<ProjectImpl> findAllByOwnerUUID(String ownerUUID) {
		Query q = new Query(Criteria.where("ownerUUID").is(ownerUUID));
		
		return getMongoOperations().find(q, getEntityClass());
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectRepository#findAllByOwnerUUID(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<ProjectImpl> findAllByOwnerUUID(String ownerUUID, Pageable p) {
		Query q = new Query(Criteria.where("ownerUUID").is(ownerUUID));
		// get total count without pageable limiting the result
		long total = getMongoOperations().count(q, getEntityClass());
		// add paging and sorting information to query
		q.with(p);
		
		return new PageImpl<>(getMongoOperations().find(q, getEntityClass()), p, total);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectRepository#findAllBySupplierUUID(java.lang.String)
	 */
	@Override
	public List<ProjectImpl> findAllBySupplierUUID(String supplierUUID) {
		Query q = new Query(Criteria.where("supplierUUID").in(supplierUUID));
		
		return getMongoOperations().find(q, getEntityClass());
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectRepository#findAllBySupplierUUID(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<ProjectImpl> findAllBySupplierUUID(String supplierUUID, Pageable p) {
		Query q = new Query(Criteria.where("supplierUUID").in(supplierUUID));
		
		long total = getMongoOperations().count(q, getEntityClass());
		
		q.with(p);
		
		return new PageImpl<>(getMongoOperations().find(q, getEntityClass()), p, total);
	}

	/* (non-Javadoc)
	 * @see org.nww.core.data.mongo.AbstractMongoPersistentObjectRepository#getEntityClass()
	 */
	@Override
	public Class<ProjectImpl> getEntityClass() {
		return ProjectImpl.class;
	}
}
