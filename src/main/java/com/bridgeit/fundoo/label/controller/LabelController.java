package com.bridgeit.fundoo.label.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.fundoo.label.dTO.LabelDto;
import com.bridgeit.fundoo.label.model.Labels;
import com.bridgeit.fundoo.label.service.LabelService;
import com.bridgeit.fundoo.note.model.Notes;
import com.bridgeit.fundoo.response.Response;

@RestController
public class LabelController {
	@Autowired
	LabelService labelservice;

	@PostMapping("/labelcreate")
	public ResponseEntity<Response> create(@RequestBody LabelDto label, @RequestHeader String Token)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = labelservice.create(label, Token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/labelupdate")
	public ResponseEntity<Response> update(@RequestBody LabelDto label, @RequestHeader String Token,
			@RequestParam long id) throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = labelservice.update(label, Token, id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/labeldelete")
	public ResponseEntity<Response> delete(@RequestParam long id, @RequestHeader String token)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = labelservice.delete(id, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getlabels")
	public List<Labels> getlabels(@RequestHeader String token)
			throws IllegalArgumentException, UnsupportedEncodingException {
		List<Labels> labels = labelservice.getlabels(token);
		return labels;

	}

	@PutMapping("/addnotelabel")
	public ResponseEntity<Response> addnotestolabel(@RequestHeader String Token, @RequestParam long noteid,
			@RequestParam long labelid) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = labelservice.addnotetolabel(Token, noteid, labelid);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/removenotelabel")
	public ResponseEntity<Response> removenotestolabel(@RequestHeader String Token, @RequestParam long noteid,
			@RequestParam long labelid) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = labelservice.removenotetolabel(Token, noteid, labelid);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getlistofnotes")
	public List<Notes> getlabels(@RequestHeader String token, @RequestParam long labelid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		List<Notes> notes = labelservice.getlistofnotes(token, labelid);
		return notes;
	}
	
}
