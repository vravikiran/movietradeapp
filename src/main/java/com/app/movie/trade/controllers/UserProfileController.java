package com.app.movie.trade.controllers;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.app.movie.trade.entities.BankAccountDetails;
import com.app.movie.trade.entities.KycDetails;
import com.app.movie.trade.entities.UserProfile;
import com.app.movie.trade.exceptions.DuplicateUserException;
import com.app.movie.trade.exceptions.InvalidBankDetailsException;
import com.app.movie.trade.exceptions.InvalidKycDetailsException;
import com.app.movie.trade.exceptions.UserNotFoundException;
import com.app.movie.trade.services.FileService;
import com.app.movie.trade.services.UserProfileService;

@Controller
@RequestMapping("/userprofile")
public class UserProfileController {
	Logger logger = LoggerFactory.getLogger(UserProfileController.class);
	@Autowired
	UserProfileService userProfileService;
	@Autowired
	FileService fileService;
	private static final String profileBucket = "mtb-profile-images";
	private static final String kycBucket = "mtb-kyc-images";

	@PostMapping
	public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile)
			throws DuplicateUserException, Exception {
		logger.info("creating user profile with details :: " + userProfile.toString());
		UserProfile createdUserProfile = userProfileService.createUserProfile(userProfile,false);
		logger.info("creation of user profile successful");
		return ResponseEntity.ok(createdUserProfile);
	}
	
	@PostMapping("/admin")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<UserProfile> createAdminUser(@RequestBody UserProfile userProfile) throws DuplicateUserException, Exception {
		userProfileService.createUserProfile(userProfile,true);
		return ResponseEntity.ok(userProfile);
	}

	@GetMapping
	public ResponseEntity<UserProfile> getUserProfile(@RequestParam long mobileno) throws UserNotFoundException {
		logger.info("fetching user profile with mobile number :: " + mobileno);
		UserProfile userProfile = userProfileService.getUserProfile(mobileno);
		logger.info("user profile fetched successfully");
		return ResponseEntity.ok(userProfile);
	}

	@PatchMapping("/update")
	public ResponseEntity<String> updateUserProfile(@RequestBody Map<String, String> valuesToUpdate,
			@RequestParam long mobileNo) throws UserNotFoundException, DuplicateUserException {
		logger.info("Updating user profile with given mobile number :: " + mobileNo);
		userProfileService.updateUserDetails(mobileNo, valuesToUpdate);
		logger.info("user profile updated successfully with given mobile number:: " + mobileNo);
		return ResponseEntity.ok("user profile updated successfully");
	}

	@PostMapping("/verify/bankdetails")
	public ResponseEntity<String> addAccountDetailsToProfile(@RequestParam long mobileno,
			@RequestBody BankAccountDetails bankAccountDetails)
			throws UserNotFoundException, InvalidBankDetailsException {
		logger.info("verification of bank details and adding to user profile with mobile number :: " + mobileno
				+ " started :: " + bankAccountDetails.toString());
		userProfileService.addBankAccDetailsToProfile(mobileno, bankAccountDetails);
		logger.info(
				"verification of bank details completed successfully and added to user profile with mobile number ::"
						+ mobileno);
		return ResponseEntity.ok("account details verified successfully");
	}

	@PostMapping("/verify/kyc")
	public ResponseEntity<String> addKycDetailsToProfile(@RequestParam long mobileno,
			@RequestBody KycDetails kycDetails) throws UserNotFoundException, InvalidKycDetailsException {
		logger.info("kyc verification started and adding kyc details to user profile with mobile number :: " + mobileno
				+ " started :: " + kycDetails.toString());
		userProfileService.verifyAndAddKycDetails(mobileno, kycDetails);
		logger.info(
				"kyc verification completed successfully and added to user profile with mobile number :: " + mobileno);
		return ResponseEntity.ok("kyc details verified successfully");
	}

	@PostMapping(path = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadProfileImage(@RequestPart("photo") MultipartFile file,
			@RequestParam long mobileno)
			throws IOException, UserNotFoundException {
		logger.info("uploading profile image for user profile with mobile number :: " + mobileno);
		String url = fileService.uploadProfileImage(file, mobileno, profileBucket);
		logger.info("profile image uploaded successfully for user profile");
		return ResponseEntity.ok(url);
	}

	@PostMapping(path = "/kycimage/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadKycImage(@RequestPart("photo") MultipartFile file, @RequestParam long mobileno)
			throws IOException, UserNotFoundException {
		logger.info("uploading kyc image for user profile with mobile number :: " + mobileno);
		String url = fileService.uploadKycImage(file, mobileno, kycBucket);
		logger.info("kyc image uploaded successfully for user profile");
		return ResponseEntity.ok(url);
	}

}
