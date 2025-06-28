package com.gab.apibank_system.service;

import com.gab.apibank_system.domain.model.Account;
import com.gab.apibank_system.domain.model.Pix;
import com.gab.apibank_system.domain.model.PixKey;
import com.gab.apibank_system.domain.model.User;
import com.gab.apibank_system.domain.request.PixRequest;
import com.gab.apibank_system.domain.request.PixTransactionRequest;
import com.gab.apibank_system.domain.response.PixTransactionResponse;
import com.gab.apibank_system.repository.AccountRepository;
import com.gab.apibank_system.repository.PixRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PixService {
    private final PixRepository pixRepository;
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;

    public PixService(PixRepository pixRepository, TransactionService transactionService, AccountRepository accountRepository) {
        this.pixRepository = pixRepository;
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
    }


    public List<PixKey> getPixKeys(Account account) {
        return account.getPix().getKeys();
    }

    public Account addPix(PixRequest pix, Account account) {
        PixKey pixKey = new PixKey();
        pixKey.setKeyType(pix.pixType());
        pixKey.setKeyValue(pix.pixKey());

        if (account.getPix().checkIfCanAddKey(pix.pixType())) {
            account.getPix().getKeys().add(pixKey);
            return account;
        } else {
            throw new RuntimeException("Cannot add Pix, you already have this key type");
        }
    }

    public PixTransactionResponse transaction(PixTransactionRequest pix, Account user) {
        Optional<Pix> receiverPix = pixRepository.findByKeyValue(pix.pixKey());

        if (receiverPix.isEmpty()) {
            throw new EntityNotFoundException("Pix not found");
        }

        Account receiverAccount =  receiverPix.get().getAccount();

        user.getWallet().removeMoney(pix.amount());
        receiverAccount.getWallet().addMoney(pix.amount());

        accountRepository.save(user);
        accountRepository.save(receiverAccount);

        return new PixTransactionResponse(UUID.randomUUID().toString(), pix.pixKey(),
                user.getOwner().getFirstName() + user.getOwner().getLastName(),
                receiverAccount.getOwner().getFirstName() + receiverAccount.getOwner().getLastName(),
                pix.amount()
        );
    }

    public User getUserCurrent() {
        return this.transactionService.getCurrentUser();
    }
}
