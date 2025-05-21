package com.codewithmosh.store.controller;

import com.codewithmosh.store.Dtos.ChangePasswordRequest;
import com.codewithmosh.store.Dtos.UserDto;
import com.codewithmosh.store.Dtos.UserRegisterRequest;
import com.codewithmosh.store.Dtos.UserUpdateRequest;
import com.codewithmosh.store.exceptions.EmailAlreadyExistException;
import com.codewithmosh.store.exceptions.UnauthorizedUserException;
import com.codewithmosh.store.exceptions.UserNotFoundException;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(
        @RequestParam(required = false , defaultValue = "" , name = "sortBy")
        String sortBy

    ) {
        return userService.getAllUsers(sortBy);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){

        var userDto = userService.getUser(id);
        return ResponseEntity.ok(userDto);
    }


    @PostMapping()
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegisterRequest request
    ){

      var userDto = userService.registerUser(request);
      return   ResponseEntity.status(HttpStatus.CREATED).body(userDto);

    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id")   Long id,
            @RequestBody UserUpdateRequest request
    ){

        var user = userService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable(name = "id") Long id
    ){
        userService.deleteUser(id);
     return ResponseEntity.noContent().build();

    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Map<String ,String>> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ) {

        userService.resetPassword(id, request);
        return ResponseEntity.ok().body(
                Map.of("message", "Password changed")
        );
    }


    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Map<String , String>> handleEmailNotFoundException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Email already registered")
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String , String>> handleUserNotFoundedException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "User not found")
        );
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<Map<String, String>>  handleUnauthorizedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("error", "Unauthorized")
        );
    }
}

