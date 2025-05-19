package org.fcup.model.auction;

import org.fcup.model.user.User;

public class Bid {
    private final User user;
    private final Long bid;

    public Bid(User user, long currentBid) {
        this.user = user;
        this.bid = currentBid;
    }

    public User getUser() {
        return user;
    }

    public Long getBid() {
        return bid;
    }
}
