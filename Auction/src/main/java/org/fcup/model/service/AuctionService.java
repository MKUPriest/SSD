package org.fcup.model.service;

import org.fcup.model.auction.Auction;
import org.fcup.model.dto.AuctionDTO;
import org.fcup.model.dto.BidDTO;
import org.fcup.model.dto.mapper.AuctionMapper;
import org.fcup.model.dto.mapper.BidMapper;
import org.fcup.model.repository.AuctionBlockchainRepository;

import java.time.LocalDateTime;
import java.util.List;

public class AuctionService {
    private final AuctionBlockchainRepository repository;

    public AuctionService() {
        repository = AuctionBlockchainRepository.getInstance();
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
            return repository.update(auction);
        }

        return false;
    }

    public void updateAllActiveAuctions(){
        repository.updateAuctions();
    }
}
