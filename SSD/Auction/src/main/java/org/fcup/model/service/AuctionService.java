package org.fcup.model.service;

import org.fcup.model.auction.Auction;
import org.fcup.model.dto.AuctionDTO;
import org.fcup.model.dto.BidDTO;
import org.fcup.model.dto.UserDTO;
import org.fcup.model.dto.mapper.AuctionMapper;
import org.fcup.model.dto.mapper.BidMapper;
import org.fcup.model.repository.AuctionBlockchainRepository;
import org.fcup.model.repository.UserInMemoryRepository;

import java.time.LocalDateTime;
import java.util.List;

public class AuctionService {
    private final AuctionBlockchainRepository repository;
    private final UserInMemoryRepository userRepository;

    public AuctionService() {
        repository = AuctionBlockchainRepository.getInstance();
        userRepository = UserInMemoryRepository.getInstance();
    }

    public boolean saveAuction(AuctionDTO newAuctionDTO) {
        return repository.save(AuctionMapper.convertToAuction(newAuctionDTO));
    }

    public List<AuctionDTO> getAllActiveAuctions() {
        List<Auction> auctionList = repository.findAll();
        List<Auction> activeAuctions = auctionList.stream().filter(Auction::isActive).toList();

        return activeAuctions.stream().map(AuctionMapper::convertToAuctionDTO).toList();
    }

    public boolean bidOnAuction(final AuctionDTO auctionDTO, final BidDTO bidDTO) throws Exception {
        Auction auction = AuctionMapper.convertToAuction(auctionDTO);

        if (auction.getClosingTime().isAfter(LocalDateTime.now())
                && bidDTO != null
                && auction.updateBid(BidMapper.convertToBid(bidDTO))) {
            repository.updateAuctions();
            userRepository.addSubbedAuction(BidMapper.convertToBid(bidDTO).getUser(), auction);
            return repository.update(auction);
        }

        return false;
    }

    public void updateAllActiveAuctions(){
        repository.updateAuctions();
    }

    public List<AuctionDTO> getWinningBids(UserDTO currentUser) {
        repository.updateAuctions();
        return repository.getWinningBids(currentUser).stream().map(AuctionMapper::convertToAuctionDTO).toList();
    }

    public List<AuctionDTO> getOwnedItems(UserDTO currentUser) {
        return repository.getOwnedItems(currentUser).stream().map(AuctionMapper::convertToAuctionDTO).toList();
    }
}
