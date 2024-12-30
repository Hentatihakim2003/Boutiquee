package com.ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String city;

    private String postalCode;

    private String country;

    // Relation Many-to-One avec la table User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Clé étrangère
    private Users user;
}
