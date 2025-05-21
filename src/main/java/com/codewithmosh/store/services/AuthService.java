package com.codewithmosh.store.services;

import com.codewithmosh.store.Dtos.LoginRequest;
import com.codewithmosh.store.exceptions.UnauthorizedUserException;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void login(LoginRequest request){

     var user = userRepository.findByEmail(request.getEmail()).orElse(null);
     if(user == null){
         throw new UnauthorizedUserException();
     }

     if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
         throw new UnauthorizedUserException();

     }








    }
}
