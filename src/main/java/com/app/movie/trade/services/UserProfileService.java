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
import com.app.movie.trade.helpers.HashGenerator;
import com.app.movie.trade.helpers.KmsUtil;
import com.app.movie.trade.repositories.UserProfileRepository;

@Service
public class UserProfileService implements UserDetailsService {
	@Autowired
	UserProfileRepository userProfileRepository;
	@Autowired
	KmsUtil kmsUtil;
	@Autowired
	VerificationService verificationService;
	public static String MOBILE_NO = "mobileno";
	public static String EMAIL = "email";
	public static String PAN_NO = "pan_number";
	public static String PANNO_HASH = "panno_hash";
	public static String EMAIL_HASH = "email_hash";
	Logger logger = LoggerFactory.getLogger(UserProfileService.class);

	public UserProfile createUserProfile(UserProfile userProfile, boolean isAdminUser) throws Exception {
		logger.info("Creation of user profile started :: " + userProfile.toString());
		UserProfile createdUserProfile = null;
		if (Long.valueOf(userProfile.getMobileno()) != null) {
			if (isUserExistsByMobileNumber(userProfile.getMobileno())) {
				logger.info("Creation of user profile failed as user already exists with given mobile number");
				throw new DuplicateUserException("user already exists with given mobile number");
			}
		}
		if (userProfile.getPan_number() != null) {
			if (isUserExistsWithPanNo(userProfile.getPan_number())) {
				logger.info("Creation of user profile failed as user already exists with given pan number");
				throw new DuplicateUserException("user already exists with given pan number");
			} else {
				userProfile.setPanno_hash(HashGenerator.generateSHA256Hash(userProfile.getPan_number()));
			}
		}
		if (userProfile.getEmail() != null) {
			if (isUserExistsWithEmail(userProfile.getEmail())) {
				logger.info("Creation of user profile failed as user already exists with given email");
				throw new DuplicateUserException("user already exists with given email");
			} else {
				userProfile.setEmail_hash(HashGenerator.generateSHA256Hash(userProfile.getEmail()));
			}
		}
		userProfile.setCreated_date(LocalDate.now());
		userProfile.setUpdated_date(LocalDate.now());
		Role role = null;
		if (isAdminUser) {
			role = new Role(2, "ADMIN");
		} else {
			role = new Role(3, "INVESTOR");
		}
		userProfile.setRole(role);
		createdUserProfile = userProfileRepository.save(userProfile);
		logger.info("user profile created successfully for given mobile number :: " + userProfile.getMobileno());
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
				bankAccountDetails.setCreated_date(LocalDate.now());
				bankAccountDetails.setUpdated_date(LocalDate.now());
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
			if (valuesToUpdate.containsKey(PAN_NO)) {
				if (valuesToUpdate.get(PAN_NO) != null) {
					if (!isUserExistsWithPanNo(valuesToUpdate.get(PAN_NO))) {
						valuesToUpdate.put(PAN_NO, valuesToUpdate.get(PAN_NO));
						valuesToUpdate.put(PANNO_HASH, HashGenerator.generateSHA256Hash(valuesToUpdate.get(PANNO_HASH)));
					} else {
						logger.info("user already exists with given pan number");
						throw new DuplicateUserException("user already exists with given pan number");
					}
				}
			}
			if (valuesToUpdate.containsKey(EMAIL)) {
				if (valuesToUpdate.get(EMAIL) != null) {
					if (!isUserExistsWithEmail(valuesToUpdate.get(EMAIL))) {
						valuesToUpdate.put(EMAIL, valuesToUpdate.get(EMAIL));
						valuesToUpdate.put(EMAIL_HASH, HashGenerator.generateSHA256Hash(valuesToUpdate.get(EMAIL)));
					} else {
						logger.info("user already exists with given email");
						throw new DuplicateUserException("user already exists with given email");
					}
				}
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
			throw new UserNotFoundException("User profile not found with given mobile number :: " + mobileNo);
		}
		return updatedUserProfile;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserProfile> userProfile = userProfileRepository.findById(Long.valueOf(username));
		return userProfile.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with given mobileNo " + username));
	}

	public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
		Optional<UserProfile> userProfile = userProfileRepository.findByEmail(email);
		return userProfile.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with given email " + email));
	}

	public boolean isUserExistsByMobileNumber(long mobileNo) throws DuplicateUserException {
		return userProfileRepository.existsById(mobileNo);
	}

	private boolean isUserExistsWithEmail(String email) {
		return userProfileRepository.existsByEmail(HashGenerator.generateSHA256Hash(email));
	}

	private boolean isUserExistsWithPanNo(String panNumber) {
		return userProfileRepository.existsByPanNumber(HashGenerator.generateSHA256Hash(panNumber));
	}
}