package com.example.demo.Repository;

import com.example.demo.Model.CardInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {

  List<CardInfo> findByIdIn(List<Long> ids);
}
