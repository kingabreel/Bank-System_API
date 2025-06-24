package com.gab.apibank_system.service;

import com.gab.apibank_system.domain.dto.*;
import com.gab.apibank_system.domain.model.*;
import com.gab.apibank_system.domain.request.UserRequest;
import com.gab.apibank_system.domain.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConversionService {

    protected static UserDTO userDTOFromRequest(UserRequest userRequest) {
        return new UserDTO(UUID.randomUUID(),
                userRequest.firstName(),
                userRequest.lastName(),
                userRequest.email(),
                userRequest.password(),
                userRequest.cellphone(),
                null);
    }

    protected static UserDTO userDTOFromEntity(User user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getCellphone(), null);
    }

    protected static UserResponse userResponseFromEntity(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getCellphone());
    }

    protected static User userFromDTO(UserDTO userDTO) {
        User user = new User();

        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password());
        user.setCellphone(userDTO.cellphone());

        if (userDTO.address() == null) {
            user.setAddress(null);
            return user;
        }

        Address address = addressFromDTO(userDTO.address());

        user.setAddress(address);

        return user;
    }

    protected static Address addressFromDTO(AddressDTO addressDTO) {
        Address address = new Address();

        address.setDistrict(addressDTO.district());
        address.setStreet(addressDTO.street());
        address.setZipCode(addressDTO.zipCode());
        address.setCity(addressDTO.city());
        address.setState(addressDTO.state());
        address.setCountry(addressDTO.country());

        return address;
    }

    protected static Account accountFromDTO(AccountDTO accountDTO) {
        Account account = new Account();

        account.setOwner(userFromDTO(accountDTO.owner()));
        account.setAccountNumber(UUID.randomUUID().toString());

        Agency agency = agencyFromDTO(accountDTO.agency());

        account.setAgency(agency);

        Wallet wallet = walletFromDTO(accountDTO.wallet());

        account.setWallet(wallet);

        return account;
    }

    protected static AccountDTO accountDTOFromEntity(Account account) {
        UserDTO userDTO = userDTOFromEntity(account.getOwner());
        AgencyDTO agencyDTO = agencyDTOFromEntity(account.getAgency());
        WalletDTO walletDTO = walletDTOFromEntity(account.getWallet());

        return new AccountDTO(account.getId(), userDTO, agencyDTO, walletDTO);
    }

    protected static AgencyDTO agencyDTOFromEntity(Agency agency) {
        return new AgencyDTO(agency.getId(), agency.getName(), agency.getEmail(), agency.getNumber(), null);
    }

    protected static Agency agencyFromDTO(AgencyDTO agencyDTO) {
        Agency agency = new Agency();

        if (agencyDTO.id() != null) {
            agency.setId(agencyDTO.id());
        }

        agency.setName(agencyDTO.name());
        agency.setEmail(agencyDTO.email());
        agency.setNumber(agencyDTO.number());

        if (agencyDTO.address() == null) {
            agency.setAddress(null);
            return agency;
        }
        Address address = addressFromDTO(agencyDTO.address());

        agency.setAddress(address);

        return agency;
    }

    protected static WalletDTO walletDTOFromEntity(Wallet wallet) {
        return new WalletDTO(wallet.getId(), wallet.getBalance(), wallet.getCards());
    }

    protected static Wallet walletFromDTO(WalletDTO walletDTO) {
        Wallet wallet = new Wallet();

        wallet.setBalance(walletDTO.balance());
        wallet.setCards(walletDTO.cards());

        return wallet;
    }
}
