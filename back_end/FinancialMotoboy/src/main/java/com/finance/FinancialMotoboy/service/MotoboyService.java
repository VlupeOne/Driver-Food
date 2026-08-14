package com.finance.FinancialMotoboy.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.finance.FinancialMotoboy.controller.dtos.AddressResponse;
import com.finance.FinancialMotoboy.entities.Address;
import com.finance.FinancialMotoboy.entities.Motoboy;
import com.finance.FinancialMotoboy.entities.User;
import com.finance.FinancialMotoboy.repositories.AddressRepository;
import com.finance.FinancialMotoboy.repositories.MotoboyRepository;

@Service
public class MotoboyService {
    private final UserService userService;
    private final MotoboyRepository repository;
    private final AddressRepository addressRepository;

    public MotoboyService(MotoboyRepository repository, AddressRepository addressRepository, UserService userService) {
        this.repository = repository;
        this.addressRepository = addressRepository;
        this.userService = userService;
    }

    public Motoboy save(Motoboy motoboy){
        return repository.save(motoboy);
    }

    public void saveAddress(Authentication authentication, AddressResponse addresResponse) {

        User user = userService.getAuthenticated(authentication);

        Motoboy motoboy = (Motoboy) user;

        Address address = new Address(
            null,
            addresResponse.cep(),
            addresResponse.logradouro(),
            addresResponse.complemento(),
            addresResponse.bairro(),
            addresResponse.localidade(),
            addresResponse.uf(),
            motoboy
        );

        addressRepository.save(address);

        motoboy.setAddress(address);
        repository.save(motoboy);

    }

    public AddressResponse updateAddress(Authentication authentication, AddressResponse response) {

        User user = userService.getAuthenticated(authentication);

        Motoboy motoboy = (Motoboy) user;

        Address address = motoboy.getAddress();

        if (address == null) {
            throw new RuntimeException("Endereço não encontrado");
        }

        address.setCep(response.cep());
        address.setLogradouro(response.logradouro());
        address.setComplemento(response.complemento());
        address.setBairro(response.bairro());
        address.setLocalidade(response.localidade());
        address.setUf(response.uf());

        addressRepository.save(address);

        return new AddressResponse(
            address.getCep(),
            address.getLogradouro(),
            address.getComplemento(),
            address.getBairro(),
            address.getLocalidade(),
            address.getUf()
        );
    }

}
