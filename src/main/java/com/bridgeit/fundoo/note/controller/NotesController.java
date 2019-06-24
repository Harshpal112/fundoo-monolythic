package com.bridgeit.fundoo.note.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.fundoo.label.model.Labels;
import com.bridgeit.fundoo.note.dTO.NotesDto;
import com.bridgeit.fundoo.note.model.Notes;
import com.bridgeit.fundoo.note.service.ElasticSearch;
import com.bridgeit.fundoo.note.service.NotesService;
import com.bridgeit.fundoo.response.Response;
import com.bridgeit.fundoo.user.model.User;

@RestController
@RequestMapping("/note")
public class NotesController {

	@Autowired
	NotesService notesservice;
	@Autowired
	ElasticSearch elasticsearch;

	@PostMapping("/create")
	public ResponseEntity<Response> create(@RequestBody NotesDto notes, @RequestHeader String Token)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = notesservice.create(notes, Token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<Response> update(@RequestBody NotesDto notes, @RequestHeader String Token,
			@RequestParam long id) throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = notesservice.update(notes, Token, id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/trash")
	public ResponseEntity<Response> isTrash(@RequestParam long id, @RequestHeader String Token)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = notesservice.isTrash(id, Token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Response> delete(@RequestParam long id, @RequestHeader String token)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = notesservice.delete(id, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/pin")
	public ResponseEntity<Response> ispin(@RequestParam long id, @RequestHeader String Token)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = notesservice.isPin(id, Token);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("/archive")
	public ResponseEntity<Response> isArchive(@RequestParam long id, @RequestHeader String Token)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = notesservice.isArchive(id, Token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getnotes")
	public List<Notes> getnotes(@RequestHeader String Token)
			throws IllegalArgumentException, UnsupportedEncodingException {
		List<Notes> listnotes = notesservice.getnotes(Token);
		return listnotes;
	}

	@PutMapping("/addlabelnote")
	public ResponseEntity<Response> addlabeltonotes(@RequestHeader String Token, @RequestParam long noteid,
			@RequestParam long labelid) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = notesservice.addlabeltonote(Token, noteid, labelid);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/removelabelnote")
	public ResponseEntity<Response> removelabeltonotes(@RequestHeader String Token, @RequestParam long noteid,
			@RequestParam long labelid) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = notesservice.removelabeltonote(Token, noteid, labelid);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getlistoflabel")
	public List<Labels> getnotes(@RequestHeader String Token, @RequestParam long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		List<Labels> listnotes = notesservice.getlistoflabel(Token, noteid);
		return listnotes;
	}

	@PutMapping("/setreminder")
	public ResponseEntity<Response> setreminder(@RequestHeader String Token, @RequestParam String time,
			@RequestParam long noteid) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = notesservice.setreminder(Token, time, noteid);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/removereminder")
	public ResponseEntity<Response> removereminder(@RequestHeader String Token, @RequestParam long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = notesservice.removereminder(Token, noteid);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/updatereminder")
	public ResponseEntity<Response> updatereminder(@RequestHeader String Token, @RequestParam String time,
			@RequestParam long noteid) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = notesservice.updatereminder(Token, time, noteid);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/addcollaborater")
	public ResponseEntity<Response> addcollaborater(@RequestHeader String Token, @RequestParam long noteid,
			@RequestParam String emailid) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = notesservice.addcolaborator(Token, noteid, emailid);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PutMapping("/removecollaborater")
	public ResponseEntity<Response> removecollaborater(@RequestHeader String Token, @RequestParam long noteid,
			@RequestParam String emailid) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = notesservice.removecolaborator(Token, noteid, emailid);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getlistofcollaborator")
	public List<User> getcollaborator(@RequestHeader String Token, @RequestParam long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		System.out.println("Inside getCollaborator");
		List<User> listnotes = notesservice.getlistofcollaborator(Token, noteid);
		return listnotes;
	}

	@GetMapping("/getlistofArchieve")
	public List<Notes> getlistofarchieve(@RequestHeader String token)
			throws IllegalArgumentException, UnsupportedEncodingException {
		List<Notes> listnotes = notesservice.getlistofarchieve(token);
		return listnotes;
	}

	@GetMapping("/getlistoftrash")
	public List<Notes> getlistoftrash(@RequestHeader String token)
			throws IllegalArgumentException, UnsupportedEncodingException {
		List<Notes> listnotes = notesservice.getlistoftrash(token);
		return listnotes;
	}

	@GetMapping("/getlistofpin")
	public List<Notes> getlistofpin(@RequestHeader String token)
			throws IllegalArgumentException, UnsupportedEncodingException {
		List<Notes> listnotes = notesservice.getlistofpin(token);
		return listnotes;
	}

	@GetMapping("/getnotefromredis")
	public Notes getsinglenotefromredis(@RequestParam long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Notes note = notesservice.getsinglenotefromredis1(noteid);
		return note;
	}

	@GetMapping("/getlistofnotefromredis")
	public Map<Object, Notes> getlistofnotefromredis() throws IllegalArgumentException, UnsupportedEncodingException {
		Map<Object, Notes> note = notesservice.getlistofnotefromredis();
		return note;
	}

	@GetMapping("/search")
	public List<Notes> search() throws Exception {
		return elasticsearch.findAll();
	}

	@GetMapping("/{id}")
	public Notes findById(@PathVariable String id) throws Exception {

		return elasticsearch.findByid(id);
	}
//	@GetMapping("/search")
//	public List<Notes> searchNote(@RequestHeader String token , @RequestParam String query) {
//	//List<Notes> notes =elasticsearch.searchData(query, token);
//	return notes;
//	}

}
