package com.gab.apibank_system.service;

import com.gab.apibank_system.domain.dto.AccountDTO;
import com.gab.apibank_system.domain.dto.AgencyDTO;
import com.gab.apibank_system.domain.dto.WalletDTO;
import com.gab.apibank_system.domain.model.Account;
import com.gab.apibank_system.domain.model.Agency;
import com.gab.apibank_system.domain.model.User;
import com.gab.apibank_system.domain.model.Wallet;
import com.gab.apibank_system.domain.request.UserRequest;
import com.gab.apibank_system.domain.response.UserResponse;
import com.gab.apibank_system.repository.AccountRepository;
import com.gab.apibank_system.repository.AgencyRepository;
import com.gab.apibank_system.repository.UserRepository;
import com.gab.apibank_system.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private UUID MOCK_AGENCY_ID = null;

    private final AccountRepository accountRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;

    @Autowired
    public UserService(AccountRepository accountRepository, WalletRepository walletRepository, UserRepository userRepository, AgencyRepository agencyRepository) {
        this.accountRepository = accountRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.agencyRepository = agencyRepository;
    }

    @Transactional
    public UserResponse registerUser(UserRequest data, String encryptedPassword) {
        User user = User.fromDto(data, encryptedPassword);
        user = userRepository.save(user);

        Wallet wallet = createWallet();
        Account account = createAccount(user, wallet);

        user.setAccount(account);
        user = userRepository.save(user);

        return ConversionService.userResponseFromEntity(user);
    }

    private Wallet createWallet() {
        Wallet wallet = new Wallet();

        wallet.setCards(new ArrayList<>());
        wallet.setBalance(BigDecimal.ZERO);

        return walletRepository.save(wallet);
    }

    private Account createAccount(User user, Wallet wallet) {
        Agency agency = saveMockAgency();

        AccountDTO accountDTO = new AccountDTO(UUID.randomUUID(), null, null, null);

        Account account = ConversionService.accountFromDTO(accountDTO);
        account.setOwner(user);
        account.setWallet(wallet);
        account.setAgency(agency);

        return accountRepository.save(account);
    }

    private Agency saveMockAgency() {
        if (MOCK_AGENCY_ID == null) {
            MOCK_AGENCY_ID = UUID.randomUUID();
        }

        Optional<Agency> optionalAgency = agencyRepository.findById(MOCK_AGENCY_ID);

        if (optionalAgency.isPresent()) {
            return optionalAgency.get();
        }

        Agency agency = ConversionService.agencyFromDTO(mockAgency());

        agency.setAccounts(new ArrayList<>());
        Agency savedAgency = agencyRepository.save(agency);
        MOCK_AGENCY_ID = savedAgency.getId();

        return savedAgency;
    }

    private AgencyDTO mockAgency() {
        return new AgencyDTO(null, "MockAgency", "email@mail.com", "829999", null);
    }
}
