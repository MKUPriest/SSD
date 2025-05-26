package org.fcup.blockchain;

import org.fcup.model.auction.Transaction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<Block>();

        this.chain.add(createBlockGenesis());
    }

    public List<Block> getChain() {
        return chain;
    }

    private Block createBlockGenesis() {
        String pseudoPreviousHash = "00000" + RandomStringGenerator();
        Block genesis = new Block(pseudoPreviousHash, 0, new ArrayList<Transaction>());
        genesis.setHash(calculateGenesisHash(genesis));
        return genesis;
    }

    public String RandomStringGenerator() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int length = 59;
        final SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public void addBlock(Block block) {
        chain.add(block);
    }

    public String calculateGenesisHash(Block block) {
        int nonce = 0;
        String hash;

        do {
            String data = block.getPreviousHash()
                    + Long.toString(block.getTimestamp())
                    + Integer.toString(nonce)
                    + block.getTransactions().toString();

            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = digest.digest(data.getBytes());
                StringBuilder hexString = new StringBuilder();
                for (byte b : hashBytes) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                hash = hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            nonce++;
        } while (!hash.startsWith("00000"));

        // Optionally store the final nonce in the block
        block.setNonce(nonce - 1); // -1 because we increment one extra after finding the hash

        return hash;
    }
}
