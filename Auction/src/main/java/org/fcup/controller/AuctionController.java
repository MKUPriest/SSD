package org.fcup.controller;

import org.fcup.blockchain.Miner;
import org.fcup.model.dto.AuctionDTO;
import org.fcup.model.dto.BidDTO;
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

}
