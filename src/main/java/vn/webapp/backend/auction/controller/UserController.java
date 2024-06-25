package vn.webapp.backend.auction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.webapp.backend.auction.dto.ChangePasswordRequest;
import vn.webapp.backend.auction.dto.RegisterAccountRequest;
import vn.webapp.backend.auction.dto.UserSpentDTO;
import vn.webapp.backend.auction.enums.AccountState;
import vn.webapp.backend.auction.enums.Role;
import vn.webapp.backend.auction.model.User;
import vn.webapp.backend.auction.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/member")
    public ResponseEntity<Page<User>> getMember(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) AccountState state,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        Sort.Direction direction = (sortOrder.equalsIgnoreCase("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        return ResponseEntity.ok(userService.getMemberByFullNameContainingAndState(fullName, state, pageable));
    }

    @GetMapping("/staff")
    public ResponseEntity<Page<User>> getStaff(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) AccountState state,
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        Sort.Direction direction = (sortOrder.equalsIgnoreCase("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        return ResponseEntity.ok(userService.getStaffByFullNameContainingAndRoleAndState(fullName, role, state, pageable));
    }

    @PutMapping("/set-state/{id}")
    public ResponseEntity<User> setState(@PathVariable Integer id, @RequestBody AccountState state) {
        userService.setAccountState(id, state);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/staff/register")
    public ResponseEntity<User> addStaff(
            @RequestBody RegisterAccountRequest user) {
        return ResponseEntity.ok(userService.registerStaff(user));
    }

    @PutMapping()
    public ResponseEntity<User> updateProfileUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @PutMapping("/reject-verify/{id}")
    public ResponseEntity<User> rejectVerifyUser(@PathVariable Integer id) {
        userService.rejectVerifyUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-winner-auction/{auctionId}")
    public ResponseEntity<User> getLatestUserInAuctionHistoryByAuctionId(@PathVariable Integer auctionId) {
        return ResponseEntity.ok(userService.getLatestUserInAuctionHistoryByAuctionId(auctionId));
    }

    @GetMapping("/get-user-registration/{auctionId}")
    public ResponseEntity<List<User>> getUserRegistrationAuctionByAuction(@PathVariable Integer auctionId) {
        return ResponseEntity.ok(userService.getUserRegistrationAuctionByAuctionId(auctionId));
    }

    @GetMapping("/get-top-spent-user")
    public ResponseEntity<List<UserSpentDTO>> getTopUser() {
        return ResponseEntity.ok(userService.getMostSpentUser());
    }

    @GetMapping("/get-by-state")
    public ResponseEntity<Page<User>> getUnVerifyUsers(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) AccountState state,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        Sort.Direction direction = (sortOrder.equalsIgnoreCase("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        return ResponseEntity.ok(userService.getUsersUnVerifyByFullNameContainingAndState(fullName, state, pageable));
    }
}
