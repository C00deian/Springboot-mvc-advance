package com.codewithmosh.store.controller;

import com.codewithmosh.store.Dtos.JwtResponseDto;
import com.codewithmosh.store.Dtos.LoginRequest;
import com.codewithmosh.store.Dtos.UserDto;
import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    //    private final AuthService authService;
    private AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ){
//        authService.login(loginRequest);
          authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )

        );

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

         var accessToken = jwtService.generateAccessToken(user);
         var refreshToken = jwtService.generateRefreshToken(user);
         var cookie = new Cookie("refresh_token", refreshToken.toString());
         cookie.setHttpOnly(true);
         cookie.setPath("/auth/refresh");
         cookie.setSecure(true);
         cookie.setMaxAge( Math.toIntExact(jwtConfig.getRefreshTokenExpiration()));//7days
         response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponseDto(accessToken.toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(
            @CookieValue(name = "refresh_token") String refreshToken

    ){
       var jwt = jwtService.parseToken(refreshToken);
       if( jwt == null || jwt.isExpired()) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       }


       var user = userRepository.findById(jwt.getUserId()).orElseThrow();
       var accessToken = jwtService.generateAccessToken(user);

       return ResponseEntity.ok(new JwtResponseDto(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {

//        extracting the current principal
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

//        lookup the User
      var user =   userRepository.findById(userId).orElse(null);
        if(user == null) {
          ResponseEntity.notFound().build();
        }

       var userDto =  userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
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
