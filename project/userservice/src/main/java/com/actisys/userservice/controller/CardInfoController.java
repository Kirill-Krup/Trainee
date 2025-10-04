package com.actisys.userservice.controller;


import com.actisys.userservice.dto.CardInfoDTO;
import com.actisys.userservice.dto.CreateCardInfoDTO;
import com.actisys.userservice.service.CardInfoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cards")
public class CardInfoController {

  private final CardInfoService cardInfoService;

  public CardInfoController(CardInfoService cardInfoService) {
    this.cardInfoService = cardInfoService;
  }

  @PostMapping
  public ResponseEntity<CardInfoDTO> createCard(
      @Valid @RequestBody CreateCardInfoDTO createCardInfoDTO) {
    CardInfoDTO createdCard = cardInfoService.createCard(createCardInfoDTO);
    return ResponseEntity.ok(createdCard);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CardInfoDTO> getCardById(@PathVariable Long id) {
    return cardInfoService.getCardInfoById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/ids")
  public ResponseEntity<List<CardInfoDTO>> getCardsByIds(@RequestParam List<Long> ids) {
    List<CardInfoDTO> cards = cardInfoService.getCardsByIds(ids);
    return ResponseEntity.ok(cards);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CardInfoDTO> updateCard(@PathVariable Long id,
      @Valid @RequestBody CardInfoDTO cardInfoDTO) {
    CardInfoDTO updatedCard = cardInfoService.updateCard(id, cardInfoDTO);
    return ResponseEntity.ok(updatedCard);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
    cardInfoService.deleteCard(id);
    return ResponseEntity.noContent().build();
  }
}