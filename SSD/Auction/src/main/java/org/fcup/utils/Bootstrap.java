package org.fcup.utils;

import org.fcup.controller.AuctionController;
import org.fcup.controller.UserController;
import org.fcup.model.dto.AuctionDTO;
import org.fcup.model.dto.BidDTO;
import org.fcup.model.dto.ItemDTO;
import org.fcup.model.dto.UserDTO;
import org.fcup.model.dto.mapper.UserMapper;
import org.fcup.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class Bootstrap {
    private final AuctionController auctionController;
    private final UserController userController;

    public Bootstrap() {
        auctionController = new AuctionController();
        userController = new UserController();
    }

    public void boot() throws Exception {

        UserDTO defaultUser = userController.createNewUser();

        ItemDTO itemDTO1 = new ItemDTO("PlayStation 5", 350, defaultUser);
        ItemDTO itemDTO2 = new ItemDTO("Apple MacBook Pro 14", 1200, defaultUser);
        ItemDTO itemDTO3 = new ItemDTO("Vintage Rolex Submariner", 5000, defaultUser);
        ItemDTO itemDTO4 = new ItemDTO("Fender Stratocaster 1965", 2500, defaultUser);
        ItemDTO itemDTO5 = new ItemDTO("iPhone 15 Pro Max", 999, defaultUser);
        ItemDTO itemDTO6 = new ItemDTO("Lego Millennium Falcon Collector Edition", 750, defaultUser);
        ItemDTO itemDTO7 = new ItemDTO("Canon EOS R6 Camera", 1800, defaultUser);
        ItemDTO itemDTO8 = new ItemDTO("Gaming Chair Secretlab Titan", 400, defaultUser);

        BidDTO bidDTO = new BidDTO(0L, defaultUser);

        List<AuctionDTO> auctionDTOList = List.of(new AuctionDTO(itemDTO1, bidDTO, true, LocalDateTime.of(2025, 6,4,16, 0))//,
                /*new AuctionDTO(itemDTO2, bidDTO, true, LocalDateTime.of(2025, 6,4,16, 0)),
                *new AuctionDTO(itemDTO3, bidDTO, false, LocalDateTime.of(2025, 4,24,12, 15)),
                *new AuctionDTO(itemDTO4, bidDTO, false, LocalDateTime.of(2025, 5,5,19, 30)),
                *new AuctionDTO(itemDTO5, bidDTO, false, LocalDateTime.of(2025, 2,2,9, 45)),
                *new AuctionDTO(itemDTO6, bidDTO, true, LocalDateTime.of(2025, 8,16,20, 0)),
                *new AuctionDTO(itemDTO7, bidDTO, true, LocalDateTime.of(2025, 6,12,17, 30)),
                *new AuctionDTO(itemDTO8, bidDTO, true, LocalDateTime.of(2025, 5,24,10, 15))
                 */);


        auctionDTOList.forEach(auctionController::createAuction);
    }
}
