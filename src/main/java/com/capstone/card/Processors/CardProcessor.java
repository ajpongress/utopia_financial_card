package com.capstone.card.Processors;

import com.capstone.card.Models.CardModel;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@StepScope
@Component
@Slf4j
public class CardProcessor implements ItemProcessor<CardModel, CardModel> {

    // ----------------------------------------------------------------------------------
    // --                                  SETUP                                       --
    // ----------------------------------------------------------------------------------

    // Tracks userID as primary Key and attaches card number to the key
    private final HashMap<Long, String> cardMap = new HashMap<>();

    // Useful for additional jobs or steps
    public void clearMap() {
        cardMap.clear();
    }

    private final Faker faker = new Faker();

    private static long cardCounter = 0;

    // ----------------------------------------------------------------------------------
    // --                                METHODS                                       --
    // ----------------------------------------------------------------------------------

    @Override
    public CardModel process(CardModel cardModel) {

        long userId = cardModel.getUserID();
        long cardId = cardModel.getCardID();

        synchronized(this) {

            // If user is already accessed
            if(cardMap.containsKey(userId)) {

                String cardIds = cardMap.get(userId); // Copy the current string of cards from the key
                String[] cardIdArray = cardIds.split(","); // Convert to string array and delimit by comma

                // Checks if cardModel has already been added to string array
                for (String cardIdString : cardIdArray) {
                    if (cardId == Long.parseLong(cardIdString)) {
                        return null;
                    }
                }

                cardIds = cardIds + "," + cardId; // Add cardModel to end of string array
                cardMap.put(userId, cardIds); // Update string of cards to key in hashmap

                // Generate cardModel number, increment counter and return cardModel
                cardModel.setCardNumber(faker.finance().creditCard());
                cardCounter++;
                cardModel.setId(cardCounter);
                log.info(cardModel.toString());
                return cardModel;

            } else { // User hasn't been seen yet
                cardMap.put(userId, "" + cardId); // Create empty string and add first cardModel to user

                // Generate cardModel number, increment counter and return cardModel
                cardModel.setCardNumber(faker.finance().creditCard());
                cardCounter++;
                cardModel.setId(cardCounter);
                log.info(cardModel.toString());
                return cardModel;
            }

        }


    }
}
