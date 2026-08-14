package com.finance.FinancialMotoboy.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.finance.FinancialMotoboy.controller.dtos.AddressResponse;
import com.finance.FinancialMotoboy.controller.dtos.DefaultUserResponse;
import com.finance.FinancialMotoboy.controller.dtos.MotoboyResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Motoboy extends User {

    private String motorcycle;
    private String plate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(
        mappedBy = "motoboy",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<DailyControls> dailyControls = new ArrayList<>();

    @Override
    public DefaultUserResponse toResponse(){

        return new MotoboyResponse(
            getName(),
            getEmail(),
            motorcycle,
            plate,
            address != null
                ? new AddressResponse(
                    address.getCep(),
                    address.getLogradouro(),
                    address.getComplemento(),
                    address.getBairro(),
                    address.getLocalidade(),
                    address.getUf()
                )
                : null,
            getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null)
        );
    }

    public Motoboy(UUID id, String name, String email, String password, String cpf, LocalDate birthDate, String motorcycle, String plate) {
        super(id, name, email, password, cpf, birthDate);
        this.motorcycle = motorcycle;
        this.plate = plate;
    }

    public Motoboy(UUID id, String name, String email, String password, String cpf, LocalDate birthDate,
        String motorcycle, String plate, Address address) {
        super(id, name, email, password, cpf, birthDate);
        this.motorcycle = motorcycle;
        this.plate = plate;
        this.address = address;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
            new SimpleGrantedAuthority("ROLE_MOTOBOY")
        );
    }

}