package org.fcup.model.dto.mapper;

import org.fcup.model.auction.Auction;
import org.fcup.model.dto.AuctionDTO;
import org.fcup.model.dto.BidDTO;

public class AuctionMapper {
    public static Auction convertToAuction(AuctionDTO auctionDTO) {
        return new Auction(ItemMapper.convertToItem(auctionDTO.auctionedItemDTO()),
                auctionDTO.closeTime(),
                BidMapper.convertToBid(auctionDTO.bidDTO()),
                auctionDTO.isActive());
    }

    public static AuctionDTO convertToAuctionDTO(Auction auction) {
        return new AuctionDTO(ItemMapper.convertToItemDTO(auction.getAuctionedItem()),
                BidMapper.convertToBidDTO(auction.getCurrentBid()),
                auction.isActive(),
                auction.getClosingTime());
    }
}
