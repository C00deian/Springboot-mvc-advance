package com.codewithmosh.store.services;

import com.codewithmosh.store.Dtos.ChangePasswordRequest;
import com.codewithmosh.store.Dtos.UserDto;
import com.codewithmosh.store.Dtos.UserRegisterRequest;
import com.codewithmosh.store.Dtos.UserUpdateRequest;
import com.codewithmosh.store.entities.Role;
import com.codewithmosh.store.exceptions.EmailAlreadyExistException;
import com.codewithmosh.store.exceptions.UnauthorizedUserException;
import com.codewithmosh.store.exceptions.UserNotFoundException;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserDto registerUser(UserRegisterRequest  request) {
        var exists = userRepository.existsUserByEmail(request.getEmail());
        if(exists){
            throw new EmailAlreadyExistException();
        }

        //take request to the user
        var user = userMapper.toEntity(request);
        //hash the Password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        //save it to db
        userRepository.save(user);
        //send response only needed field
        return userMapper.toUserDto(user);

    }

    public List<UserDto> getAllUsers(String sortBy) {
        if(!Set.of("name" , "email").contains(sortBy)){
            sortBy = "name";
        };
        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }


    public UserDto getUser(Long userId) {

        var user = userRepository.findById(userId).orElse(null);

        if(user == null){
          throw new UserNotFoundException();
        }

      return userMapper.toUserDto(user);

    }


    public UserDto updateUser(Long userId, UserUpdateRequest request) {
        var user = userRepository.findById(userId).orElse(null);
        if(user == null){
        throw new UserNotFoundException();
        }

        userMapper.update(request , user);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }


    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElse(null);

        if(user == null){
            throw new UserNotFoundException();
        }

        userRepository.delete(user);
    }


    public void resetPassword(Long userId , ChangePasswordRequest request) {
        var user = userRepository.findById(userId).orElse(null);

        if (user == null) {
           throw  new UserNotFoundException();
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
         throw new UnauthorizedUserException();
        }

        user.setPassword(request.getNewPassword());
       userRepository.save(user);
    }
}
