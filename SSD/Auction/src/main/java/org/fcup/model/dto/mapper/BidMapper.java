package org.fcup.model.dto.mapper;

import org.fcup.model.auction.Bid;
import org.fcup.model.dto.BidDTO;

public class BidMapper {
    public static Bid convertToBid(final BidDTO bidDTO) {
        if (bidDTO != null) {
            return new Bid(UserMapper.convertToUser(bidDTO.userDTO()), bidDTO.currentBid());
        }
        return null;
    }

    public static BidDTO convertToBidDTO(final Bid bid) {
        if (bid != null)
            return new BidDTO(bid.getBid(), UserMapper.convertToUserDTO(bid.getUser()));

        return null;
    }
}
