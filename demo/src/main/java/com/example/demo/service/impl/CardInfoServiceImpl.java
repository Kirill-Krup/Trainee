package com.example.demo.service.impl;

import com.example.demo.dto.CardInfoDTO;
import com.example.demo.dto.CreateCardInfoDTO;
import com.example.demo.exception.CardInfoNotFoundException;
import com.example.demo.mapper.CardInfoMapper;
import com.example.demo.model.CardInfo;
import com.example.demo.repository.CardInfoRepository;
import com.example.demo.service.CardInfoService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardInfoServiceImpl implements CardInfoService {
  private final CardInfoRepository cardInfoRepository;
  private final CardInfoMapper cardInfoMapper;

  public CardInfoServiceImpl(CardInfoRepository cardInfoRepository, CardInfoMapper cardInfoMapper) {
    this.cardInfoRepository = cardInfoRepository;
    this.cardInfoMapper = cardInfoMapper;
  }

  @Override
  public CardInfoDTO createCard(CreateCardInfoDTO createCardInfoDTO) {
    CardInfo entity = cardInfoMapper.toEntityForCreate(createCardInfoDTO);
    CardInfo savedCard = cardInfoRepository.save(entity);
    return cardInfoMapper.toDTO(savedCard);
  }

  @Override
  public Optional<CardInfoDTO> getCardInfoById(Long id) {
    return cardInfoRepository.findById(id).map(cardInfoMapper::toDTO);
  }

  @Override
  public List<CardInfoDTO> getCardsByIds(List<Long> ids){
    return cardInfoRepository.findByIdIn(ids).stream().map(cardInfoMapper::toDTO).collect(
        Collectors.toList());
  }


  @Transactional
  @Override
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
  @Override
  public void deleteCard(Long id) {
    if(!cardInfoRepository.existsById(id)) {
      throw new CardInfoNotFoundException(id);
    }
    cardInfoRepository.deleteById(id);
  }

}
