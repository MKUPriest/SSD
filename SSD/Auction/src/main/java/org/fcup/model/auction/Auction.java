package org.fcup.model.auction;

import java.time.LocalDateTime;

public class Auction {
    private final Item auctionedItem;
    private final LocalDateTime closingTime;
    private Bid currentBid;
    private boolean isActive;

    public Auction(final Item auctionedItem, final LocalDateTime closingTime) {
        validateItem(auctionedItem);
        validateClosingTime(closingTime);
        this.auctionedItem = auctionedItem;
        this.closingTime = closingTime;
        this.isActive = true;
    }

    public Auction(final Item auctionedItem,
                   final LocalDateTime closingTime,
                   final Bid currentBid,
                   final boolean isActive) {
        validateItem(auctionedItem);
        validateClosingTime(closingTime);

        this.auctionedItem = auctionedItem;
        this.closingTime = closingTime;
        this.currentBid = currentBid;
        this.isActive = isActive;
    }

    private void validateItem(final Item item) {
        if (item == null)
            throw new IllegalArgumentException("No auctioned item specified;\nPlease provide a valid auction;");
    }

    private void validateClosingTime(final LocalDateTime closingTime) {
        if (closingTime == null)
            throw new IllegalArgumentException("No closing time specified;\nPlease provide a valid time to auction;");
    }

    public Item getAuctionedItem() {
        return auctionedItem;
    }

    public Bid getCurrentBid() {
        return currentBid;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean updateBid(final Bid newBid) {
        if (currentBid == null || currentBid.getBid() < newBid.getBid()) {
            if(newBid.getBid() < auctionedItem.getMinimumValue())
                return false;
            currentBid = newBid;
            return true;
        }

        return false;
    }
}
