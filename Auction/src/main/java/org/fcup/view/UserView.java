package org.fcup.view;

import org.fcup.controller.UserController;
import org.fcup.model.dto.UserDTO;

import java.nio.file.Path;
import java.util.Scanner;

public class UserView {
    private static final Scanner scanner = new Scanner(System.in);

    private final UserController controller;

    public UserView() {
        controller = new UserController();
    }

    public UserDTO run() throws Exception {

        int choice;
        UserDTO currentUserDTO = null;

        System.out.println("\n====== Log In/Sign Up page ======\n");
        System.out.println("1 - Log In\n2 - Sign Up\n3 - Exit\n");
        choice = scanner.nextInt();
        scanner.nextLine();
        switch(choice) {
            case 1:
                System.out.print("Please enter your Wallet address name:");
                String addressPath = scanner.nextLine();
                UserDTO userDTO = null;
                try {
                    userDTO = controller.loadFromFile(Path.of(addressPath));
                    System.out.println("Loaded address from " + addressPath);
                } catch (Exception e) {
                    System.err.println("Error loading address: " + e.getMessage());
                }
                currentUserDTO = userDTO;
                break;
            case 2:
                System.out.print("Please enter the name of the file you would like to save your address to:");
                Path filePath = Path.of(scanner.nextLine());
                UserDTO newUserDTO = controller.createNewUser();
                controller.createFile(filePath, newUserDTO.address());
                System.out.println("Created new user with the following address: " + newUserDTO.address());
                currentUserDTO = newUserDTO;
        }
        return currentUserDTO;
    }

}
