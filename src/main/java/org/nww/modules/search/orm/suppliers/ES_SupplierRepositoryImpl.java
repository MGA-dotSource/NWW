/**
 * 
 */
package org.nww.modules.search.orm.suppliers;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

/**
 * Implementation of the supplier elastic search repositories custom methods.
 * @author mga
 *
 */
public class ES_SupplierRepositoryImpl implements ES_SupplierRepositoryCustom {

	@Autowired
	private ElasticsearchTemplate esTemplate;
	
	/* (non-Javadoc)
	 * @see org.nww.modules.search.orm.suppliers.ES_SupplierRepositoryCustom#findSuppliers(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<ES_Supplier> findSuppliers(String q, Pageable p) {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.should(QueryBuilders.moreLikeThisQuery(ES_Supplier.fieldNamesBoosted).addLikeText(q).boost(2f))
				.should(QueryBuilders.moreLikeThisQuery(ES_Supplier.fieldNamesRegular).addLikeText(q));
		
		SearchQuery query = new NativeSearchQuery(queryBuilder).setPageable(p);
		query.addIndices(ES_Supplier.INDEX_NAME);
		
		return esTemplate.queryForPage(query, ES_Supplier.class);
	}

}
