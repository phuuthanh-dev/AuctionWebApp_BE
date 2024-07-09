package vn.webapp.backend.auction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.webapp.backend.auction.enums.AuctionRegistrationState;
import vn.webapp.backend.auction.model.AuctionRegistration;

import java.util.List;
import java.util.Optional;

public interface AuctionRegistrationRepository extends JpaRepository<AuctionRegistration, Integer> {

    @Query("SELECT ar FROM AuctionRegistration ar JOIN FETCH ar.auction a WHERE a.id = :auctionId AND ar.auctionRegistrationState = :state")
    List<AuctionRegistration> findByAuctionIdAndValid(@Param("auctionId") Integer auctionId, @Param("state") AuctionRegistrationState state);

    @Query("SELECT ar FROM AuctionRegistration ar WHERE ar.auction.id = :auctionId AND ar.user.id = :userId AND ar.auctionRegistrationState = 'VALID'")
    Optional<AuctionRegistration> findByAuctionIdAndUserIdValid(@Param("userId") Integer userId, @Param("auctionId") Integer auctionId);

    @Query("SELECT ar FROM AuctionRegistration ar JOIN FETCH ar.user a WHERE a.id = :userId  AND (:auctionName IS NULL OR ar.auction.name LIKE %:auctionName%)")
    Page<AuctionRegistration> findByUserIdAndValid(@Param("userId") Integer userId,@Param("auctionName") String auctionName, Pageable pageable);

    @Query("SELECT ar FROM AuctionRegistration ar WHERE ar.user.id = :userId AND ar.auctionRegistrationState = 'VALID'")
    List<AuctionRegistration> findByUserIdValid(@Param("userId") Integer userId);

    @Query("SELECT SUM(ar.registrationFee) FROM AuctionRegistration ar")
    Double sumTotalRegistrationFee();

    @Query("SELECT COUNT(DISTINCT ar.user.id) FROM AuctionRegistration ar WHERE ar.auctionRegistrationState = 'VALID' AND MONTH(ar.registrationDate) = :month AND YEAR(ar.registrationDate) = :year")
    Long countDistinctUsersRegistered(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COUNT(ar) FROM AuctionRegistration ar WHERE ar.auction.id = :auctionId AND ar.auctionRegistrationState = 'VALID'")
    Integer countValidParticipantsByAuctionId(@Param("auctionId") Integer auctionId);

}
