package com.example.demo.Controller;

import com.example.demo.DTO.CardInfoDTO;
import com.example.demo.Service.CardInfoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
public class CardInfoController {

  private final CardInfoService cardInfoService;

  CardInfoController(CardInfoService cardInfoService) {
    this.cardInfoService = cardInfoService;
  }

  @PostMapping
  public ResponseEntity<CardInfoDTO> createCard(@Valid @RequestBody CardInfoDTO cardInfoDTO) {
    CardInfoDTO createdCard = cardInfoService.createCard(cardInfoDTO);
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