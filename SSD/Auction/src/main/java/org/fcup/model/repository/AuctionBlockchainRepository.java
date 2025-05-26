package org.fcup.model.repository;

import org.fcup.blockchain.Block;
import org.fcup.blockchain.Blockchain;
import org.fcup.blockchain.Miner;
import org.fcup.model.auction.Auction;
import org.fcup.model.auction.Bid;
import org.fcup.model.auction.Item;
import org.fcup.model.auction.Transaction;
import org.fcup.model.dto.AuctionDTO;
import org.fcup.model.dto.UserDTO;
import org.fcup.model.dto.mapper.UserMapper;
import org.fcup.model.user.User;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        List<Blockchain> blockchains = new ArrayList<>();
        Blockchain blockchain = new Blockchain();
        blockchains.add(blockchain);
        this.miner = new Miner(blockchains);
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

        InMemoryAuction wantedAuction = null;

        for(InMemoryAuction aux : inMemoryAuctions){

            if(aux.auctionedItem.getName().equals(auction.getAuctionedItem().getName())){
                wantedAuction = aux;
                break;
            }
        }

        //System.out.println(wantedAuction);

        Transaction transaction = new Transaction(auction.getAuctionedItem(),
                auction.getClosingTime(),
                auction.getCurrentBid().getBid(),
                auction.getCurrentBid().getUser().getWallet().getAddress(),
                auction.getCurrentBid().getUser().getWallet().getPublicKey(),
                null);

        transaction.setSignature(auction.getCurrentBid().getUser().sign(transaction.getData()));

        //System.out.println(wantedAuction);

        if (wantedAuction != null) {
            inMemoryAuctions.remove(wantedAuction);
            inMemoryAuctions.add(new InMemoryAuction(
                    auction.getAuctionedItem(),
                    auction.getClosingTime(),
                    auction.getCurrentBid(),
                    auction.isActive()
            ));
            miner.addTransaction(transaction);
            Block block = miner.mineBlock();
            miner.addConfirmedBlock(block);
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

    public void updateAuctions() {
        List<InMemoryAuction> updatedAuctions = new ArrayList<>();
        List<Transaction> updatedTransactions = miner.getUpdatedAuctions();

        System.out.println(updatedTransactions.getFirst().getAddress());

        for (Transaction aux : updatedTransactions) {
            //if(UserInMemoryRepository.getInstance().findById(aux.getAddress()).isPresent()){
            //    System.out.println("User not found");
            //}
            //System.out.println((UserInMemoryRepository.getInstance().getUserListSize()));
            //UserInMemoryRepository.getInstance().printWallets();

            if (aux.getClosingTime().isAfter(LocalDateTime.now())) {
                InMemoryAuction imaa = new InMemoryAuction(
                        aux.getItem(),
                        aux.getClosingTime(),
                        new Bid(UserInMemoryRepository.getInstance().findById(aux.getAddress()).get(), aux.getBid()),
                        true
                );

                boolean alreadyExists = false;
                int i = 0;
                for (InMemoryAuction existing : updatedAuctions) {
                    if (Objects.equals(existing.auctionedItem.getName(), imaa.auctionedItem.getName())) {
                        alreadyExists = true;
                        break;
                    }
                    i++;
                }

                if (!alreadyExists) {
                    updatedAuctions.add(imaa);
                }else{
                    if(updatedAuctions.get(i).bid.getBid() < imaa.bid.getBid()){
                        updatedAuctions.remove(i);
                        updatedAuctions.add(imaa);
                    }
                }
            }
        }

        inMemoryAuctions = updatedAuctions;
    }

    public List<Auction> getWinningBids(UserDTO currentUser) {
        User user = UserMapper.convertToUser(currentUser);
        List<Auction> subbedAuctions = new ArrayList<>();
        Auction auxAuc = null;
        int size = 0;
        if(user.getSubbedAuctions() != null){
            size = user.getSubbedAuctions().size();
        }
        for(InMemoryAuction aux : inMemoryAuctions){
            for(int i = 0; i < size; i++) {
                if (aux.auctionedItem.getName().equals(user.getSubbedAuctions().get(i).getAuctionedItem().getName())) {
                    auxAuc = new Auction(aux.auctionedItem,
                            aux.closingTime,
                            aux.bid,
                            aux.isActive);
                    subbedAuctions.add(auxAuc);
                }
            }
        }
        return subbedAuctions;
    }

    public List<Auction> getOwnedItems(UserDTO currentUser) {
        List<Auction> ownedItems = new ArrayList<>();
        Auction auxAuc = null;
        for(InMemoryAuction aux : inMemoryAuctions){
            if(Objects.equals(aux.auctionedItem.getOwner().getWallet().getAddress(), currentUser.address())){
                auxAuc = new Auction(aux.auctionedItem,
                        aux.closingTime,
                        aux.bid,
                        aux.isActive);
                ownedItems.add(auxAuc);
            }
        }
        return ownedItems;
    }

}
