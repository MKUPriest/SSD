package org.fcup.model.dto.mapper;

import org.fcup.model.dto.UserDTO;
import org.fcup.model.user.User;
import org.fcup.model.user.Wallet;

public class UserMapper {
    public static User convertToUser(UserDTO userDTO) {
        if (userDTO == null)
            return null;

        return new User(userDTO.address(), userDTO.privateKey(), userDTO.publicKey());
    }

    public static UserDTO convertToUserDTO(User user) {
        if (user == null)
            return null;

        return new UserDTO(user.getWallet().getAddress(),
                user.getWallet().getPublicKey(),
                user.getWallet().getPrivateKey());
    }
}
