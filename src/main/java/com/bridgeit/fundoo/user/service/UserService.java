package com.bridgeit.fundoo.user.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bridgeit.fundoo.exception.Exception;
import com.bridgeit.fundoo.response.Response;
import com.bridgeit.fundoo.response.ResponseToken;
import com.bridgeit.fundoo.user.dto.LoginDTO;
import com.bridgeit.fundoo.user.dto.PasswordDTO;
import com.bridgeit.fundoo.user.dto.UserDTO;
import com.bridgeit.fundoo.user.model.Email;
import com.bridgeit.fundoo.user.model.User;
import com.bridgeit.fundoo.user.repository.UserRepository;
import com.bridgeit.fundoo.util.ResponseStatus;
import com.bridgeit.fundoo.util.TokenGenerator;

@PropertySource("classpath:message.properties")
@Service
public class UserService implements IuserService {

	@Autowired
	ModelMapper modelmapper;
	@Autowired
	UserRepository userrepository;
	@Autowired
	Environment environment;
	@Autowired
	PasswordEncoder passwordencorder;
	@Autowired
	MailService mailService;
	@Autowired
	RabbitTemplate rabbitTemplate;

	@Value("${spring.rabbitmq.template.exchange}")
	private String exchange;
	@Value("${spring.rabbitmq.template.routing-key}")
	private String routing;

	private AmazonS3 s3client;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	@Value("${aws.access_key_id}")
	private String accessKey;

	@Value("${aws.secret_access_key}")
	private String secretKey;

	@PostConstruct
	private void initializeAmazon() {

		BasicAWSCredentials creds = new BasicAWSCredentials(this.accessKey, this.secretKey);
		s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
				.withRegion(Regions.AP_SOUTH_1).build();
	}

	@SuppressWarnings("static-access")
	@Override
	public Response resgister(UserDTO userDto) throws RuntimeException {
		Email email = new Email();
		Response response = null;
		Optional<User> use = userrepository.findByEmailid(userDto.getEmailid());
		if (use.isPresent()) {
			throw new Exception(environment.getProperty("status.register.message"),
					Integer.parseInt(environment.getProperty("status.register.errorcode")));
		}
		User user = modelmapper.map(userDto, User.class);
		String password = passwordencorder.encode(user.getPassword());
		user.setRegisteredDate(LocalDateTime.now());
		user.setPassword(password);
		user.setIsverify(false);
		User status = userrepository.save(user);
		System.out.println(status + "status of user");
		if (status == null) {
			throw new Exception(environment.getProperty("status.register.nosuccess"),
					Integer.parseInt(environment.getProperty("status.nosuccess.errorcode")));
		} else {
			try {
				email.setFrom("mehakbansal909@gmail.com");
				email.setTo(userDto.getEmailid());
				email.setSubject("Email Verification");
				email.setBody(linkcreator("http://localhost:8080/emailvalidation/", user.getUserid()));
//				rabbitTemplate.convertAndSend("exchange", "trim", email);
				//mailService.send(email);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			}

			response = ResponseStatus.statusinfo(environment.getProperty("status.register.success"),
					Integer.parseInt(environment.getProperty("status.regsuccess.code")));

			return response;
		}
	}

	public String linkcreator(String link, long id) {
		return link + TokenGenerator.gennerateToken(id);
	}

	@Override
	public ResponseToken login(LoginDTO logindto) throws RuntimeException {
		ResponseToken response = null;
		@SuppressWarnings("unused")
		User user = modelmapper.map(logindto, User.class);
		Optional<User> use = userrepository.findByEmailid(logindto.getEmailid());
		if (use.isPresent()) {
			boolean status = passwordencorder.matches(logindto.getPassword(), use.get().getPassword());
			if (status == true) {
				String tokengenerate = TokenGenerator.gennerateToken(use.get().getUserid());
				System.out.println(tokengenerate);
				response = ResponseStatus.statusinfoo(environment.getProperty("status.login.success"),
						Integer.parseInt(environment.getProperty("status.success.code")), tokengenerate);
				return response;

			} else {
				throw new Exception(environment.getProperty("status.login.nosuccess"),
						Integer.parseInt(environment.getProperty("status.nosuccess.errocode")));

			}
		}
		throw new Exception(environment.getProperty("status.register.nosuccess"),
				Integer.parseInt(environment.getProperty("status.nosuccess.errorcode")));

	}

	@Override
	public Response validation(String token) throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = null;

		long id = TokenGenerator.decodeToken(token);
		Optional<User> use = userrepository.findById((long) id).map(this::verify);
		if (use.isPresent()) {
			response = ResponseStatus.statusinfo(environment.getProperty("status.email.verified"),
					Integer.parseInt(environment.getProperty("status.email.verified.code")));

		} else {
			throw new Exception(environment.getProperty("status.email.notverified"),
					Integer.parseInt(environment.getProperty("status.email.notverified.code")));
		}
		return response;
	}

	private User verify(User user) {
		user.setIsverify(true);
		user.setModifiedDate(LocalDateTime.now());
		return userrepository.save(user);

	}

	@Override
	public Response forgetpassword(String emailid)
			throws RuntimeException, IllegalArgumentException, UnsupportedEncodingException {
		Email email = new Email();
		Response response;
		Optional<User> use = userrepository.findByEmailid(emailid);

		if (use.isPresent()) {
			email.setFrom("mehakbansal909@gmail.com");
			email.setTo(emailid);
			email.setSubject("Forget Password");
			email.setBody(linkcreator("http://localhost:8080/resetpassword/", use.get().getUserid()));
			MailService.send(email);
			response = ResponseStatus.statusinfo(environment.getProperty("status.success.fpasswor"),
					Integer.parseInt(environment.getProperty("status.success.fpassword.code")));
			return response;
		}

		throw new Exception(environment.getProperty("status.failure.fpassword"),
				Integer.parseInt(environment.getProperty("status.failure.fpassword.code")));

	}

	@Override
	public Response resetpassword(String token, PasswordDTO password)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Response response = null;
		long id = TokenGenerator.decodeToken(token);
		Optional<User> use = userrepository.findById((long) id);
		if (use.isPresent()) {
			User user = modelmapper.map(password, User.class);
			if (password.getNewpassword().equals(password.getConfirmpassword())) {
				user.setPassword(password.getNewpassword());
			}
			User status = userrepository.save(user);

			if (status == null) {
				throw new Exception(environment.getProperty("status.failure.resetpassword"),
						Integer.parseInt(environment.getProperty("status.failure.resetpassword.code")));
			}
		}
		response = ResponseStatus.statusinfo(environment.getProperty("status.resetPassword.success"),
				Integer.parseInt(environment.getProperty("status.resetPassword.success.code")));
		return response;

	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public String uploadFile(MultipartFile multipartFile) throws IOException {

		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = "/" + bucketName + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	public String deleteFileFromS3Bucket(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
		return "Successfully deleted";
	}

}
