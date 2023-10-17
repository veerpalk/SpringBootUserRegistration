package com.adsologist.adsologist.controller;


import com.adsologist.adsologist.dto.UserLoginDTO;
import com.adsologist.adsologist.dto.UserRequestDTO;
import com.adsologist.adsologist.entity.User;
import com.adsologist.adsologist.response.UserResponse;
import com.adsologist.adsologist.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;


@RestController
@CrossOrigin(origins = "*")
@Api(value = "User Registration Service", description = "Operations pertaining to user management")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping({ ""})
    @ApiOperation(value = "Hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello to adsologist  app",HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ApiOperation(value = "login user")
    public ResponseEntity<UserResponse> login(@RequestBody UserLoginDTO loginUser, HttpSession httpSession) {
        UserResponse user = userService.login(loginUser.getUsername(), loginUser.getPassword());
        httpSession.setAttribute("USER_ID", String.valueOf(user.getId()));
        return ResponseEntity.ok(user);
    }
    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/login";
    }
    @GetMapping("/session-expired")
    @ApiOperation(value = "session expired")
    public ResponseEntity<String> sessionExpired() {
        return new ResponseEntity<>("Session has expired!", HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/addUser")
    @ApiOperation(value = "Add User")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>("User is created successfully!",HttpStatus.CREATED);
    }
    @PostMapping("/addUsers")
    @ApiOperation(value = "Add Users")
    public ResponseEntity<String> addUsers(@RequestBody List<User> users) {
        userService.createUsers(users);
        return new ResponseEntity<>("Users is created successfully!",HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "Get User by Id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        UserResponse foundUser= userService.getUserById(id);
        return ResponseEntity.ok(foundUser);
    }
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ApiOperation(value = "Get All Users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> foundUsers = userService.getUsers();
        return ResponseEntity.ok(foundUsers);
    }

    @PutMapping("/updateUser/{id}")
    @ApiOperation(value = "Update User")
    public ResponseEntity<String> updateUser(@RequestBody UserRequestDTO user, @PathVariable int id) {
        UserResponse updatedUser = userService.updateUser(user,id);
        return new ResponseEntity<>("Users is updated successfully!",HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    @ApiOperation(value = "Delete User")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("Users got deleted!",HttpStatus.OK);
    }
}
