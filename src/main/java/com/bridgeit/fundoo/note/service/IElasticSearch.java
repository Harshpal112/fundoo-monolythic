package com.bridgeit.fundoo.note.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgeit.fundoo.note.model.Notes;

@Service
public interface IElasticSearch {

	public Notes create(Notes notes);

	public Notes update(Notes notes) throws IOException;

	public void delete(Long noteid) throws IOException;

	List<Notes> findAll() throws Exception;

	Notes findByid(String id) throws Exception;

	List<Notes> searchData(String query, String userId) throws IllegalArgumentException, UnsupportedEncodingException;

}
