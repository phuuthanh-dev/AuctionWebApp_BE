package vn.webapp.backend.auction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.webapp.backend.auction.enums.JewelryState;
import vn.webapp.backend.auction.model.Jewelry;
import vn.webapp.backend.auction.model.User;

import java.util.List;
import java.util.Optional;

public interface JewelryRepository extends JpaRepository<Jewelry, Integer> {
    @Query("SELECT j FROM Jewelry j WHERE j.user.username = :username")
    List<Jewelry> findJewelryByUsername(@Param("username") String username);

    List<Jewelry> findJewelryByCategoryId(Integer categoryId);

    List<Jewelry> getJewelriesByNameContaining(String name);

    Page<Jewelry> findByState(JewelryState jewelryState, Pageable pageable);
}
