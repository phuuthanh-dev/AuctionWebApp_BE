package vn.webapp.backend.auction.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.webapp.backend.auction.dto.UserTransactionResponse;
import vn.webapp.backend.auction.enums.PaymentMethod;
import vn.webapp.backend.auction.enums.TransactionState;
import vn.webapp.backend.auction.enums.TransactionType;
import vn.webapp.backend.auction.exception.ResourceNotFoundException;
import vn.webapp.backend.auction.model.ErrorMessages;
import vn.webapp.backend.auction.model.Transaction;
import vn.webapp.backend.auction.model.User;
import vn.webapp.backend.auction.repository.AuctionHistoryRepository;
import vn.webapp.backend.auction.repository.AuctionRepository;
import vn.webapp.backend.auction.repository.TransactionRepository;
import vn.webapp.backend.auction.repository.UserRepository;

import java.util.List;
@Transactional
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AuctionHistoryRepository auctionHistoryRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.TRANSACTION_NOT_FOUND));
    }

    @Override
    public UserTransactionResponse getTransactionsDashboardByUsername(String username) {
        Integer numberRegistration =  transactionRepository.getCountTransactionsRegistrationByUsername(username);
        Double totalPriceJewelryWonByUsername =  transactionRepository.getTotalPriceJewelryWonByUsername(username);
        Integer totalJewelryWon = transactionRepository.getTotalJewelryWon(username);
        Integer totalBid = auctionHistoryRepository.getTotalBidByUsername(username);
        return UserTransactionResponse.builder()
                .numberTransactionsRequest(numberRegistration)
                .totalPriceJewelryWonByUsername(totalPriceJewelryWonByUsername)
                .totalJewelryWon(totalJewelryWon)
                .totalBid(totalBid)
                .build();
    }

    @Override
    public Page<Transaction> getTransactionsByUsername (String username, Pageable pageable) {
        return transactionRepository.findTransactionsByUsername(username, pageable);
    }

    @Override
    public Page<Transaction> getTransactionByTypeAndState(TransactionType typename, TransactionState state, Pageable pageable) {
        Page<Transaction> transactionsList = transactionRepository.findTransactionByTypeAndState(typename, state, pageable);
        if (transactionsList.isEmpty()) {
            throw new ResourceNotFoundException("Type'" + typename + "' does not have any transaction items.");
        }
        return transactionsList;
    }

    @Override
    public Page<Transaction> getTransactionHandover(TransactionType typename, Pageable pageable) {
        return transactionRepository.findTransactionHandover(typename, pageable);
    }

    @Override
    public void setTransactionState(Integer id, String state) {
        var existingAuction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.AUCTION_NOT_FOUND));
        existingAuction.setState(TransactionState.valueOf(state));
    }

    @Override
    public void setTransactionMethod(Integer id, String method) {
        var existingAuction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.AUCTION_NOT_FOUND));
        existingAuction.setPaymentMethod(PaymentMethod.valueOf(method));
    }

    @Override
    public User createTransactionForWinner(Integer auctionId) {
        var auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.AUCTION_NOT_FOUND));

        var userWin = userRepository.findLatestUserInAuctionHistoryByAuctionId(auction.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_WINNER_NOT_FOUND));

        Transaction transaction = Transaction.builder()
                .user(userWin)
                .auction(auction)
                .state(TransactionState.PENDING)
                .totalPrice(auction.getLastPrice())
                .feesIncurred(0.0)
                .createDate(auction.getEndDate())
                .type(TransactionType.PAYMENT_TO_WINNER)
                .build();

        transactionRepository.save(transaction);

        return userWin;
    }
}
