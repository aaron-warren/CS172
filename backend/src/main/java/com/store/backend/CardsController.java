package com.store.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Imports all DB Entities
import com.store.backend.DBConnections.Account.*;
import com.store.backend.DBConnections.CardInfo.*;
import com.store.backend.DBConnections.*;

@RestController
@CrossOrigin(origins = "http://localhost:3001")
public class CardsController {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private CardInfoRepo cardRepo;

    @Autowired
    private HoldsRepo holdRepo;

    @GetMapping("/cards")
    public ArrayList<CardInfo> getCards(
    @RequestParam Integer id
    ){
        Iterable<Holds> allRel = holdRepo.findAllByAccountId(id);
        ArrayList<CardInfo> allCards = new ArrayList<>();

        for(Holds holds : allRel){
            allCards.add(cardRepo.findById(holds.getCardId()).get());
        }

        return allCards;
    }

    @GetMapping("cards/add")
    public CardInfo addCard(
    @RequestParam Integer id,
    @RequestParam String CardNumber,
    @RequestParam String cardHolder,
    @RequestParam String cvv,
    @RequestParam String Zip,
    @RequestParam String ExpMonth,
    @RequestParam String ExpYear
    ){
        Optional<Account> getAccount = accountRepo.findById(id);
        Account account;

        try{
            account = getAccount.get();
        } catch (NoSuchElementException e){
            return null;
        }
        
        CardInfo newCard = new CardInfo(cardHolder, cvv, Zip, CardNumber, ExpMonth, ExpYear);
        Holds newRel = new Holds(newCard, account);

        cardRepo.save(newCard);
        holdRepo.save(newRel);

        return newCard;
    }

    @GetMapping("/cards/remove")
    public Map<String, String> removeCard(
    @RequestParam Integer id,
    @RequestParam Integer cardID
    ){
        Map<String, String> newMap = new HashMap<>();
        try{
            Holds delHolds = holdRepo.findByAccountIdAndCardId(id, cardID).get();
            CardInfo delCard = cardRepo.findById(cardID).get();

            holdRepo.delete(delHolds);
            cardRepo.delete(delCard);

            newMap.put("result", "success");
            return newMap;
        } catch (NoSuchElementException e){
            newMap.put("result", "failure");
            return newMap;
        }
    }
}