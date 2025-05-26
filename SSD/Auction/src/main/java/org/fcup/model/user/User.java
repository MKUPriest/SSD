package org.fcup.model.user;

import org.fcup.model.auction.Auction;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class User {
    private final Wallet wallet;
    private List<Auction> subbedAuctions;

    public User() throws Exception {
        wallet = new Wallet();
        subbedAuctions = new ArrayList<>();
    }

    public User(String address, PrivateKey privateKey, PublicKey publicKey, List<Auction> subbedAuctions) {
        wallet = new Wallet(address, privateKey, publicKey);
        this.subbedAuctions = subbedAuctions;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public List<Auction> getSubbedAuctions(){
        return subbedAuctions;
    }

    public String sign(String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        PrivateKey privateKey = wallet.getPrivateKey();

        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(data.getBytes());

        byte[] signatureBytes = signer.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }
}
