package vn.webapp.backend.auction.dto;

import vn.webapp.backend.auction.model.Role;

public record RegisterRequest(
        String firstName,
        String lastName,
        String username,
        String password,
        String email,
        String address,
        String province,
        String city,
        String yob,
        String phone,
        String CCCD,
        Role role) {
}
