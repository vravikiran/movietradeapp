package com.app.movie.trade.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.movie.trade.entities.Role;
import com.app.movie.trade.services.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {
	@Autowired
	RoleService roleService;
	Logger logger = LoggerFactory.getLogger(RoleController.class);

	@PostMapping
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<HttpStatus> createRole(@RequestBody Role role) {
		logger.info("Creating a new role with details ::"+role.toString());
		roleService.createRole(role);
		logger.info("Role created successfully");
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
