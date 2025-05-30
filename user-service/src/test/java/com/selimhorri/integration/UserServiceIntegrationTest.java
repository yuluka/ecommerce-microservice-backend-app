package com.selimhorri.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.selimhorri.app.domain.Address;
import com.selimhorri.app.domain.Credential;
import com.selimhorri.app.domain.RoleBasedAuthority;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.dto.UserDto;
import com.selimhorri.app.helper.UserMappingHelper;

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = com.selimhorri.app.UserServiceApplication.class
)
@Testcontainers
@ContextConfiguration(initializers = KubernetesIntegrationTest.Initializer.class)
public class UserServiceIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void createUserAndRetrieve() {
        // Crear usuario
        HashSet<Address> addresses = new HashSet<Address>();
        addresses.add(new Address());

        Credential credential = new Credential();
        credential.setCredentialId(1);
        credential.setUsername("bobsmith");
        credential.setPassword("password");
        credential.setRoleBasedAuthority(RoleBasedAuthority.ROLE_ADMIN);
        credential.setIsEnabled(true);
        credential.setIsAccountNonExpired(true);
        credential.setIsAccountNonLocked(true);
        credential.setIsCredentialsNonExpired(true);

        User user = new User(1, "Bob", "Smith", "https://bootdey.com/img/Content/avatar/avatar7.png", "bobsmith@email.com", "+21622125144", addresses, credential);

        UserDto newUser = UserMappingHelper.map(user);
        
        ResponseEntity<UserDto> createResponse = restTemplate.postForEntity(
            "/api/users", 
            newUser, 
            UserDto.class
        );
        
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody().getUserId());

        int userId = createResponse.getBody().getUserId();

        // Consultar usuario
        ResponseEntity<Object> getResponse = restTemplate.getForEntity(
            "/api/users/" + userId,
            Object.class
        );
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        // assertEquals("bobsmith@email.com", getResponse.getBody().getEmail());
    }
}
