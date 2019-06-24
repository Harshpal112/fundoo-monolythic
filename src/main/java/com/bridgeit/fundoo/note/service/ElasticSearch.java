package com.bridgeit.fundoo.note.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgeit.fundoo.note.model.Notes;
import com.bridgeit.fundoo.util.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticSearch implements IElasticSearch {

	@Autowired
	private RestHighLevelClient client;

	@Autowired
	private ObjectMapper objectMapper;

	String INDEX = "dbnote";
	String TYPE = "typenote";

	@Override
	public Notes create(Notes notes) {

		@SuppressWarnings("unchecked")
		Map<String, Object> mapper = objectMapper.convertValue(notes, Map.class);

		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, String.valueOf(notes.getId())).source(mapper);

		try {
			client.index(indexRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {

			e.printStackTrace();
		}

		return notes;

	}

	@Override
	public Notes update(Notes notes) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, Object> mapper = objectMapper.convertValue(notes, Map.class);
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(notes.getId()));
		updateRequest.doc(mapper);
		client.update(updateRequest, RequestOptions.DEFAULT);

		return notes;
	}

	@Override
	public void delete(Long noteid) throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, String.valueOf(noteid));

		client.delete(deleteRequest, RequestOptions.DEFAULT);

	}

	@Override
	public List<Notes> findAll() throws Exception {

		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		return getSearchResult(searchResponse);

	}

	private List<Notes> getSearchResult(SearchResponse response) {

		SearchHit[] searchHit = response.getHits().getHits();

		List<Notes> notes = new ArrayList<>();

		if (searchHit.length > 0) {

			Arrays.stream(searchHit)
					.forEach(hit -> notes.add(objectMapper.convertValue(hit.getSourceAsMap(), Notes.class)));
		}

		return notes;
	}
	@Override
	public Notes findByid(String id) throws Exception {

		GetRequest getRequest = new GetRequest(INDEX, TYPE, id);

		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
		Map<String, Object> resultMap = getResponse.getSource();

		return objectMapper.convertValue(resultMap, Notes.class);

	}
	@Override
	public List<Notes> searchData(String query, String userId) throws IllegalArgumentException, UnsupportedEncodingException {
		
		System.out.println("Query :" + query);
		SearchRequest searchRequest = new SearchRequest(INDEX).types(TYPE);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery("*" + query + "*")
				.analyzeWildcard(true).field("title").field("description"))
				.filter(QueryBuilders.termsQuery("userId", String.valueOf(userId)));
		System.out.println();
		searchSourceBuilder.query(queryBuilder);
		System.out.println("Query builder :" + queryBuilder);
		searchRequest.source(searchSourceBuilder);
		System.out.println("Search Request : " + searchRequest);
		SearchResponse searchResponse = null;
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			System.out.println(searchResponse);
		} catch (IOException e) {

			e.printStackTrace();
		}

		List<Notes> allnote = getSearchResult(searchResponse);

		return allnote;
	}
}
