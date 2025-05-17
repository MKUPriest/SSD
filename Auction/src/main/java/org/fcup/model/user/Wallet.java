package org.fcup.model.user;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.util.Base64;

public class Wallet {
    private static String address;
    private static PublicKey publicKey;
    private PrivateKey privateKey;

    public Wallet() throws Exception {
        generateKeys();
        generateAddress();
    }

    public Wallet(String address, PrivateKey privateKey, PublicKey publicKey) {
        Wallet.address = address;

        this.privateKey = privateKey;
        Wallet.publicKey = publicKey;
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private void generateKeys() throws NoSuchAlgorithmException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public static byte[] sha256(byte[] input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input);
    }

    public static byte[] ripemd160(byte[] input) {
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(input, 0, input.length);
        byte[] out = new byte[20];
        digest.doFinal(out, 0);
        return out;
    }

    public static void generateAddress() throws Exception {
        byte[] pubKeyBytes = publicKey.getEncoded();

        byte[] sha256 = sha256(pubKeyBytes);
        byte[] ripemd160 = ripemd160(sha256);

        address = Base64.getUrlEncoder().encodeToString(ripemd160);
    }

    public String getAddress() {
        return address;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
