package com.actisys.apigateway.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "services")
@Getter
@Setter
public class ServiceProperties {

  private ServiceConfig user;
  private ServiceConfig auth;

  @Getter
  @Setter
  public static class ServiceConfig {
    private String url;
    private Endpoints endpoints;
  }

  @Getter
  @Setter
  public static class Endpoints {
    private String create;
    private String delete;
    private String register;
  }
}
