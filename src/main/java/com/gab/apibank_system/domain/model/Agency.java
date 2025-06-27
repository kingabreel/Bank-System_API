package com.gab.apibank_system.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "agency")
@Entity(name = "agency")
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    private Long version;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private List<Employee> employees;*/

    private String name;

    private String email;

    private String number;

    @OneToMany(mappedBy = "agency", fetch = FetchType.LAZY)
    private List<Account> accounts;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
