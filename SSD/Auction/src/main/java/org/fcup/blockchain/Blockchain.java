package org.fcup.blockchain;

import org.fcup.model.auction.Transaction;

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
        return new Block(pseudoPreviousHash, 0, new ArrayList<Transaction>());
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
}
