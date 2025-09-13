package com.example.demo.Service;

import com.example.demo.DTO.CardInfoDTO;
import com.example.demo.Exception.CardInfoNotFoundException;
import com.example.demo.Mapper.CardInfoMapper;
import com.example.demo.Model.CardInfo;
import com.example.demo.Repository.CardInfoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardInfoService {

  private final CardInfoRepository cardInfoRepository;
  private final CardInfoMapper cardInfoMapper;

  public CardInfoService(CardInfoRepository cardInfoRepository, CardInfoMapper cardInfoMapper) {
    this.cardInfoRepository = cardInfoRepository;
    this.cardInfoMapper = cardInfoMapper;
  }

  public CardInfoDTO createCard(CardInfoDTO cardInfo) {
    CardInfo entity = cardInfoMapper.toEntity(cardInfo);
    CardInfo savedCard = cardInfoRepository.save(entity);
    return cardInfoMapper.toDTO(savedCard);
  }

  public Optional<CardInfoDTO> getCardInfoById(Long id) {
    return cardInfoRepository.findById(id).map(cardInfoMapper::toDTO);
  }

  public List<CardInfoDTO> getCardsByIds(List<Long> ids){
    return cardInfoRepository.findByIdIn(ids).stream().map(cardInfoMapper::toDTO).collect(Collectors.toList());
  }

  @Transactional
  public CardInfoDTO updateCard(Long id, CardInfoDTO updated) {
    CardInfo cardInfo = cardInfoRepository.findById(id)
        .map(card -> {
          card.setNumber(updated.getNumber());
          card.setHolder(updated.getHolder());
          card.setExpirationDate(updated.getExpirationDate());
          return card;
        })
        .orElseThrow(() -> new CardInfoNotFoundException(id));
    CardInfo savedCard = cardInfoRepository.save(cardInfo);
    return cardInfoMapper.toDTO(savedCard);
  }

  @Transactional
  public void deleteCard(Long id) {
    if(!cardInfoRepository.existsById(id)) {
      throw new CardInfoNotFoundException(id);
    }
    cardInfoRepository.deleteById(id);
  }

}
