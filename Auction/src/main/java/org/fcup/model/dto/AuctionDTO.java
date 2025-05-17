package org.fcup.model.dto;

import org.fcup.model.user.User;

import java.time.LocalDateTime;

public record AuctionDTO(ItemDTO auctionedItemDTO,
                         BidDTO bidDTO,
                         boolean isActive,
                         LocalDateTime closeTime) {
}
