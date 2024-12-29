package com.example.trace.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.trace.dto.OnboardUserRequestDTO;

@Service
public class OnboardUserService {

  private static final Logger log = LoggerFactory.getLogger(OnboardUserService.class);

  public void onboardUser(OnboardUserRequestDTO onboardUserRequestDTO) {
    log.info("User Onboard request SUCCESS : {} - {}", onboardUserRequestDTO.getUserId(), LocalDateTime.now().toString().substring(0, 19));
  }
}
