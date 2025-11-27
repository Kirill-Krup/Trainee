package com.actisys.paymentservice.client.impl;

import com.actisys.paymentservice.client.RandomNumberClient;
import com.actisys.paymentservice.exception.RandomApiException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
@Slf4j
@RequiredArgsConstructor
public class RandomNumberClientApi implements RandomNumberClient {

  private final RestTemplate restTemplate;
  @Value("${random.api.url}")
  private String randomApiUrl;

  @Override
  public int getRandomNumber(){
    try {
      String response = restTemplate.getForObject(
          randomApiUrl + "/api/v1.0/random?min=1&max=100",
          String.class);

      if(response != null){
        String number = response.replace("[", "").replace("]", "").trim();
        return Integer.parseInt(number);
      }
    } catch(Exception e) {
      log.error("Error occurred in randomApi{}", e.getMessage(), e);
      throw new RandomApiException();
    }
    return new Random().nextInt(100) + 1;
  }
}

