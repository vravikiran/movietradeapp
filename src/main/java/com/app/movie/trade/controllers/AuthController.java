package com.app.movie.trade.controllers;

import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.movie.trade.entities.AuthRequest;
import com.app.movie.trade.entities.EmailAuthRequest;
import com.app.movie.trade.exceptions.UnauthorizedUserException;
import com.app.movie.trade.helpers.JwtHelper;
import com.app.movie.trade.services.MessageService;
import com.twilio.exception.ApiException;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private MessageService otpService;
	@Autowired
	JwtHelper jwtHelper;
	Logger logger = LoggerFactory.getLogger(AuthController.class);

	@GetMapping("/generateOtp")
	public ResponseEntity<HttpStatus> generateOtp(@RequestParam String mobileNo) throws ApiException, UnauthorizedUserException {
		logger.info("otp generated for given mobile number:: " + mobileNo);
		otpService.generateOtp(mobileNo);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("/validateOtp")
	public ResponseEntity<String> validateOtp(@RequestBody AuthRequest authRequest)
			throws NoSuchElementException, Exception {
		logger.info("validating otp started with  given mobile number " + authRequest.getMobileNo());
		String token = "";
		boolean isOtpValid = otpService.validateOtp(authRequest);
		logger.info(
				"is otp validation successful for given mobile number :: " + authRequest.getMobileNo() + " " + isOtpValid);
		if (isOtpValid) {
			token = jwtHelper.generateToken(authRequest.getMobileNo());
			logger.info("otp validated successfully");
			return ResponseEntity.ok(token);
		} else {
			logger.info("otp validation failed for given number::" + authRequest.getMobileNo());
			return ResponseEntity.badRequest().body("Invalid otp");
		}
	}

	@GetMapping("/emailOtp")
	public ResponseEntity<HttpStatus> sendOtpToEmail(@RequestParam String email) throws UnsupportedEncodingException, MessagingException {
		otpService.generateOtpToEmail(email);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/validateEmailOtp")
	public ResponseEntity<String> validateEmailOtp(@RequestBody EmailAuthRequest emailAuthRequest) {
		if (otpService.validateEmailOtp(emailAuthRequest)) {
			String token = jwtHelper.generateTokenByEmail(emailAuthRequest.getEmail());
			return ResponseEntity.ok(token);
		}
		return ResponseEntity.badRequest().body("Invalid otp");
	}
}
