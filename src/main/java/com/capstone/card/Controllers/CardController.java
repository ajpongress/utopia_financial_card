package com.capstone.card.Controllers;

import com.capstone.card.Services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

    // ----------------------------------------------------------------------------------
    // --                                  SETUP                                       --
    // ----------------------------------------------------------------------------------

    @Autowired
    CardService cardService;



    // ----------------------------------------------------------------------------------
    // --                               MAPPINGS                                       --
    // ----------------------------------------------------------------------------------

    // Generate card numbers
    @GetMapping("/cards")
    public ResponseEntity<String> generateCardsAPI(@RequestParam String source, @RequestParam String destination) {

        return cardService.generateCards(source, destination);
    }
}
