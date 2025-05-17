package org.fcup.model.user;

import java.security.*;
import java.util.Base64;

public class User {
    private final Wallet wallet;

    public User() throws Exception {
        wallet = new Wallet();
    }

    public User(String address, PrivateKey privateKey, PublicKey publicKey) {
        wallet = new Wallet(address, privateKey, publicKey);
    }

    public Wallet getWallet() {
        return wallet;
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
