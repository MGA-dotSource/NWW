package org.nww.modules.search.orm.pinboard;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

public class ES_PinboardEntryRepositoryImpl implements ES_PinboardEntryRepositoryCustom {

	@Autowired
	private ElasticsearchTemplate esTemplate;
	
	@Override
	public Page<ES_PinboardEntry> findPinboardEntries(String q, Pageable p) {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.should(QueryBuilders.moreLikeThisQuery(ES_PinboardEntry.fieldNamesBoosted).addLikeText(q).boost(2f))
				.should(QueryBuilders.moreLikeThisQuery(ES_PinboardEntry.fieldNamesRegular).addLikeText(q));
		
		SearchQuery query = new NativeSearchQuery(queryBuilder).setPageable(p);
		query.addIndices(ES_PinboardEntry.INDEX_NAME);
		
		return esTemplate.queryForPage(query, ES_PinboardEntry.class);
	}
}
