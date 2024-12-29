package com.app.movie.trade.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.movie.trade.entities.User;
import com.app.movie.trade.entities.UserDto;
import com.app.movie.trade.exceptions.DuplicateUserException;
import com.app.movie.trade.exceptions.UserNotFoundException;
import com.app.movie.trade.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	@PostMapping
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws DuplicateUserException {
		userService.createUser(userDto,false);
		return ResponseEntity.ok(userDto);
	}

	@GetMapping(value = "/details", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<User> getUserDetails(@RequestParam String mobileNo)
			throws UserNotFoundException, NumberFormatException {
		User user = userService.getUserDetails(Long.valueOf(mobileNo));
		return ResponseEntity.ok(user);
	}

	@PatchMapping("/update")
	public ResponseEntity<String> updateUser(@RequestBody Map<String, String> valuesToUpdate,
			@RequestParam long mobileNo) throws UserNotFoundException, DuplicateUserException {
		userService.updateUserDetails(mobileNo, valuesToUpdate);
		return ResponseEntity.ok("user details updated successfully");
	}

	@PostMapping("/admin")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<UserDto> createAdminUser(@RequestBody UserDto userDto) throws DuplicateUserException {
		userService.createUser(userDto,true);
		return ResponseEntity.ok(userDto);
	}
}
