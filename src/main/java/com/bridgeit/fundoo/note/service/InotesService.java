package com.bridgeit.fundoo.note.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bridgeit.fundoo.label.model.Labels;
import com.bridgeit.fundoo.note.dTO.NotesDto;
import com.bridgeit.fundoo.note.model.Notes;
import com.bridgeit.fundoo.response.Response;
import com.bridgeit.fundoo.user.model.User;

@Service
public interface InotesService {
	public Response create(NotesDto notesdto, String token)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public Response update(NotesDto notesdto, String token, long id)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public Response delete(long id, String token) throws IllegalArgumentException, UnsupportedEncodingException;

	public Response isTrash(long id, String token) throws IllegalArgumentException, UnsupportedEncodingException;

	public Response isPin(long id, String token) throws IllegalArgumentException, UnsupportedEncodingException;

	public Response isArchive(long id, String token) throws IllegalArgumentException, UnsupportedEncodingException;

	public List<Notes> getnotes(String token) throws IllegalArgumentException, UnsupportedEncodingException;

	public Response addlabeltonote(String token, long noteid, long labelid)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public Response removelabeltonote(String token, long noteid, long labelid)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public List<Labels> getlistoflabel(String token, long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public Response setreminder(String token, String time, long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public Response removereminder(String token, long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public Response updatereminder(String token, String time, long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public Response addcolaborator(String token, long noteid, String emailid)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public Response removecolaborator(String token, long noteid, String emailid)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public List<User> getlistofcollaborator(String token, long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException;

	public List<Notes> getlistofarchieve(String token) throws IllegalArgumentException, UnsupportedEncodingException;

	public List<Notes> getlistoftrash(String token) throws IllegalArgumentException, UnsupportedEncodingException;

	public List<Notes> getlistofpin(String token) throws IllegalArgumentException, UnsupportedEncodingException;

	public Response addcolor(String token, long noteid, String color)
			throws IllegalArgumentException, UnsupportedEncodingException;



	Notes getsinglenotefromredis1(long id);

	Map<Object, Notes> getlistofnotefromredis();

}
