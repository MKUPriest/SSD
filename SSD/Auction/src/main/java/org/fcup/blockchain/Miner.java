package org.fcup.blockchain;

import org.fcup.model.auction.Item;
import org.fcup.model.auction.Transaction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class Miner {

    private List<Blockchain> blockchains;
    private List<Transaction> mempoolBids;

    public Miner(List<Blockchain> blockchains) {
        this.blockchains = blockchains;
        mempoolBids = new ArrayList<Transaction>();
    }

    public List<Blockchain> getBlockchain() {
        return blockchains;
    }

    public boolean replaceChain(Blockchain newChain) throws Exception {
        if(isChainValid(newChain)){
            blockchains.clear();
            blockchains.add(newChain);
            return true;
        }
        return false;
    }

    public int getMempoolSize(){
        return mempoolBids.size();
    }

    public void printMempool(){
        for(Transaction transaction : mempoolBids){
            System.out.println("+++++++++++++++");
            transaction.printTransaction();
            System.out.println("+++++++++++++++");
            System.out.println();
        }
    }

    public boolean isValidBlock(Block block, Blockchain newChain) throws Exception {
        String hash = calculateHash(block, 0);
        return block.getHash().equals(hash)
                && hash.startsWith("00000")
                && verifySignature(block);
    }

    public boolean isChainValid(Blockchain newChain) throws Exception {
        for(Blockchain aux : blockchains) {
            if (aux.getChain().size() > newChain.getChain().size()) {
                return false;
            }
        }
        Block previousBlock = newChain.getChain().getFirst();

        for(int i = 1; i < newChain.getChain().size(); i++) {
            Block currentBlock = newChain.getChain().get(i);
            if(currentBlock.getPreviousHash().equals(previousBlock.getHash())){
                return false;
            }
            if(!isValidBlock(currentBlock, newChain)){
                return false;
            }
            previousBlock = currentBlock;
        }
        return true;
    }

    public Block mineBlock() {
        List<Transaction> bids = new ArrayList<>();
        bids.add(mempoolBids.getFirst());
        Block block = new Block(blockchains.getFirst().getChain().getLast().getHash(),
                new Date().getTime(),
                bids);
        String target = new String(new char[5]).replace('\0', '0');
        int nonce = block.getNonce();
        String hash = calculateHash(block, nonce);
        while (!hash.substring(0, 5).equals(target)) {
            nonce++;
            hash = calculateHash(block, nonce);
        }
        block.setNonce(nonce);
        block.setHash(hash);
        return block;
    }

    public String calculateHash(Block block, int nonce) {
        String data = block.getPreviousHash()
                + Long.toString(block.getTimestamp())
                + Integer.toString(nonce)
                + block.getTransactions().toString();
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

    public Transaction addConfirmedBlock(Block block){
        Blockchain biggest = null;

        for (Blockchain aux : blockchains) {
            if (biggest == null || aux.getChain().size() > biggest.getChain().size()) {
                biggest = aux;
            }
        }

        blockchains.clear();
        blockchains.add(biggest);

        blockchains.getFirst().addBlock(block);

        if (block.getTransactions().getFirst().equals(mempoolBids.getFirst())) {
            mempoolBids.removeFirst();
        }

        return block.getTransactions().getFirst();
    }


    public boolean addTransaction(Transaction transaction){
        if(mempoolBids.size() > 10){
            // Send to Kademlia
            return false;
        }
        mempoolBids.add(transaction);
        return true;
    }

    public static boolean verifySignature(Block block) throws Exception {
        byte[] signatureBytes = Base64.getDecoder().decode(block.getTransactions().getLast().getSignature());

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(block.getTransactions().getLast().getPublicKey());
        String data = block.getTransactions().getLast().getData();
        signature.update(data.getBytes());

        return signature.verify(signatureBytes);
    }

    public List<Transaction> getUpdatedAuctions() {
        List<Item> diff_items = new ArrayList<>();
        List<Transaction> updatedTransactions = new ArrayList<>();
        for(Block aux : blockchains.getFirst().getChain()){
            if(blockchains.getFirst().getChain().getFirst() != aux) {
                Item aux_item = aux.getTransactions().getFirst().getItem();
                if (!diff_items.contains(aux_item)) {
                    diff_items.add(aux_item);
                    updatedTransactions.add(aux.getTransactions().getFirst());
                }
            }
        }
        return updatedTransactions;
    }
}
