package com.example.demo.service;

import com.example.demo.dto.CardInfoDTO;
import com.example.demo.dto.CreateCardInfoDTO;
import com.example.demo.exception.CardInfoNotFoundException;
import com.example.demo.mapper.CardInfoMapper;
import com.example.demo.model.CardInfo;
import com.example.demo.repository.CardInfoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface CardInfoService {

  CardInfoDTO createCard(CreateCardInfoDTO createCardInfoDTO);

  Optional<CardInfoDTO> getCardInfoById(Long id);

  List<CardInfoDTO> getCardsByIds(List<Long> ids);

  CardInfoDTO updateCard(Long id, CardInfoDTO updated);

  void deleteCard(Long id);
}
