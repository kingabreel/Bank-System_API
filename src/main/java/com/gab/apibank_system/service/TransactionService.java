package com.gab.apibank_system.service;

import com.gab.apibank_system.domain.model.Account;
import com.gab.apibank_system.domain.model.User;
import com.gab.apibank_system.domain.model.Wallet;
import com.gab.apibank_system.domain.response.TransactionResponse;
import com.gab.apibank_system.repository.UserRepository;
import com.gab.apibank_system.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public TransactionService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public TransactionResponse addMoney(BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        User user = getCurrentUser();
        Wallet wallet = user.getAccount().getWallet();

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        Account account =  user.getAccount();

        return new TransactionResponse(account.getAccountNumber(), account.getAgency().getNumber(), wallet.getBalance().doubleValue());
    }

    public TransactionResponse withdrawMoney(BigDecimal amount){
        User user = getCurrentUser();
        Wallet wallet = user.getAccount().getWallet();

        if (amount.compareTo(wallet.getBalance()) > 0) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
        Account account =  user.getAccount();

        return new TransactionResponse(account.getAccountNumber(), account.getAgency().getNumber(), wallet.getBalance().doubleValue());
    }

    public TransactionResponse transferMoney(UUID toUserId, BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        User fromUser = getCurrentUser();
        Wallet fromWallet = fromUser.getAccount().getWallet();

        if (amount.compareTo(fromWallet.getBalance()) > 0) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("Destination user not found."));

        Wallet toWallet = toUser.getAccount().getWallet();

        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(amount));

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        Account account =  fromUser.getAccount();

        return new TransactionResponse(account.getAccountNumber(), account.getAgency().getNumber(), account.getWallet().getBalance().doubleValue());
    }

    public TransactionResponse getWalletInfo() {
        User user = getCurrentUser();
        Wallet wallet = user.getAccount().getWallet();

        Account account =  user.getAccount();

        return new TransactionResponse(account.getAccountNumber(), account.getAgency().getNumber(), wallet.getBalance().doubleValue());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        String email = ((User) authentication.getPrincipal()).getEmail();

        return (User) userRepository.findByEmail(email);
    }
}
