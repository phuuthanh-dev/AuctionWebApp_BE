package vn.webapp.backend.auction.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.webapp.backend.auction.dto.RegisterAccountRequest;
import vn.webapp.backend.auction.dto.UserSpentDTO;
import vn.webapp.backend.auction.enums.AccountState;
import vn.webapp.backend.auction.enums.Role;
import vn.webapp.backend.auction.model.User;

import java.util.List;

public interface UserService {
    User findUserByUsernameOrEmail(String username);
    User getUserByUsername(String username);
    User getUserById(Integer id);
    User getUserByEmail(String email);
    List<User> getAll();
    List<User> getAllStaff();
    User registerStaff(RegisterAccountRequest request);
    void setAccountState(Integer id, String state);
    User updateUser(User user);
    Page<User> getMemberByFullNameContainingAndState(String fullName ,AccountState state, Pageable pageable);
    Page<User> getStaffByFullNameContainingAndRoleAndState(String fullName, Role role, AccountState state , Pageable page);
    User getLatestUserInAuctionHistoryByAuctionId(Integer auctionId);
    List<UserSpentDTO> getMostSpentUser();
    Page<User> getUsersUnVerifyByFullNameContainingAndState(String fullName, AccountState state, Pageable pageable);
 }
