package org.fcup.model.service;

import org.fcup.model.dto.UserDTO;
import org.fcup.model.dto.mapper.UserMapper;
import org.fcup.model.repository.UserInMemoryRepository;
import org.fcup.model.user.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.fcup.model.dto.mapper.UserMapper.convertToUserDTO;

public class UserService {
    private final UserInMemoryRepository repository;

    public UserService() {
        repository = UserInMemoryRepository.getInstance();
    }

    public boolean saveUser(final UserDTO newUserDTO) {
        return repository.save(UserMapper.convertToUser(newUserDTO));
    }

    public UserDTO createNewUser() throws Exception {
        User newUser = new User();
        return convertToUserDTO(newUser);
    }

    public UserDTO loadFromFile(Path path) throws Exception {
        String address;
        try {
            Path newPath = Paths.get("Keys").resolve(path.toString());
            address = Files.readString(newPath);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return null;
        }
        User user = null;
        if(repository.findById(address).isPresent()) {
            user = repository.findById(address).get();
        }else{
            System.out.println("User was not found.");
            return null;
        }
        return convertToUserDTO(user);
    }

    public boolean createFile(Path filePath, String address){
        try {
            Path newPath = Paths.get("Keys").resolve(filePath.toString());
            Files.createDirectories(newPath.getParent());
            Files.writeString(newPath, address);

            System.out.println("Wallet address saved to: " + filePath);
            return true;

        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            return false;
        }
    }
}
