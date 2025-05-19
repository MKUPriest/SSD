package org.fcup.blockchain;

import org.fcup.model.auction.Transaction;

import java.util.List;
import java.util.Random;

public class Block {
    private String previousHash;
    private long timestamp;
    private int nonce;
    private String hash;
    private List<Transaction> transactions;

    public Block(String previousHash, long timestamp, List<Transaction> transactions) {
        this.previousHash = previousHash;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.nonce = new Random().nextInt(Integer.MAX_VALUE);
    }

    public void printBlock(){
        System.out.println("wwwwwwwwwwwwwwwwwwww");
        System.out.println("Previous Hash: " + previousHash);
        System.out.println("wwwwwwwwwwwwwwwwwwww");
        System.out.println("Timestamp: " + Long.toString(timestamp));
        System.out.println("wwwwwwwwwwwwwwwwwwww");
        System.out.println("Nonce: " + Integer.toString(nonce));
        System.out.println("wwwwwwwwwwwwwwwwwwww");
        System.out.println("Hash: " + hash);
        System.out.println("wwwwwwwwwwwwwwwwwwww");
        for(Transaction transaction : transactions)
            transaction.printTransaction();
        System.out.println("wwwwwwwwwwwwwwwwwwww");
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getNonce(){
        return nonce;
    }

    public void setNonce(int nonce){
        this.nonce = nonce;
    }

}
