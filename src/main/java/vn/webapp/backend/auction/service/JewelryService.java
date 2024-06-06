package vn.webapp.backend.auction.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.webapp.backend.auction.dto.SendJewelryFromUserRequest;
import vn.webapp.backend.auction.model.Jewelry;

import java.util.List;

public interface JewelryService {
    List<Jewelry> getAll();

    Jewelry getJewelryById(Integer id);

    List<Jewelry> getJewelryByUsername(String username);

    void deleteJewelry(Integer id);

    List<Jewelry> getJeweriesByCategoryId(Integer id);

    List<Jewelry> getJeweriesByNameContain(String key);

    Page<Jewelry> getAllJeweries(Pageable pageable);

    Page<Jewelry> getJewelriesInWaitList(Pageable pageable);

    Page<Jewelry> getJewelriesInHandOver(Pageable pageable);

    Page<Jewelry> getJewelriesByUsername(String username, Pageable pageable);

    Jewelry requestJewelry(SendJewelryFromUserRequest request);

    Jewelry getLatestJewelry();

}