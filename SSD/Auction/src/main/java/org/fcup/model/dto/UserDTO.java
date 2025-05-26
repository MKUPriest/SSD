package org.fcup.model.dto;

import org.fcup.model.auction.Auction;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public record UserDTO(String address, PublicKey publicKey, PrivateKey privateKey, List<Auction> subbedAuctions) {
}
