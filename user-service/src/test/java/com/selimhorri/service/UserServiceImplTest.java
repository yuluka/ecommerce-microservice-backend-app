package com.selimhorri.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.selimhorri.app.domain.Address;
import com.selimhorri.app.domain.Credential;
import com.selimhorri.app.domain.RoleBasedAuthority;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.dto.UserDto;
import com.selimhorri.app.exception.wrapper.UserObjectNotFoundException;
import com.selimhorri.app.helper.UserMappingHelper;
import com.selimhorri.app.repository.UserRepository;
import com.selimhorri.app.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    private User user;
    private Credential credential;
    private Set<Address> addresses;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);

        addresses = new HashSet<>();
        addresses.add(new Address());

        credential = new Credential();
        credential.setCredentialId(1);
        credential.setUsername("bobsmith");
        credential.setPassword("password");
        credential.setRoleBasedAuthority(RoleBasedAuthority.ROLE_ADMIN);
        credential.setIsEnabled(true);
        credential.setIsAccountNonExpired(true);
        credential.setIsAccountNonLocked(true);
        credential.setIsCredentialsNonExpired(true);

        user = new User(1, "Bob", "Smith", "https://bootdey.com/img/Content/avatar/avatar7.png",
                "bobsmith@email.com", "+21622125144", addresses, credential);
    }

    @Test
    void testFindAllReturnsMappedUserDtos() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDto> result = userService.findAll();

        // Assert
        assertEquals(1, result.size());
        UserDto dto = result.get(0);
        assertEquals(user.getUserId(), dto.getUserId());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertNotNull(user.getCredential());

        verify(userRepository).findAll();
    }

    @Test
    void testFindByIdReturnsUserDtoWhenFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDto result = userService.findById(1);

        assertEquals(1, result.getUserId());
        assertEquals("Bob", result.getFirstName());
        verify(userRepository).findById(1);
    }

    @Test
    void testFindByIdThrowsExceptionWhenNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(UserObjectNotFoundException.class, () -> userService.findById(99));

        verify(userRepository).findById(99);
    }

    @Test
    void testFindByUsernameReturnsUserDto() {
        user.setUserId(9);
        when(userRepository.findByCredentialUsername("bobsmith")).thenReturn(Optional.of(user));

        UserDto dto = userService.findByUsername("bobsmith");

        assertEquals(9, dto.getUserId());
        assertEquals("bobsmith", dto.getCredentialDto().getUsername());
        verify(userRepository).findByCredentialUsername("bobsmith");
    }

    @Test
    void testFindByUsernameThrowsExceptionWhenNotFound() {
        when(userRepository.findByCredentialUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserObjectNotFoundException.class, () -> userService.findByUsername("unknown"));

        verify(userRepository).findByCredentialUsername("unknown");
    }

    @Test
    void testSaveUserCallsRepositorySaveAndReturnsDto() {
        UserDto inputDto = Optional.of(user).map(UserMappingHelper::map).orElse(null);
        inputDto.setFirstName("John");

        UserDto result = userService.save(inputDto);

        assertEquals(1, result.getUserId());
        assertEquals("John", result.getFirstName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateByIdCallsFindByIdAndSave() {
        // Arrange
        UserDto oldDto = UserMappingHelper.map(user);
        oldDto.setFirstName("OldName");
        User oldUser = UserMappingHelper.map(oldDto);

        when(userRepository.findById(1)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserDto newDto = UserMappingHelper.map(user);
        newDto.setFirstName("UpdatedName");
        newDto.setLastName("UpdatedLastName");
        newDto.setEmail("updatedemail@example.com");

        UserDto updatedResult = userService.update(1, newDto);

        // Assert
        assertNotNull(updatedResult);
        assertEquals("UpdatedName", updatedResult.getFirstName());
        assertEquals("UpdatedLastName", updatedResult.getLastName());
        assertEquals("updatedemail@example.com", updatedResult.getEmail());

        verify(userRepository).findById(1);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeleteByIdCallsRepositoryDelete() {
        doNothing().when(userRepository).deleteById(1);

        userService.deleteById(1);

        verify(userRepository).deleteById(1);
    }
}
