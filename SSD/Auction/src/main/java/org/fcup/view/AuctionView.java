package org.fcup.view;

import org.fcup.controller.AuctionController;
import org.fcup.model.dto.AuctionDTO;
import org.fcup.model.dto.BidDTO;
import org.fcup.model.dto.ItemDTO;
import org.fcup.model.dto.UserDTO;
import org.fcup.model.dto.mapper.UserMapper;
import org.fcup.model.repository.AuctionBlockchainRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class AuctionView {
    private final static Scanner SCANNER = new Scanner(System.in);

    private final AuctionController controller;

    public AuctionView() {
        controller = new AuctionController();
    }

    public int run(UserDTO currentUser) throws Exception {
        System.out.println("\n====== Welcome to the Auction Menu! ======\n");

        int option;

        do {
            System.out.println("Please select an option:\n1 - Create Auction\n2 - Bid on an Auction\n3 - Your subscribed Auctions\n4 - Your auctioned Items\n5 - Log Out\n6 - Exit");
            System.out.print("Option: ");

            option = SCANNER.nextInt();
            SCANNER.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Please enter the auctioned item name: ");

                    String auctionedItemName = SCANNER.nextLine();

                    System.out.print("Please enter the auctioned item minimum value: ");

                    long minimumValue = SCANNER.nextLong();
                    SCANNER.nextLine();

                    String pattern = "(\\d\\d)/(\\d\\d)/(\\d\\d\\d\\d) (\\d\\d):(\\d\\d)";
                    Pattern regex = Pattern.compile(pattern);
                    String auctionClosingTime;
                    Matcher matcher;

                    do {
                        System.out.print("Please enter the auction's closing time (format: dd/MM/yyyy HH:mm): ");

                        auctionClosingTime = SCANNER.nextLine();
                        matcher = regex.matcher(auctionClosingTime);

                        if (!matcher.matches())
                            System.out.println("Invalid auction closing time. Please try again using a format of: dd/MM/yyyy HH:mm.");

                    } while (!matcher.matches());

                    if (controller.createAuction(new AuctionDTO(new ItemDTO(auctionedItemName, minimumValue, currentUser),
                            new BidDTO(0L, currentUser),
                            true,
                            LocalDateTime.of(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)))))) {
                        System.out.println("Auction successfully created!");
                    } else {
                        System.out.println("Failed to create auction. Please try again.");
                    }
                    break;
                case 2:
                    controller.updateAllActiveAuctions();
                    List<AuctionDTO> activeAuctionsDTO = controller.getAllActiveAuctions();
                    AuctionDTO selectedAuctionDTO = null;

                    int innerOption;
                    boolean validInnerOption;

                    do {
                        IntStream.range(0, activeAuctionsDTO.size()).forEach(index -> {
                            AuctionDTO activeAuctionDTO = activeAuctionsDTO.get(index);

                            System.out.print("Auction #" + (index + 1) + " - Item Name: " + activeAuctionDTO.auctionedItemDTO().name() + " & " + "Starting Price: " + activeAuctionDTO.auctionedItemDTO().minimumValue() + " € " + " & " + " Current Bid: " + activeAuctionDTO.bidDTO().currentBid() + " € " + "\n");
                        });

                        System.out.print("Please select the auction you intend to bid on and subscribe to: ");

                        innerOption = SCANNER.nextInt();
                        validInnerOption = !(innerOption <= 0 || innerOption > activeAuctionsDTO.size() + 1);

                        if (!validInnerOption) {
                            System.out.println("Invalid option. Please try again.");
                        } else {
                            selectedAuctionDTO = activeAuctionsDTO.get(innerOption - 1);
                        }
                    } while (!validInnerOption);

                    System.out.print("Please enter the amount you intend to bid: ");
                    long amountToBid = SCANNER.nextLong();

                    if (controller.bidOnAuction(selectedAuctionDTO, new BidDTO(amountToBid, currentUser))) {
                        System.out.println("Bid successfully place on auction!");
                    } else
                        System.out.println("Failed to bid on auction. Please try again.");

                    break;
                case 3:
                    List<AuctionDTO> winningBids = controller.getWinningBids(currentUser);
                    IntStream.range(0, winningBids.size()).forEach(index -> {
                        AuctionDTO winningAuctionDTO = winningBids.get(index);

                        System.out.print("Auction #" + (index + 1) + " - Item Name: " + winningAuctionDTO.auctionedItemDTO().name() + " & " + "Current Highest Bid: " + winningAuctionDTO.bidDTO().currentBid() + " € " + "\n");
                    });
                    System.out.println("Press Enter to leave");
                    SCANNER.nextLine();
                    break;
                case 4:
                    List<AuctionDTO> ownedItems = controller.getOwnedItems(currentUser);
                    IntStream.range(0, ownedItems.size()).forEach(index -> {
                        AuctionDTO ownedItemsDTO = ownedItems.get(index);

                        System.out.print("Item Name: " + ownedItemsDTO.auctionedItemDTO().name() + " & " + "Highest Bid: " + ownedItemsDTO.bidDTO().currentBid() + " € " + "\n");
                    });
                    System.out.println("Press Enter to leave");
                    SCANNER.nextLine();
                    break;
                case 5:
                    System.out.print("Logging out...");
                    break;
                case 6:
                    System.out.println("Quitting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (option != 5 && option != 6);
        return option;
    }
}
