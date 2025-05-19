package org.fcup;

import org.fcup.blockchain.Blockchain;
import org.fcup.blockchain.Miner;
import org.fcup.controller.UserController;
import org.fcup.model.dto.UserDTO;
import org.fcup.model.dto.mapper.UserMapper;
import org.fcup.model.repository.UserInMemoryRepository;
import org.fcup.model.user.User;
import org.fcup.utils.Bootstrap;
import org.fcup.view.AuctionView;
import org.fcup.view.UserView;

public class Main {

    public static void main(String[] args) throws Exception {

        int option = 0;
        AuctionView auctionView = new AuctionView();
        while (option != 6) {
            UserDTO currentUserDTO = new UserView().run();
            if(currentUserDTO != null){
                new Bootstrap().boot();
                option = auctionView.run(currentUserDTO);
            }
        }
    }
}
