package com.adsologist.adsologist.service;

import java.util.*;
import java.util.stream.Collectors;

import com.adsologist.adsologist.dao.UserRepository;
import com.adsologist.adsologist.dto.UserRequestDTO;
import com.adsologist.adsologist.entity.User;
import com.adsologist.adsologist.exceptions.UserAlreadyExistsException;
import com.adsologist.adsologist.exceptions.UserCreationException;
import com.adsologist.adsologist.exceptions.UserNotFoundException;
import com.adsologist.adsologist.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public UserResponse createUser(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if(Objects.nonNull(foundUser)){
            throw new UserAlreadyExistsException("User with UserName: "+user.getUsername()+"Already exists in the database");
        }
        User savedUser =  userRepository.save(user);
        log.info("User saved with id: " + savedUser.getId());

        return convertToUserResponse(savedUser);
    }

    public List<UserResponse> createUsers(List<User> users) {
        List<User> existingUsers = new ArrayList<>();
        List<User> newUsers = new ArrayList<>();

        // Check if users already exist in the database
        for (User user : users) {
            Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
            if (existingUser.isPresent()) {
                // User already exists, log it or take other action
                existingUsers.add(existingUser.get());
            } else {
                newUsers.add(user);
            }
        }
        if (!newUsers.isEmpty()) {
            // Save the new users
            List<User> createdUsers = userRepository.saveAll(newUsers);
            // Check if user creation failed
            if (createdUsers.isEmpty()) {
                throw new UserCreationException("Failed to create new users");
            }
            // Convert the list of User objects to UserResponse objects
            List<UserResponse> createdUserResponses = createdUsers.stream()
                    .map(this::convertToUserResponse)
                    .collect(Collectors.toList());

            // Log existing users if needed
            for (User existingUser : existingUsers) {
                // Log or take other action for existing users
                // You can add your specific logic here
                log.info("User already exists: " + existingUser.getUsername());
            }

            log.info("Users created");
            return createdUserResponses;
        }
        // All provided users already exist, so no new users were created
       throw new UserAlreadyExistsException("All the provided Users are already present");
    }


    // Helper method to convert User to UserResponse
    private UserResponse convertToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFullName(user.getFullName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }

    public UserResponse getUserById(int id) {
        User foundUser = userRepository.findById(id).orElse(null);
        if(Objects.isNull(foundUser))
            throw new UserNotFoundException("User not found with id :"+ id);
        return convertToUserResponse(foundUser);
    }

    public List<UserResponse> getUsers() {
        List<User> foundUsers = userRepository.findAll();

        if (foundUsers.isEmpty()) {
            throw new UserNotFoundException("No users in DB");
        }
        return foundUsers.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(UserRequestDTO user, int id) {
        User oldUser = null;
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("No users in DB with id: "+id);
        }
            oldUser = optionalUser.get();
            oldUser.setFullName(user.getFullName());
            oldUser.setEmail(user.getEmail());
            oldUser.setPhoneNumber(user.getPhoneNumber());
            oldUser.setUsername(user.getUsername());
            oldUser.setPassword(user.getPassword());
           User savedUser = userRepository.save(oldUser);
           log.info("User updated with id: " + savedUser.getId());

        return convertToUserResponse(savedUser);
    }

    public void deleteUserById(int id) {
        User existingUser = userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("User not found with id: "+id));
        userRepository.deleteById(id);
        log.info("User deleted with id: " + existingUser.getId());

    }

}