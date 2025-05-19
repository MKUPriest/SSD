package org.fcup.controller;

import org.fcup.model.dto.UserDTO;
import org.fcup.model.service.UserService;

import java.nio.file.Path;

public class UserController {
    private final UserService userService;

    public UserController() {
        userService = new UserService();
    }

    public boolean createUser(final UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    public UserDTO createNewUser() throws Exception {
        UserDTO newUser = userService.createNewUser();
        userService.saveUser(newUser);
        return newUser;
    }

    public UserDTO loadFromFile(Path path) throws Exception {
        return userService.loadFromFile(path);
    }

    public void createFile(Path filePath, String address){
        userService.createFile(filePath, address);
    }
}
