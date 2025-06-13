package com.selimhorri.app.resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selimhorri.app.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = {"/api/experimental/users"})
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "feature.experimental-users", havingValue = "true")
public class UserResourceExperimental {
	
	private final UserService userService;
	
	@GetMapping
	public ResponseEntity<String> experimental() {
		log.info("*** Experimental feature *");
		return ResponseEntity.ok("This is an experimental feature for user management.");
	}
}
