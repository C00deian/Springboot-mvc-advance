package com.codewithmosh.store.controller;

import com.codewithmosh.store.Dtos.ChangePasswordRequest;
import com.codewithmosh.store.Dtos.UserDto;
import com.codewithmosh.store.Dtos.UserRegisterRequest;
import com.codewithmosh.store.Dtos.UserUpdateRequest;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {


    private final UserRepository userRepository;
    private final UserMapper userMapper;

@GetMapping
public List<UserDto> getAllUsers(
        @RequestParam(required = false , defaultValue = "" , name = "sortBy") String sortBy

) {

    if(!Set.of("name" , "email").contains(sortBy)){
        sortBy = "name";
    };
     return userRepository.findAll(Sort.by(sortBy))
              .stream()
              .map(userMapper::toUserDto)
              .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
    var user = userRepository.findById(id).orElse(null);

    if(user == null){
        return ResponseEntity.notFound().build();
    }
    var userDto = userMapper.toUserDto(user);
    return ResponseEntity.ok(userDto);
    }


    @PostMapping()
    public ResponseEntity<UserDto> createUser(
            @RequestBody UserRegisterRequest request,
            UriComponentsBuilder uriBuilder
    ){

        //take request to the user
        var user = userMapper.toEntity(request);
        //save it to db
        userRepository.save(user);
        //send reposnse only needed field
        var userDto = userMapper.toUserDto(user);

        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }



    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id")   Long id,
            @RequestBody UserUpdateRequest request
    ){
     var user = userRepository.findById(id).orElse(null);
     if(user == null){
       return  ResponseEntity.notFound().build();
     }

     userMapper.update(request , user);
     userRepository.save(user);
     return ResponseEntity.ok(userMapper.toUserDto(user));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable(name = "id") Long id
    ){
    var user = userRepository.findById(id).orElse(null);

    if(user == null){
      return  ResponseEntity.notFound().build();
    }

    userRepository.delete(user);
  return ResponseEntity.noContent().build();

    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ) {
        var user = userRepository.findById(id).orElse(null);

        if(user == null){
            return  ResponseEntity.notFound().build();
        }

        if(!user.getPassword().equals(request.getOldPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

}

