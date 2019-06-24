package com.bridgeit.fundoo.user.service;

import java.io.UnsupportedEncodingException;
import org.springframework.stereotype.Service;

import com.bridgeit.fundoo.response.Response;
import com.bridgeit.fundoo.response.ResponseToken;
import com.bridgeit.fundoo.user.dto.LoginDTO;
import com.bridgeit.fundoo.user.dto.PasswordDTO;
import com.bridgeit.fundoo.user.dto.UserDTO;

@Service
public interface IuserService {
	public Response resgister(UserDTO userDto) throws RuntimeException;

	public ResponseToken login(LoginDTO logindto) throws RuntimeException;

	public Response validation(String token) throws IllegalArgumentException, UnsupportedEncodingException;

	public Response forgetpassword(String emailid) throws IllegalArgumentException, UnsupportedEncodingException;

	public Response resetpassword(String token, PasswordDTO password)
			throws IllegalArgumentException, UnsupportedEncodingException;
}
