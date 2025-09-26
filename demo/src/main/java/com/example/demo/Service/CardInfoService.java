package com.example.demo.Service;

import com.example.demo.Model.CardInfo;
import com.example.demo.Repository.CardInfoRepository;
import java.util.List;
import java.util.Optional;
import javax.smartcardio.Card;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardInfoService {

  private CardInfoRepository cardInfoRepository;

  public CardInfoService(CardInfoRepository cardInfoRepository) {
    this.cardInfoRepository = cardInfoRepository;
  }

  public CardInfo createCard(CardInfo cardInfo) {
    return cardInfoRepository.save(cardInfo);
  }

  public Optional<CardInfo> getCardInfoById(Long id) {
    return cardInfoRepository.findById(id);
  }

  public List<CardInfo> getCardsByIds(List<Long> ids){
    return cardInfoRepository.findByIdIn(ids);
  }

  @Transactional
  public CardInfo updateCard(Long id, CardInfo updated) {
    return cardInfoRepository.findById(id)
        .map(card -> {
          card.setNumber(updated.getNumber());
          card.setHolder(updated.getHolder());
          card.setExpirationDate(updated.getExpirationDate());
          return cardInfoRepository.save(card);
        })
        .orElseThrow(() -> new RuntimeException("Card " + id + " not found"));
  }

  @Transactional
  public void deleteCard(Long id) {
    cardInfoRepository.deleteById(id);
  }
}
