package com.vinicius.finance_api.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "value")
    private Double value;

    @Column(name = "type")
    private TransactionType type;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "description")
    private String description;

}
