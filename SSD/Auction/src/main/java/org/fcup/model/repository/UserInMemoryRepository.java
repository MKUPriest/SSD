package org.fcup.model.repository;

import org.fcup.blockchain.Blockchain;
import org.fcup.blockchain.Miner;
import org.fcup.model.auction.Auction;
import org.fcup.model.user.User;
import org.fcup.model.user.Wallet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserInMemoryRepository implements Repository<User, String> {

    private record InMemoryUser(Wallet wallet, List<Auction> subbedAuctions) {
    }

    private List<InMemoryUser> inMemoryUsers = new ArrayList<>();

    private static UserInMemoryRepository instance;

    public static UserInMemoryRepository getInstance() {
        if (instance == null) {
            instance = new UserInMemoryRepository();
        }

        return instance;
    }

    @Override
    public boolean save(User object) {
        InMemoryUser user = new InMemoryUser(object.getWallet(), object.getSubbedAuctions());
        if (!inMemoryUsers.contains(user)) {
            inMemoryUsers.add(user);
            //printWallets();
            return true;
        }
        //printWallets();
        return false;
    }

    @Override
    public boolean update(User object) {
        Optional<UserInMemoryRepository.InMemoryUser> optionalUser = inMemoryUsers.stream()
                .filter(aux -> aux.wallet().equals(object.getWallet()))
                .findFirst();

        if (optionalUser.isPresent()) {
            inMemoryUsers.remove(optionalUser.get());
            inMemoryUsers.add(new InMemoryUser(object.getWallet(), object.getSubbedAuctions()));
            return true;
        }
        return false;
    }

    @Override
    public List<User> findAll() {
        return inMemoryUsers.stream().map(inMemoryUser -> new User(inMemoryUser.wallet().getAddress(), inMemoryUser.wallet().getPrivateKey(), inMemoryUser.wallet().getPublicKey(), inMemoryUser.subbedAuctions())).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String id) {
        for (InMemoryUser inMemoryUser : inMemoryUsers) {
            if (inMemoryUser.wallet().getAddress().equals(id))
                return Optional.of(new User(inMemoryUser.wallet().getAddress(), inMemoryUser.wallet().getPrivateKey(), inMemoryUser.wallet().getPublicKey(), inMemoryUser.subbedAuctions()));
        }
        return Optional.empty();
    }

    public int getUserListSize(){
        return inMemoryUsers.size();
    }

    public void printWallets(){
        for(InMemoryUser aux : inMemoryUsers){
            System.out.println(aux.wallet().getAddress());
        }
    }

    public void addSubbedAuction(User user, Auction auction) {
        boolean notSubscribed = true;
        for(InMemoryUser aux : inMemoryUsers){
            if (aux.wallet().getAddress().equals(user.getWallet().getAddress())){
                if(aux.subbedAuctions != null) {
                    for (Auction auxAuc : aux.subbedAuctions) {
                        if (auxAuc.getAuctionedItem().getName().equals(auction.getAuctionedItem().getName())) {
                            notSubscribed = false;
                            break;
                        }
                    }
                }
                if (notSubscribed){
                    aux.subbedAuctions.add(auction);
                    break;
                }
            }
        }
    }
}
