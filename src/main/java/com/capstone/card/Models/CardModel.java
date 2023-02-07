package com.capstone.card.Models;

import lombok.Data;

@Data
public class CardModel {

    private long id;
    private long userID; // CSV - User (index 0)
    private long cardID; // CSV - CardModel (index 1)
    private String cardNumber;

}
