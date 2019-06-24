package com.bridgeit.fundoo.user.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgeit.fundoo.response.Response;
import com.bridgeit.fundoo.response.ResponseToken;
import com.bridgeit.fundoo.user.dto.LoginDTO;
import com.bridgeit.fundoo.user.dto.PasswordDTO;
import com.bridgeit.fundoo.user.dto.UserDTO;
import com.bridgeit.fundoo.user.service.UserService;

@RestController
public class UserController {
	@Autowired
	UserService userservice;

	@PostMapping("/registration")
	public ResponseEntity<Response> register(@RequestBody @Valid UserDTO userdto) {

		Response response = userservice.resgister(userdto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/login")
	public ResponseEntity<ResponseToken> login(@RequestBody @Valid LoginDTO logindto) {

		ResponseToken response = userservice.login(logindto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/forget")
	public ResponseEntity<Response> forgetpassword(@RequestParam String emailid)
			throws IllegalArgumentException, UnsupportedEncodingException, RuntimeException {

		Response response = userservice.forgetpassword(emailid);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/emailvalidation/{token}")
	public ResponseEntity<Response> validation(@PathVariable String token)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = userservice.validation(token);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

	}

	@PutMapping("/resetpassword/{token}")
	public ResponseEntity<Response> resetpassword(@PathVariable String token, @RequestBody PasswordDTO password)
			throws IllegalArgumentException, UnsupportedEncodingException, RuntimeException {
		Response response = userservice.resetpassword(token, password);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

	}
	
	@PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) throws IOException {
        return this.userservice.uploadFile(file);
        
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.userservice.deleteFileFromS3Bucket(fileUrl);
    }
	
}
