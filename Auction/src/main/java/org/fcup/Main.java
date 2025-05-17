package org.fcup;

import org.fcup.blockchain.Blockchain;
import org.fcup.blockchain.Miner;
import org.fcup.model.dto.UserDTO;
import org.fcup.model.repository.UserInMemoryRepository;
import org.fcup.utils.Bootstrap;
import org.fcup.view.AuctionView;
import org.fcup.view.UserView;

public class Main {
    public static void main(String[] args) throws Exception {
        int option = 0;
        AuctionView auctionView = new AuctionView();
        while (option != 5) {
            UserDTO currentUserDTO = new UserView().run();
            //String cuser = currentUserDTO.toString();
            //System.out.println(cuser);
            if(currentUserDTO != null){
                new Bootstrap().boot(currentUserDTO);
                option = auctionView.run(currentUserDTO);
            }
        }
    }
}
