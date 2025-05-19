package org.fcup.model.dto;

import java.security.PrivateKey;
import java.security.PublicKey;

public record UserDTO(String address, PublicKey publicKey, PrivateKey privateKey) {
}
