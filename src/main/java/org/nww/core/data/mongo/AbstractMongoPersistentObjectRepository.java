/**
 * 
 */
package org.nww.core.data.mongo;

import java.util.ArrayList;
import java.util.List;

import org.nww.core.data.PersistentObject;
import org.nww.core.data.PersistentObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author MGA
 *
 */
public abstract class AbstractMongoPersistentObjectRepository<T extends PersistentObject> implements PersistentObjectRepository<T> {

    private MongoOperations mongoOperations;
	
	/* (non-Javadoc)
	 * @see org.nww.core.data.PersistentObjectRepository#setMongoOperations(org.springframework.data.mongodb.core.MongoOperations)
	 */
	
    @Autowired @Override
	public void setMongoOperations(MongoOperations operations) {
		this.mongoOperations = operations;
	}
	
	/**
	 * Get the mongo connector class that allows querying and manipulating the stored data.
	 * @return the {@link MongoOperations} instance
	 */
	public MongoOperations getMongoOperations() {
		return this.mongoOperations;
	}
	
	/**
	 * Get the type of the pojo entity that is handled by the repository implementation.
	 * @return
	 */
	public abstract Class<T> getEntityClass();

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
	 */
	@Override
	public Iterable<T> findAll(Sort sort) {
		Query q = new Query();
		q.with(sort);
		
		return getMongoOperations().find(q, getEntityClass()); 
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<T> findAll(Pageable pageable) {
		Query q = new Query();
        q.with(pageable);

        long totalCount = getMongoOperations().count(q, getEntityClass());
        List<T> result = getMongoOperations().find(q, getEntityClass());

        return new PageImpl<>(result, pageable, totalCount);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#save(S)
	 */
	@Override
	public <S extends T> S save(S entity) {
		getMongoOperations().save(entity);
		
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#save(java.lang.Iterable)
	 */
	@Override
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		for (S entity : entities) {
			getMongoOperations().save(entity);
		}
		
		return entities;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findOne(java.io.Serializable)
	 */
	@Override
	public T findOne(String id) {
		return getMongoOperations().findById(id, getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#exists(java.io.Serializable)
	 */
	@Override
	public boolean exists(String id) {
		return getMongoOperations().findById(id, getEntityClass()) != null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll()
	 */
	@Override
	public Iterable<T> findAll() {
		return getMongoOperations().findAll(getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll(java.lang.Iterable)
	 */
	@Override
	public Iterable<T> findAll(Iterable<String> ids) {
		List<T> objects = new ArrayList<>();
		
		for (String id : ids) {
			objects.add(findOne(id));
		}
		
		return objects;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#count()
	 */
	@Override
	public long count() {
		return getMongoOperations().count(new Query(), getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#delete(java.io.Serializable)
	 */
	@Override
	public void delete(String id) {
		T object = findOne(id);
		if(null != object) {
			getMongoOperations().remove(object);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Object)
	 */
	@Override
	public void delete(T entity) {
		getMongoOperations().remove(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Iterable)
	 */
	@Override
	public void delete(Iterable<? extends T> entities) {
		for (T entity : entities) {
			getMongoOperations().remove(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#deleteAll()
	 */
	@Override
	public void deleteAll() {
		getMongoOperations().remove(new Query(), getEntityClass());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.core.data.PersistentObjectRepository#executeQuery(org.springframework.data.mongodb.core.query.Query)
	 */
	@Override
	public T executeQuery(Query q) {
		return getMongoOperations().findOne(q, getEntityClass());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.core.data.PersistentObjectRepository#executeListQuery(org.springframework.data.mongodb.core.query.Query)
	 */
	@Override
	public List<? extends T> executeListQuery(Query q) {
		return getMongoOperations().find(q, getEntityClass());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.core.data.PersistentObjectRepository#executePageQuery(org.springframework.data.mongodb.core.query.Query, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<? extends T> executePageQuery(Query q, Pageable p) {
        q.with(p);

        long totalCount = getMongoOperations().count(q, getEntityClass());
        List<T> result = getMongoOperations().find(q, getEntityClass());

        return new PageImpl<>(result, p, totalCount);
	}
}
