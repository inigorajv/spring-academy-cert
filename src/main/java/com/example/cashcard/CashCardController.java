package com.example.cashcard;

import com.example.cashcard.entity.CashCard;
import com.example.cashcard.repository.CashCardRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("v1/cashcards")
public class CashCardController {

    private final CashCardRepository cashCardRepository;

    CashCardController(CashCardRepository cashCardRepository){
        this.cashCardRepository = cashCardRepository;

    }

    @GetMapping("/{id}")
    private ResponseEntity<CashCard> getCashCardById(@PathVariable Long id, Principal principal){
        Optional<CashCard> cashCard = cashCardRepository.findByIdAndOwner(id, principal.getName());
        if(cashCard.isPresent()){
            return ResponseEntity.ok(cashCard.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard cashCard, UriComponentsBuilder ucb, Principal principal){
        CashCard cashCardData = cashCardRepository.save(new CashCard(null, cashCard.amount(), principal.getName()));
        URI locationOfCashCard = ucb.path("v1/cashcards/{id}").buildAndExpand(cashCardData.id()).toUri();
        return ResponseEntity.created(locationOfCashCard).build();
    }


    @GetMapping
    private ResponseEntity<Iterable<CashCard>> getAllCashCards(Pageable pageable, Principal principal){
        PageRequest pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount")));
        return ResponseEntity.ok(cashCardRepository.findByOwner(pageRequest, principal.getName()).getContent());
    }
}
