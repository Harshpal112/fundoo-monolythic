 package com.bridgeit.fundoo.label.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgeit.fundoo.exception.Exception;
import com.bridgeit.fundoo.label.dTO.LabelDto;
import com.bridgeit.fundoo.label.model.Labels;
import com.bridgeit.fundoo.label.repository.LabelRepo;
import com.bridgeit.fundoo.note.model.Notes;
import com.bridgeit.fundoo.note.repository.NotesRepository;
import com.bridgeit.fundoo.response.Response;
import com.bridgeit.fundoo.user.model.User;
import com.bridgeit.fundoo.user.repository.UserRepository;
import com.bridgeit.fundoo.util.ResponseStatus;
import com.bridgeit.fundoo.util.TokenGenerator;

@PropertySource("classpath:message.properties")
@Service
public class LabelService implements IlabelService {
	@Autowired
	LabelRepo labelrepository;
	@Autowired
	ModelMapper modelmapper;
	@Autowired
	UserRepository userrepository;
	@Autowired
	Environment environment;
	@Autowired
	TokenGenerator tokengenerator;
	@Autowired
	NotesRepository notesrepository;

	@Override
	public Response create(LabelDto label, String token) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long userid = TokenGenerator.decodeToken(token);
		Optional<Labels> labels = labelrepository.findByLabelname(label.getLabelname());
		if (labels.isPresent()) {
			System.out.println("label alredy present");
		} else {
			Labels labe = modelmapper.map(label, Labels.class);
			Optional<User> user = userrepository.findByUserid(userid);
			labe.setUserid(userid);
			labe.setModifieddate(LocalDateTime.now());
			labe.setRegistereddate(LocalDateTime.now());
			user.get().getLabels().add(labe);
			labelrepository.save(labe);
			userrepository.save(user.get());
//			nnoteservice.create(notessdto,string token)
			response = ResponseStatus.statusinfo(environment.getProperty("status.label.created"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));
			return response;
		}
		throw new Exception(environment.getProperty("status.label.failedCreate"),
				Integer.parseInt(environment.getProperty("status.regsuccess.code")));

	}

	@Override
	public Response update(LabelDto labell, String token, long id)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response;
		long userid = TokenGenerator.decodeToken(token);
		Labels label = labelrepository.findByLabelidAndUserid(id, userid);
		System.out.println(labell);
		label.setLabelname(labell.getLabelname());
		Labels status = labelrepository.save(label);
		if (status == null) {
			throw new Exception(environment.getProperty("status.label.failedUpdate"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));

		} else {
			response = ResponseStatus.statusinfo(environment.getProperty("status.label.updated"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));

			return response;
		}
	}

	@Override
	public Response delete(long id, String token) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long userid = TokenGenerator.decodeToken(token);
		Labels label = labelrepository.findByLabelidAndUserid(id, userid);
		if (label.getLabelid() == id) {
			labelrepository.delete(label);
			response = ResponseStatus.statusinfo(environment.getProperty("status.label.removedfromnote"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));
			return response;
		}

		throw new Exception(environment.getProperty("status.label.notdeleted"),
				Integer.parseInt(environment.getProperty("status.regsuccess.code")));

	}

	@Override
	public List<Labels> getlabels(String token) throws IllegalArgumentException, UnsupportedEncodingException {
		long userid = TokenGenerator.decodeToken(token);
		List<Labels> labels = labelrepository.findByUserid(userid);
		List<Labels> userlist = new ArrayList<>();
		for (Labels userlabel : labels) {
			Labels label1 = modelmapper.map(userlabel, Labels.class);
			userlist.add(label1);
		}

		return userlist;
	}

	@Override
	public Response addnotetolabel(String token, long noteid, long labelid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long userid = TokenGenerator.decodeToken(token);
		Optional<User> user = userrepository.findByUserid(userid);
		Labels label = labelrepository.findByLabelidAndUserid(labelid, userid);
		Notes note = notesrepository.findByIdAndUserid(noteid, userid);
		if (user.isPresent()) {
			label.setModifieddate(LocalDateTime.now());
			label.setNoteid(noteid);
			note.getLabels().add(label);
			labelrepository.save(label);
			notesrepository.save(note);
			response = ResponseStatus.statusinfo(environment.getProperty("status.note.addedtolabel"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));
			return response;
		}

		throw new Exception(environment.getProperty("status.note.notaddedtolabel"),
				Integer.parseInt(environment.getProperty("status.regsuccess.code")));
	}
	@Override
	public Response removenotetolabel(String token, long noteid, long labelid) throws IllegalArgumentException, UnsupportedEncodingException {
		
		Response response;
		long userid = TokenGenerator.decodeToken(token);
		Optional<User> user = userrepository.findByUserid(userid);
		Labels label = labelrepository.findByLabelidAndUserid(labelid, userid);
		Notes note = notesrepository.findByIdAndUserid(noteid, userid);
		if (user.isPresent()) {
			label.setModifieddate(LocalDateTime.now());
			label.setNoteid(noteid);
			note.getLabels().remove(label);
			labelrepository.save(label);
			notesrepository.save(note);
			response = ResponseStatus.statusinfo(environment.getProperty("status.note.removedfromlabel"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));
			return response;
		}

		throw new Exception(environment.getProperty("status.note.notremovedfromlabel"),
				Integer.parseInt(environment.getProperty("status.regsuccess.code")));
	}
	@Override
	public List<Notes> getlistofnotes(String token, long labelid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		long userid = TokenGenerator.decodeToken(token);
		Optional<User> user = userrepository.findByUserid(userid);
		if (!user.isPresent()) {

			throw new Exception("user is not present", 224);

		}
		Labels label = labelrepository.findByLabelidAndUserid(labelid, userid);
		if (label == null) {
			throw new Exception("note is not present", 224);
		}
		List<Notes> note = label.getNotes();
		List<Notes> noteslist = new ArrayList<>();
		for (Notes notes : note) {
			Notes note1 = modelmapper.map(notes, Notes.class);
			noteslist.add(note1);
		}
		return noteslist;
	}
	
	}

	
	
