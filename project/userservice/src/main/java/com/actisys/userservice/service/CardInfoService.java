package com.actisys.userservice.service;

import com.actisys.userservice.dto.CardInfoDTO;
import com.actisys.userservice.dto.CreateCardInfoDTO;
import java.util.List;
import java.util.Optional;


public interface CardInfoService {

  CardInfoDTO createCard(CreateCardInfoDTO createCardInfoDTO);

  Optional<CardInfoDTO> getCardInfoById(Long id);

  List<CardInfoDTO> getCardsByIds(List<Long> ids);

  CardInfoDTO updateCard(Long id, CardInfoDTO updated);

  void deleteCard(Long id);
}
