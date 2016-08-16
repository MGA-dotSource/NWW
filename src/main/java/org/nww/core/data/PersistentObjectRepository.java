/*******************************************************************************
 * Copyright 2014-2015 MGA, garth-3d
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.nww.core.data;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The base interface for all repository implementations.
 * @author MGA
 *
 */
@NoRepositoryBean
public interface PersistentObjectRepository<T extends PersistentObject> extends PagingAndSortingRepository<T, String> {
	
	/**
	 * Set the {@link MongoOperations} object used to connect to the underlying database.
	 * @param operations the connection handler
	 */
	public void setMongoOperations(MongoOperations operations);
	
	/**
	 * Creates a new instance of the repositorys data type.
	 * @return a new instance of the managed pojo data type
	 */
	public T createNew();
	
	/**
	 * Execute a query and return the first result.
	 * @param q the query to be executed
	 * @return the first result
	 */
	public T executeQuery(Query q);
	
	/**
	 * Execute a query and return a list of results.
	 * @param q the query to be executed
	 * @return list of results
	 */
	public List<? extends T> executeListQuery(Query q);
	
	/**
	 * Execute a query and return a page of results.
	 * @param q the query to be execucted
	 * @param p the paging and sorting information
	 * @return page of results
	 */
	public Page<? extends T> executePageQuery(Query q, Pageable p);
}
