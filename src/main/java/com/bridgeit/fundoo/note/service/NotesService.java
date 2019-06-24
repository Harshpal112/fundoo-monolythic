package com.bridgeit.fundoo.note.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgeit.fundoo.configuration.RedisConfiguration;
import com.bridgeit.fundoo.exception.Exception;
import com.bridgeit.fundoo.label.model.Labels;
import com.bridgeit.fundoo.label.repository.LabelRepo;
import com.bridgeit.fundoo.note.dTO.NotesDto;
import com.bridgeit.fundoo.note.model.Notes;
import com.bridgeit.fundoo.note.repository.NotesRepository;
import com.bridgeit.fundoo.response.Response;
import com.bridgeit.fundoo.user.model.Email;
import com.bridgeit.fundoo.user.model.User;
import com.bridgeit.fundoo.user.repository.UserRepository;
import com.bridgeit.fundoo.user.service.MailService;
import com.bridgeit.fundoo.util.ResponseStatus;
import com.bridgeit.fundoo.util.TokenGenerator;

@PropertySource("classpath:message.properties")
@Service
public class NotesService implements InotesService {
	@Autowired
	NotesRepository notesrepository;
	@Autowired
	Environment environment;
	@Autowired
	ModelMapper modelmapper;
	@Autowired
	UserRepository userrepository;
	@Autowired
	LabelRepo labelrepository;
	@Autowired
	MailService mailService;
	@Autowired
	ElasticSearch elasticsearch;
	@Autowired
	RedisConfiguration redisconfig;

//	@SuppressWarnings("rawtypes")
//	
	@Autowired
	RedisService<Notes> redisservice;

//	@SuppressWarnings("unchecked")
	@Override
	public Response create(NotesDto notesdto, String token)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = null;
		long userid = TokenGenerator.decodeToken(token);

		if (notesdto.getTitle() == null && notesdto.getDiscription() == null) {
			throw new Exception("title and discription can't be empty", 10);
		}
		Optional<User> user = userrepository.findByUserid(userid);

		if (user == null) {
			throw new Exception("user not found", 11);
		}
		Notes note = modelmapper.map(notesdto, Notes.class);
		note.setUserid(userid);
		note.setModifiedDate(LocalDateTime.now());
		note.setRegisteredDate(LocalDateTime.now());
		user.get().getNotes().add(note);
//		rabbitTemplate.convertAndSend(exchange,routing,note);
		
		notesrepository.save(note);
		redisservice.putMap("note", note.getId(), note);
		
		elasticsearch.create(note);
		@SuppressWarnings("unused")
		User status = userrepository.save(user.get());
		response = ResponseStatus.statusinfo(environment.getProperty("status.notes.createdSuccessfull"),

				Integer.parseInt(environment.getProperty("status.regsuccess.code")));
		return response;
	}

//	@SuppressWarnings("unchecked")
	@Override
	public Response update(NotesDto noteDto, String token, long id) throws UnsupportedEncodingException  {

		Response response = null;
		try {
			long userid = TokenGenerator.decodeToken(token);
			Notes notes = notesrepository.findByIdAndUserid(id, userid);
			System.out.println(notes.toString());
			notes.setTitle(noteDto.getTitle());
			notes.setDiscription(noteDto.getDiscription());
			
			Notes status = notesrepository.save(notes);
//			redisservice.putMap("note", id, notes);
			
			try {
				elasticsearch.update(notes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (status == null) {
				throw new Exception(environment.getProperty("status.note.failedUpdate"),
						Integer.parseInt(environment.getProperty("status.nosuccess.errorcode")));

			} else {
				response = ResponseStatus.statusinfo(environment.getProperty("status.note.updated"),
						Integer.parseInt(environment.getProperty("status.regsuccess.code")));
				return response;
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		return response;
	}
	@Override
	public Notes getsinglenotefromredis1(long id) {
		Notes note = redisservice.getMapAsSingleEntry("notes",id);
	System.out.println("note is "+note);
	return note;
}
	@Override
	public Map<Object, Notes> getlistofnotefromredis() {
		@SuppressWarnings("unchecked")
		Map<Object, Notes> map = redisservice.getMapAsAll("notes");
		System.out.println("All radis note " + map);
		return map;

	}

	@Override
	public Response isTrash(long id, String token) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long Token = TokenGenerator.decodeToken(token);
		Notes notes = notesrepository.findByIdAndUserid(id, Token);
		System.out.println(notes);
		if (notes.isTrash() == true) {
			notes.setTrash(false);
			notes.setArchive(false);
			notes.setPin(false);
			notes.setModifiedDate(LocalDateTime.now());
			notesrepository.save(notes);
			response = ResponseStatus.statusinfo(environment.getProperty("status.note.untrashed"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));

			return response;
		} else {
			notes.setTrash(true);
			notes.setArchive(false);
			notes.setPin(false);
			notes.setModifiedDate(LocalDateTime.now());
			notesrepository.save(notes);
			response = ResponseStatus.statusinfo(environment.getProperty("status.note.trashed"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));

			return response;
		}

	}

	@Override
	public Response delete(long id, String token) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long Token = TokenGenerator.decodeToken(token);
		Notes notes = notesrepository.findByIdAndUserid(id, Token);

		if (notes.isTrash() == true) {
			
			notesrepository.delete(notes);
//			redisservice.deleteMap("note", id);
			try {
				elasticsearch.delete(id);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			response = ResponseStatus.statusinfo(environment.getProperty("status.note.deleted"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));
			return response;

		} else {

			throw new Exception(environment.getProperty("status.note.notdeleted"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));

		}

	}

	@Override
	public Response isPin(long id, String token) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long Token = TokenGenerator.decodeToken(token);
		Notes notes = notesrepository.findByIdAndUserid(id, Token);
		System.out.println(notes);
		if (notes.isPin() == true) {
			notes.setTrash(false);
			notes.setArchive(false);
			notes.setPin(false);
			notes.setModifiedDate(LocalDateTime.now());
			notesrepository.save(notes);
			response = ResponseStatus.statusinfo(environment.getProperty("status.note.unpinned"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));

			return response;
		} else {
			notes.setTrash(false);
			notes.setArchive(false);
			notes.setPin(true);
			notes.setModifiedDate(LocalDateTime.now());
			notesrepository.save(notes);
			response = ResponseStatus.statusinfo(environment.getProperty("status.note.pinned"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));

			return response;

		}
	}

	@Override
	public Response isArchive(long id, String token) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long Token = TokenGenerator.decodeToken(token);
		Notes notes = notesrepository.findByIdAndUserid(id, Token);
		System.out.println(notes);
		if (notes.isArchive() == true) {
			notes.setTrash(false);
			notes.setArchive(false);
			notes.setPin(false);
			notes.setModifiedDate(LocalDateTime.now());
			notesrepository.save(notes);
			response = ResponseStatus.statusinfo(environment.getProperty("status.note.unarchieved"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));

			return response;
		} else {
			notes.setTrash(false);
			notes.setArchive(true);
			notes.setPin(false);
			notes.setModifiedDate(LocalDateTime.now());
			notesrepository.save(notes);
			response = ResponseStatus.statusinfo(environment.getProperty("status.note.archieved"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));

			return response;
		}

	}

	@Override
	public List<Notes> getnotes(String token) throws IllegalArgumentException, UnsupportedEncodingException {
		long userid = TokenGenerator.decodeToken(token);
		List<Notes> notes = notesrepository.findByUserid(userid);
		List<Notes> listnortes = new ArrayList<>();
		for (Notes usernotes : notes) {
			Notes notes1 = modelmapper.map(usernotes, Notes.class);
			listnortes.add(notes1);
		}
		return listnortes;
	}

	@Override
	public Response addlabeltonote(String token, long noteid, long labelid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long userid = TokenGenerator.decodeToken(token);
		Optional<User> user = userrepository.findByUserid(userid);
		Labels label = labelrepository.findByLabelidAndUserid(labelid, userid);
		Notes note = notesrepository.findByIdAndUserid(noteid, userid);
		if (user.isPresent()) {
			note.setModifiedDate(LocalDateTime.now());
			note.setLabelid(labelid);
			label.getNotes().add(note);
			notesrepository.save(note);
			labelrepository.save(label);
			response = ResponseStatus.statusinfo(environment.getProperty("status.label.addedtonote"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));
			return response;
		}

		throw new Exception(environment.getProperty("status.label.notaddedtonote"),
				Integer.parseInt(environment.getProperty("status.regsuccess.code")));
	}

	@Override
	public Response removelabeltonote(String token, long noteid, long labelid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long userid = TokenGenerator.decodeToken(token);
		Optional<User> user = userrepository.findByUserid(userid);
		Labels label = labelrepository.findByLabelidAndUserid(labelid, userid);
		Notes note = notesrepository.findByIdAndUserid(noteid, userid);
		if (user.isPresent()) {
			note.setModifiedDate(LocalDateTime.now());
			note.setLabelid(labelid);
			label.getNotes().remove(note);
			notesrepository.save(note);
			labelrepository.save(label);
			response = ResponseStatus.statusinfo(environment.getProperty("status.label.removedfromnote"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));
			return response;
		}

		throw new Exception(environment.getProperty("status.label.notremovedfromnote"),
				Integer.parseInt(environment.getProperty("status.regsuccess.code")));

	}

	@Override
	public List<Labels> getlistoflabel(String token, long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		long userid = TokenGenerator.decodeToken(token);
		Optional<User> user = userrepository.findByUserid(userid);
		if (!user.isPresent()) {

			throw new Exception("user is not present", 224);

		}
		Notes note = notesrepository.findByIdAndUserid(noteid, userid);
		if (note == null) {
			throw new Exception("note is not present", 224);
		}
		List<Labels> label = note.getLabels();
		List<Labels> labelslist = new ArrayList<>();
		for (Labels label1 : label) {
			Labels labels1 = modelmapper.map(label1, Labels.class);
			labelslist.add(labels1);
		}
		System.out.println(labelslist);
		return labelslist;
	}

	@Override
	public Response setreminder(String token, String time, long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response;
		long id = TokenGenerator.decodeToken(token);
		Notes note = notesrepository.findByIdAndUserid(noteid, id);
		if (note != null) {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			LocalDateTime timenow = LocalDateTime.now();

			LocalDateTime localdatetime1 = LocalDateTime.parse(time, format);

			if (timenow.compareTo(localdatetime1) < 0) {
				note.setReminder(localdatetime1);
				notesrepository.save(note);
				response = ResponseStatus.statusinfo(environment.getProperty("status.remainder.success"),
						Integer.parseInt(environment.getProperty("status.success.code")));
				return response;
			}
		}
		throw new Exception("remainder is not set", 550);
	}

	@Override
	public Response removereminder(String token, long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long id = TokenGenerator.decodeToken(token);
		Notes note = notesrepository.findByIdAndUserid(noteid, id);
		if (note == null) {
			throw new Exception("there is no note of this userid", 203);
		}
		note.setReminder(null);
		Notes status = notesrepository.save(note);
		if (status != null) {
			response = ResponseStatus.statusinfo(environment.getProperty("status.remainderdelete.success"),
					Integer.parseInt(environment.getProperty("status.success.code")));
			return response;
		}
		throw new Exception("remainder is not set", 550);
	}

	@Override
	public Response updatereminder(String token, String time, long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Response response;
		long id = TokenGenerator.decodeToken(token);
		Notes note = notesrepository.findByIdAndUserid(noteid, id);
		if (note != null) {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			LocalDateTime timenow = LocalDateTime.now();

			LocalDateTime localdatetime1 = LocalDateTime.parse(time, format);

			if (timenow.compareTo(localdatetime1) < 0) {
				note.setReminder(localdatetime1);
				notesrepository.save(note);
				response = ResponseStatus.statusinfo(environment.getProperty("status.remainderupdate.success"),
						Integer.parseInt(environment.getProperty("status.success.code")));
				return response;
			}
		}
		throw new Exception("remainder is not set", 550);
	}

	@Override
	public Response addcolaborator(String token, long noteid, String emailid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Email email = new Email();
		Response response = null;
		long id = TokenGenerator.decodeToken(token);
		Optional<User> user = userrepository.findByUserid(id);
		if (!user.isPresent()) {

			throw new Exception("user is not present", 224);
		}

		Notes note = notesrepository.findByIdAndUserid(noteid, id);
		if (note == null) {
			throw new Exception("note is not present", 46);
		} else {
			User usercollaborator1 = userrepository.findByEmailid(emailid).get();
			if (note.getUsercollaborater().contains(usercollaborator1)) {
				throw new Exception("user already collaborated", 46);
			} else {
				note.getUsercollaborater().add(usercollaborator1);
				usercollaborator1.getNotecollaborater().add(note);
				notesrepository.save(note);
				userrepository.save(usercollaborator1);
				email.setTo(emailid);
				email.setFrom("mehakbansal909@gmail.com");
				email.setSubject("collabration successfull");
				email.setBody("note has be collabrated with this email");
				MailService.send(email);

				response = ResponseStatus.statusinfo(environment.getProperty("status.collaborator.success"),
						Integer.parseInt(environment.getProperty("status.success.code")));
				return response;

			}

		}

	}

	@Override
	public Response removecolaborator(String token, long noteid, String emailid)
			throws IllegalArgumentException, UnsupportedEncodingException {

		Response response = null;
		long id = TokenGenerator.decodeToken(token);
		Optional<User> user = userrepository.findByUserid(id);
		if (!user.isPresent()) {

			throw new Exception("user is not present", 224);
		}

		Notes note = notesrepository.findByIdAndUserid(noteid, id);
		if (note == null) {
			throw new Exception("note is not present", 46);
		} else {
			User usercollaborator1 = userrepository.findByEmailid(emailid).get();
			if (note.getUsercollaborater().contains(usercollaborator1)) {
				note.getUsercollaborater().remove(usercollaborator1);
				usercollaborator1.getNotecollaborater().remove(note);
				notesrepository.save(note);
				userrepository.save(usercollaborator1);

				response = ResponseStatus.statusinfo(environment.getProperty("status.delete.success.collaborator"),
						Integer.parseInt(environment.getProperty("status.success.code")));
				return response;

			}

		}
		return response;
	}

	@Override
	public List<User> getlistofcollaborator(String token, long noteid)
			throws IllegalArgumentException, UnsupportedEncodingException {
		long userid = TokenGenerator.decodeToken(token);
		Optional<User> user = userrepository.findByUserid(userid);
		System.out.println("My note Id=" + noteid);
		if (!user.isPresent()) {
			throw new Exception("user is not present", 224);
		}
		Notes note = notesrepository.findByIdAndUserid(noteid, userid);
		if (note == null) {
			throw new Exception("note is not present", 214);
		}
		List<User> note1 = note.getUsercollaborater();
		System.out.println("________________" + note1);
		List<User> collaboratorlist = new ArrayList<>();
		for (User note2 : note1) {
//			Notes note3 = modelmapper.map(note2, Notes.class);
			collaboratorlist.add(note2);
		}
		System.out.println(collaboratorlist);
		return collaboratorlist;
	}

	@Override
	public List<Notes> getlistofarchieve(String token) throws IllegalArgumentException, UnsupportedEncodingException {
		long userid = TokenGenerator.decodeToken(token);
		List<Notes> note = notesrepository.findByUserid(userid);
		List<Notes> note121 = new ArrayList<>();
		for (Notes note2 : note) {
			Notes note3 = modelmapper.map(note2, Notes.class);
			if (note3.isArchive() == true) {
				note121.add(note3);
			}

		}
		return note121;

	}

	@Override
	public List<Notes> getlistoftrash(String token) throws IllegalArgumentException, UnsupportedEncodingException {
		long userid = TokenGenerator.decodeToken(token);
		List<Notes> note = notesrepository.findByUserid(userid);
		List<Notes> note121 = new ArrayList<>();
		for (Notes note2 : note) {
			Notes note3 = modelmapper.map(note2, Notes.class);
			if (note3.isTrash()) {
				note121.add(note3);
			}

		}
		return note121;

	}

	@Override
	public List<Notes> getlistofpin(String token) throws IllegalArgumentException, UnsupportedEncodingException {
		long userid = TokenGenerator.decodeToken(token);
		List<Notes> note = notesrepository.findByUserid(userid);
		List<Notes> note121 = new ArrayList<>();
		for (Notes note2 : note) {
			Notes note3 = modelmapper.map(note2, Notes.class);
			if (note3.isPin()) {
				note121.add(note3);
			}

		}
		return note121;
	}

	@Override
	public Response addcolor(String token, long noteid, String color)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = null;
		long id = TokenGenerator.decodeToken(token);
		Optional<User> user = userrepository.findByUserid(id);
		if (!user.isPresent()) {

			throw new Exception("user is not present", 224);
		}

		Notes note = notesrepository.findByIdAndUserid(noteid, id);
		if (note == null) {
			throw new Exception("note is not present", 46);
		} else {
			note.setColor(color);
			Notes status = notesrepository.save(note);
			if (status == null) {
				throw new Exception(environment.getProperty("status.setColor.unsuccessful"),
						Integer.parseInt(environment.getProperty("status.success.code")));
			}
		}

		response = ResponseStatus.statusinfo(environment.getProperty("status.setColor.success"),
				Integer.parseInt(environment.getProperty("status.success.code")));
		return response;
	}

	



}
