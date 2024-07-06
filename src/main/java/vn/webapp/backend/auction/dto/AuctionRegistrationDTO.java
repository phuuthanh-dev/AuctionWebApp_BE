package vn.webapp.backend.auction.dto;

import vn.webapp.backend.auction.enums.AuctionState;

import java.sql.Timestamp;

public record AuctionRegistrationDTO (
        Integer id,
        String name,
        Timestamp startDate,
        Timestamp endDate,
        AuctionState state,
        Integer numberOfParticipants
) {
}
