package org.fcup.controller;

import org.fcup.model.auction.Auction;
import org.fcup.model.dto.AuctionDTO;
import org.fcup.model.dto.BidDTO;
import org.fcup.model.dto.UserDTO;
import org.fcup.model.service.AuctionService;

import java.util.List;

public class AuctionController {
    private final AuctionService auctionService;

    public AuctionController() {
        auctionService = new AuctionService();
    }

    public boolean createAuction(final AuctionDTO auctionDTO) {
        return auctionService.saveAuction(auctionDTO);
    }

    public List<AuctionDTO> getAllActiveAuctions() {
        return auctionService.getAllActiveAuctions();
    }

    public boolean bidOnAuction(final AuctionDTO auctionDTO, BidDTO bidDTO) throws Exception {
        return auctionService.bidOnAuction(auctionDTO, bidDTO);
    }

    public void updateAllActiveAuctions(){
        auctionService.updateAllActiveAuctions();
    }

    public List<AuctionDTO> getWinningBids(UserDTO currentUser) {
        return auctionService.getWinningBids(currentUser);
    }

    public List<AuctionDTO> getOwnedItems(UserDTO currentUser) {
        return auctionService.getOwnedItems(currentUser);
    }
}
