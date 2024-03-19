package com.example.cashcard;

import com.example.cashcard.entity.CashCard;
import com.example.cashcard.repository.CashCardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("v1/cashcards")
public class CashCardController {

    private CashCardRepository cashCardRepository;

    CashCardController(CashCardRepository cashCardRepository){
        this.cashCardRepository = cashCardRepository;

    }

    @GetMapping("/{id}")
    ResponseEntity<CashCard> getCashCardById(@PathVariable Long id){
        Optional<CashCard> cashCard = cashCardRepository.findById(id);
        if(cashCard.isPresent()){
            return ResponseEntity.ok(cashCard.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
