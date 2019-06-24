  package com.bridgeit.fundoo.util;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

@Component
public class TokenGenerator {
	public final static String token = "Mahak";

	
	public static String gennerateToken(long id) {
		Algorithm algorithm = null;
		try {
			algorithm = Algorithm.HMAC256(token);
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String Token = JWT.create().withClaim("userid", id).sign(algorithm);
		return Token;

	}

	public static long decodeToken(String Token) throws IllegalArgumentException, UnsupportedEncodingException  {
		
		Verification verfication = JWT.require(Algorithm.HMAC256(token));
		
		
		JWTVerifier jwtverifier = verfication.build();
		
		DecodedJWT decodejwt = jwtverifier.verify(Token);
		Claim claim = decodejwt.getClaim("userid");
		Long userid = claim.asLong();
		 
		System.out.println(userid);
		return userid;
	}
}

