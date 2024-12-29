package com.app.movie.trade.services;

import java.time.LocalDate;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.BankAccountDetails;
import com.app.movie.trade.entities.KycDetails;
import com.app.movie.trade.entities.Role;
import com.app.movie.trade.entities.UserInfoDetails;
import com.app.movie.trade.entities.UserProfile;
import com.app.movie.trade.exceptions.DuplicateUserException;
import com.app.movie.trade.exceptions.InvalidBankDetailsException;
import com.app.movie.trade.exceptions.InvalidKycDetailsException;
import com.app.movie.trade.exceptions.UserNotFoundException;
import com.app.movie.trade.repositories.BankAccountDetailsRepository;
import com.app.movie.trade.repositories.UserProfileRepository;

@Service
public class UserProfileService implements UserDetailsService {
	@Autowired
	UserProfileRepository userProfileRepository;
	BankAccountDetailsRepository bankAccountDetailsRepository;
	@Autowired
	VerificationService verificationService;
	public static String MOBILE_NO = "mobileno";
	public static String EMAIL = "email";
	public static String PAN_NO = "pan_number";
	Logger logger = LoggerFactory.getLogger(UserProfileService.class);

	public UserProfile createUserProfile(UserProfile userProfile) throws Exception {
		logger.info("Creation of user profile started :: " + userProfile.toString());
		UserProfile createdUserProfile = null;
		if (isUserExistsWithEmail(userProfile.getEmail()) || isUserExistsWithPanNo(userProfile.getPan_number())
				|| isUserExistsByMobileNumber(userProfile.getMobileno())) {
			logger.info(
					"Creation of user profile failed as user already exists with given mobile number or email or pan number");
			throw new DuplicateUserException("user already exists with given mobile number or email or pan number");
		} else {
			if (verificationService.verifyPanno(userProfile.getPan_number())) {
				userProfile.setCreated_date(LocalDate.now());
				userProfile.setUpdated_date(LocalDate.now());
				Role role = new Role(3, "INVESTOR");
				userProfile.setRole(role);
				createdUserProfile = userProfileRepository.save(userProfile);
				logger.info(
						"user profile created successfully for given mobile number :: " + userProfile.getMobileno());
			} else {
				logger.info("user profile creation failed due to invalid pan number");
				throw new Exception("invalid pan number");
			}
		}
		return createdUserProfile;
	}

	public UserProfile getUserProfile(long mobileno) throws UserNotFoundException {
		logger.info("Fetching user profile for given mobile number :: " + mobileno);
		if (userProfileRepository.existsById(mobileno)) {
			logger.info("successfully fetched user profile with given mobile number :: " + mobileno);
			return userProfileRepository.getReferenceById(mobileno);
		} else {
			logger.info("User profile not found with given mobile number :: " + mobileno);
			throw new UserNotFoundException("User not found with given mobile number");
		}
	}

	public void addBankAccDetailsToProfile(long mobileno, BankAccountDetails bankAccountDetails)
			throws UserNotFoundException, InvalidBankDetailsException {
		logger.info("verifying bank account details and adding it to user profile with mobile number :: " + mobileno
				+ " account details :: " + bankAccountDetails.toString());
		UserProfile userProfile = null;
		if (userProfileRepository.existsById(mobileno)) {
			userProfile = userProfileRepository.getReferenceById(mobileno);
			if (verificationService.verifyBankDetails(bankAccountDetails)) {
				userProfile.setBankAccountDetails(bankAccountDetails);
				userProfile.setBank_details_verified(true);
				userProfile.setUpdated_date(LocalDate.now());
				userProfileRepository.save(userProfile);
				logger.info(
						"bank account details are verified successfully and added it to user profile with mobile number :: "
								+ mobileno);
			} else {
				logger.info("given bank account details are invalid");
				throw new InvalidBankDetailsException("verification failed with given bank account details");
			}
		} else {
			logger.info("User profile not found with given mobile number :: " + mobileno);
			throw new UserNotFoundException("User with given mobile number doesn't exists");
		}
	}

	public void verifyAndAddKycDetails(long mobileno, KycDetails kycDetails)
			throws UserNotFoundException, InvalidKycDetailsException {
		logger.info("verifying kyc details and adding it to user profile with given mobile number :: " + mobileno
				+ " kyc details :: " + kycDetails.toString());
		UserProfile userProfile = null;
		if (userProfileRepository.existsById(mobileno)) {
			userProfile = userProfileRepository.getReferenceById(mobileno);
			if (verificationService.verifyKycDetails(kycDetails)) {
				userProfile.setKycDetails(kycDetails);
				userProfile.setAadhar_verified(true);
				userProfile.setUpdated_date(LocalDate.now());
				userProfileRepository.save(userProfile);
				logger.info("kyc details are verified successfully and added it to user profile with mobile number :: "
						+ mobileno);
			} else {
				logger.info("kyc verification failed for user profile with mobile number :: " + mobileno);
				throw new InvalidKycDetailsException("verification failed with given kyc details");
			}
		} else {
			logger.info("User profile not found with given mobile number :: " + mobileno);
			throw new UserNotFoundException("User with given mobile number doesn't exists");
		}
	}

	public UserProfile updateUserDetails(long mobileNo, Map<String, String> valuesToUpdate)
			throws UserNotFoundException, DuplicateUserException, NoSuchElementException {
		UserProfile userProfile = null;
		UserProfile updatedUserProfile = null;
		if (userProfileRepository.existsById(mobileNo)) {
			userProfile = userProfileRepository.getReferenceById(mobileNo);
			if ((valuesToUpdate.containsKey(PAN_NO) && isUserExistsWithPanNo(valuesToUpdate.get(PAN_NO)))
					|| (valuesToUpdate.containsKey(EMAIL) && isUserExistsWithEmail(valuesToUpdate.get(EMAIL)))) {
				logger.info("user already exists with given email or pan number");
				throw new DuplicateUserException("user already exists with email or pan number");
			}
			try {
				userProfile.updateValues(userProfile, valuesToUpdate);
				userProfile.setUpdated_date(LocalDate.now());
			} catch (NoSuchElementException exception) {
				throw new NoSuchElementException("one or more fields are not valid");
			}
			updatedUserProfile = userProfileRepository.save(userProfile);
		} else {
			logger.info("User profile not found with given mobile number :: " + mobileNo);
			throw new UserNotFoundException();
		}
		return updatedUserProfile;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserProfile> userProfile = userProfileRepository.findById(Long.valueOf(username));
		return userProfile.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with given mobileNo " + username));
	}

	private boolean isUserExistsByMobileNumber(long mobileNo) {
		return userProfileRepository.existsById(mobileNo);
	}

	private boolean isUserExistsWithEmail(String email) {
		return userProfileRepository.existsByEmail(email);
	}

	private boolean isUserExistsWithPanNo(String panNumber) {
		return userProfileRepository.existsByPanNumber(panNumber);
	}
}