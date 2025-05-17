package org.fcup.model.repository;

import org.fcup.blockchain.Block;
import org.fcup.blockchain.Blockchain;
import org.fcup.blockchain.Miner;
import org.fcup.model.auction.Auction;
import org.fcup.model.auction.Bid;
import org.fcup.model.auction.Item;
import org.fcup.model.auction.Transaction;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TODO - Complete when Transaction classes in Blockchain project is set;
 */
public class AuctionBlockchainRepository implements Repository<Auction, String> {
    private record InMemoryAuction(Item auctionedItem, LocalDateTime closingTime, Bid bid, boolean isActive) {}

    private static AuctionBlockchainRepository instance;

    private List<InMemoryAuction> inMemoryAuctions;

    private final Miner miner;

    public static AuctionBlockchainRepository getInstance() {
        if (instance == null) {
            instance = new AuctionBlockchainRepository();
        }

        return instance;
    }

    private AuctionBlockchainRepository() {
        this.miner = new Miner(new Blockchain());
        inMemoryAuctions = new ArrayList<>();
    }

    /**
     * TODO
     *
     * @param newAuction
     * @return
     */
    @Override
    public boolean save(final Auction newAuction) {
        InMemoryAuction inMemoryAuction = new InMemoryAuction(newAuction.getAuctionedItem(), newAuction.getClosingTime(), newAuction.getCurrentBid(),newAuction.isActive());

        Transaction transaction = new Transaction(newAuction.getAuctionedItem(),
                newAuction.getClosingTime(),
                newAuction.getCurrentBid().getBid(),
                newAuction.getCurrentBid().getUser().getWallet().getAddress(),
                newAuction.getCurrentBid().getUser().getWallet().getPublicKey(),
                null);

        try {
            transaction.setSignature(newAuction.getCurrentBid().getUser().sign(transaction.getData()));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }

        miner.addTransaction(transaction);
        Block block = miner.mineBlock();
        miner.addConfirmedBlock(block);

        //System.out.println(miner.getBlockchain().getChain().size());

        if (!inMemoryAuctions.contains(inMemoryAuction)) {
            inMemoryAuctions.add(inMemoryAuction);

            return true;
        }

        return false;
    }

    @Override
    public boolean update(final Auction auction) throws Exception {
        System.out.println("Eu: " + auction.getAuctionedItem().getName());

        Optional<InMemoryAuction> optionalAuction = inMemoryAuctions.stream()
                .filter(aux -> aux.auctionedItem().equals(auction.getAuctionedItem()))
                .findFirst();

        System.out.println(optionalAuction);

        Transaction transaction = new Transaction(auction.getAuctionedItem(),
                auction.getClosingTime(),
                auction.getCurrentBid().getBid(),
                auction.getCurrentBid().getUser().getWallet().getAddress(),
                auction.getCurrentBid().getUser().getWallet().getPublicKey(),
                null);

        transaction.setSignature(auction.getCurrentBid().getUser().sign(transaction.getData()));

        System.out.println(optionalAuction.isPresent());

        if (optionalAuction.isPresent()) {
            inMemoryAuctions.remove(optionalAuction.get());
            inMemoryAuctions.add(new InMemoryAuction(
                    auction.getAuctionedItem(),
                    auction.getClosingTime(),
                    auction.getCurrentBid(),
                    auction.isActive()
            ));
            return true;
        }

        return false;
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public List<Auction> findAll() {
        return inMemoryAuctions.stream().map(inMemoryAuction -> new Auction(inMemoryAuction.auctionedItem,inMemoryAuction.closingTime, inMemoryAuction.bid, inMemoryAuction.isActive)).collect(Collectors.toList());
    }

    /**
     * TODO
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Auction> findById(String id) {
        return Optional.empty();
    }

    public void updateAuctions(){
        List<InMemoryAuction> updatedAuctions = new ArrayList<>();
        List<Transaction> updatedTransactions = miner.getUpdatedAuctions();
        for(Transaction aux : updatedTransactions){
            if(aux.getClosingTime().isAfter(LocalDateTime.now())) {
                updatedAuctions.add(new InMemoryAuction(aux.getItem(),
                        aux.getClosingTime(),
                        new Bid(UserInMemoryRepository.getInstance().findById(aux.getAddress()).get(), aux.getBid()),
                        true
                        ));
            }
        }
        inMemoryAuctions = updatedAuctions;
    }
}
