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

    public UserResponse configUser(User user) {
        WalletDTO walletDTO = createWallet();

        Account account = createAccount(user, walletDTO);

        user.setAccount(account);

        return ConversionService.userResponseFromEntity(userRepository.save(user));
    }

    private WalletDTO createWallet() {
        Wallet wallet = new Wallet();

        wallet.setCards(new ArrayList<>());
        wallet.setBalance(BigDecimal.ZERO);

        return ConversionService.walletDTOFromEntity(walletRepository.save(wallet));
    }

    private Account createAccount(User user, WalletDTO walletDTO) {
        AgencyDTO agency;
        if (MOCK_AGENCY_ID == null) {
            agency = saveMockAgency();
        } else {
            agency = mockAgency();
        }

        AccountDTO accountDTO = new AccountDTO(UUID.randomUUID(), ConversionService.userDTOFromEntity(user), agency, walletDTO);

        Account account = ConversionService.accountFromDTO(accountDTO);

        return accountRepository.save(account);
    }

    private AgencyDTO saveMockAgency() {
        if (MOCK_AGENCY_ID == null) {
            MOCK_AGENCY_ID = UUID.randomUUID();
        }

        Agency existingAgency = agencyRepository.findById(MOCK_AGENCY_ID).orElse(null);

        Agency agency;

        if (existingAgency == null) {
            agency = ConversionService.agencyFromDTO(mockAgency());
        } else {
            agency = existingAgency;
        }

        Agency savedAgency = agencyRepository.save(agency);

        return ConversionService.agencyDTOFromEntity(savedAgency);
    }

    private AgencyDTO mockAgency() {
        return new AgencyDTO(MOCK_AGENCY_ID, "MockAgency", "email@mail.com", "829999", null);
    }
}
