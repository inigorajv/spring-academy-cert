package com.example.cashcard.repository;

import com.example.cashcard.entity.CashCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CashCardRepository extends CrudRepository<CashCard, Long>, PagingAndSortingRepository<CashCard, Long> {

    Optional<CashCard> findByIdAndOwner(Long id, String owner);

    Page<CashCard> findByOwner(PageRequest pageRequest, String owner);

    boolean existsByIdAndOwner(Long id, String owner);
}
