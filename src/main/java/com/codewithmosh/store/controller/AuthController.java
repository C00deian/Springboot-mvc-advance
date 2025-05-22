package com.codewithmosh.store.controller;

import com.codewithmosh.store.Dtos.LoginRequest;
import com.codewithmosh.store.exceptions.UnauthorizedUserException;
import com.codewithmosh.store.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

//    private final AuthService authService;
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest loginRequest
    ){
//        authService.login(loginRequest);
          authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )

        );
        return ResponseEntity.ok().build();
    }


//    @ExceptionHandler(UnauthorizedUserException.class)
//    public ResponseEntity<Map<String, String>> handleUnauthorizedUserException(){
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
//                Map.of("message", "Unauthorized User")
//        );
//
//    }


//   mosh
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
