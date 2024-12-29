package com.app.movie.trade.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.movie.trade.entities.Role;
import com.app.movie.trade.repositories.RoleRepository;

@Service
public class RoleService {
	@Autowired
	RoleRepository roleRepository;
	Logger logger = LoggerFactory.getLogger(RoleService.class);

	@PostMapping
	public Role createRole(Role role) {
		logger.info("Started creating a role :: "+role.toString());
		Role createdRole = roleRepository.save(role);
		logger.info("Successfully created a role");
		return createdRole;
	}
}
