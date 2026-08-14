package com.example.financialmotoboy.entity;

import jakarta.persistence.Entity;

@Entity
public class Motoboy extends Person {

    private String motorcycle;

    private String plate;

}