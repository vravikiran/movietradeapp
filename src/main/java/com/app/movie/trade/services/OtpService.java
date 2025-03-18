package com.app.movie.trade.services;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.AuthRequest;
import com.app.movie.trade.entities.EmailAuthRequest;
import com.app.movie.trade.entities.UserProfile;
import com.app.movie.trade.security.TwilioConfig;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.mail.MessagingException;

@Service
public class OtpService {
	private static final Integer EXPIRE_MIN = 5;
	@Autowired
	TwilioConfig twilioConfig;
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	Logger logger = LoggerFactory.getLogger(OtpService.class);
	@Autowired
	EmailService emailService;
	private static final String TEST_MOBILE_NO = "9739418251";
	@Autowired
	UserProfileService userProfileService;

	public void generateOtp(String mobileNo) throws ApiException {
		logger.info("started generating otp for given mobile number :: " + mobileNo);
		if (mobileNo != TEST_MOBILE_NO) {
			PhoneNumber to = new PhoneNumber("+91" + mobileNo);
			String otpMessage = "Please find the OTP to login into Movie Trade App: " + getRandomOtp(mobileNo);
			Message.creator(to, twilioConfig.getServiceId(), otpMessage).create();
			logger.info("otp generated successfully for given mobile number :: " + mobileNo + " otp " + otpMessage);
		}
	}

	private String getRandomOtp(String mobileNo) {
		String otp = null;
		if (mobileNo.equals(TEST_MOBILE_NO)) {
			otp = "1111";
		} else {
			otp = String.valueOf(new Random().nextInt(1000, 10000));
		}
		redisTemplate.opsForValue().set(mobileNo, otp);
		redisTemplate.expire(mobileNo, EXPIRE_MIN, TimeUnit.MINUTES);
		return otp;
	}

	public void generateOtpToEmail(String email) throws UnsupportedEncodingException, MessagingException {
		String otp = getRandomOtp(email);
		String content = "Please find the OTP to login into Movie Trade App: " + otp;
		//emailService.sendEmail(email, "OTP to login application", content);
	}

	public boolean validateOtp(AuthRequest authRequest) throws Exception {
		logger.info("validating otp started with  given mobile number and otp :: " + authRequest.toString());
		logger.info("validating otp started with  given mobile number " + authRequest.getMobileNo() + " otp :: "
				+ authRequest.getOtp());
		if (redisTemplate.opsForValue().get(authRequest.getMobileNo()) != null
				&& redisTemplate.opsForValue().get(authRequest.getMobileNo()).equals(authRequest.getOtp())) {
			if(!userProfileService.isUserExistsByMobileNumber(Long.valueOf(authRequest.getMobileNo()))) {
				UserProfile userProfile = new UserProfile();
				userProfile.setMobileno(Long.valueOf(authRequest.getMobileNo()));
				userProfileService.createUserProfile(userProfile, false);
			}
			logger.info("otp validation is successful");
			return true;
		}
		logger.error("invalid otp");
		return false;
	}

	public boolean validateEmailOtp(EmailAuthRequest emailAuthRequest) {
		if (redisTemplate.opsForValue().get(emailAuthRequest.getEmail()) != null
				&& redisTemplate.opsForValue().get(emailAuthRequest.getEmail()).equals(emailAuthRequest.getOtp())) {
			logger.info("otp validation is successful");
			return true;
		}
		logger.error("invalid otp");
		return false;
	}
}
