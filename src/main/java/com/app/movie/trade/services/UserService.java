package com.app.movie.trade.services;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.Role;
import com.app.movie.trade.entities.User;
import com.app.movie.trade.entities.UserDto;
import com.app.movie.trade.exceptions.DuplicateUserException;
import com.app.movie.trade.exceptions.UserNotFoundException;
import com.app.movie.trade.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	@Autowired
	UserRepository userRepository;
	private static final String MOBILE_NO = "mobile_no";

	public User createUser(UserDto userDto, boolean isAdminUser) throws DuplicateUserException {
		if (!userRepository.existsById(userDto.getMobile_no()) && !userRepository.findByEmail(userDto.getEmail())) {
			User user = convertUserDtoToUser(userDto, isAdminUser);
			return userRepository.save(user);
		} else {
			throw new DuplicateUserException("user already exists with given mobile number or email");
		}
	}

	private User convertUserDtoToUser(UserDto userDto, boolean isAdminUser) {
		User user = new User();
		user.setMobile_no(userDto.getMobile_no());
		user.setEmail(userDto.getEmail());
		user.setFirst_name(userDto.getFirst_name());
		user.setLast_name(userDto.getLast_name());
		Role role = new Role();
		if (isAdminUser) {
			role.setRole_id(2);
			role.setRole_name("ADMIN");
		} else {
			role.setRole_id(3);
			role.setRole_name("INVESTOR");
		}
		user.setRole(role);
		return user;
	}

	public User getUserDetails(long mobileNo) throws UserNotFoundException {
		if (userRepository.existsById(mobileNo)) {
			return userRepository.getReferenceById(mobileNo);
		} else {
			throw new UserNotFoundException("User not found with given mobile number");
		}
	}

	public User updateUserDetails(long mobileNo, Map<String, String> valuesToUpdate)
			throws UserNotFoundException, DuplicateUserException,NoSuchElementException {
		User user = userRepository.getReferenceById(mobileNo);
		User updatedUser = null;
		if (user != null) {
			if (valuesToUpdate.containsKey(MOBILE_NO)
					&& userRepository.existsById(Long.valueOf(valuesToUpdate.get(MOBILE_NO)))) {
				throw new DuplicateUserException("user already exists with given mobile number or email");
			}try {
			user.updateValues(user, valuesToUpdate);
			} catch(NoSuchElementException exception) {
				throw new NoSuchElementException("one or more fields are not valid");
			}
			updatedUser = userRepository.save(user);
		} else {
			throw new UserNotFoundException();
		}
		return updatedUser;
	}

	public User getMobileNoByEmail(String email) throws UserNotFoundException {
		User user = userRepository.findUserDetailsByEmail(email);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("User not found with given email");
		}
	}

	public boolean isUserExists(Long mobileNo) {
		return userRepository.existsById(mobileNo);
	}
}
