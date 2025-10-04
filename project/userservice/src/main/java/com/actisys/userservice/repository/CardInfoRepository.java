package com.actisys.userservice.repository;

import com.actisys.userservice.model.CardInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {

  List<CardInfo> findByIdIn(List<Long> ids);
}
