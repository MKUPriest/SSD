package org.fcup.model.auction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.time.LocalDateTime;

public class Transaction {

    private Item item;
    private LocalDateTime closingTime;
    private Long bid;
    private String address;
    private PublicKey publicKey;
    private String hash;
    private String signature;

    public Transaction(Item item, LocalDateTime closingTime,Long bid, String address, PublicKey publicKey, String signature) {
        this.item = item;
        this.closingTime = closingTime;
        this.address = address;
        this.publicKey = publicKey;
        this.signature = signature;
        if(bid == null){
            this.bid = 0L;
        }else{
            this.bid = bid;
        }
        hash = calculateHash();
    }

    public void printTransaction(){
        System.out.println("Item: " + item.getName());
        System.out.println("----------");
        System.out.println("Bid: " + Long.toString(bid));
        System.out.println("----------");
        System.out.println("Address: " + address);
        System.out.println("----------");
    }

    public LocalDateTime getClosingTime(){
        return closingTime;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getHash(){
        return hash;
    }

    public String getSignature(){
        return signature;
    }

    public void setSignature(String signature){
        this.signature = signature;
    }

    public String getData(){
        return item
                + Long.toString(bid)
                + address;
    }

    public String calculateHash() {
        String data = item
                + Long.toString(bid)
                + address;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
