package com.capstone.card;

import com.capstone.card.Models.CardModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CardApplicationTests {

    @Test
    void contextLoads() {
    }

    // ----------------------------------------------------------------------------------
    // --                             MODEL TESTING                                    --
    // ----------------------------------------------------------------------------------

    @Test
    public void creates_card_id_1_userid_8_cardid_2_cardnumber_5555_5555_5555_5555() throws ClassNotFoundException {

        CardModel cardModel = new CardModel();

        cardModel.setId(1);
        cardModel.setUserID(8);
        cardModel.setCardID(2);
        cardModel.setCardNumber("5555-5555-5555-5555");

        assertEquals(1, cardModel.getId());
        assertEquals(8, cardModel.getUserID());
        assertEquals(2, cardModel.getCardID());
        assertEquals("5555-5555-5555-5555", cardModel.getCardNumber());
    }

}
