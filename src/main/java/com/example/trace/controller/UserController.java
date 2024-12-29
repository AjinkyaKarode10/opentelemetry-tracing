package com.example.trace.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.trace.dto.OnboardUserRequestDTO;
import com.example.trace.service.OnboardUserService;


@RestController()
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  @Autowired
  OnboardUserService onboardUserService;

  @PostMapping("/user/onboard")
  public ResponseEntity<String> onboardUser(
      @RequestBody final OnboardUserRequestDTO onboardUserRequestDTO) {
    log.info("Onboarding User Started : {} ", onboardUserRequestDTO.getUserId());
    onboardUserService.onboardUser(onboardUserRequestDTO);
    log.info(
        "Onboarding User Completed: {}",onboardUserRequestDTO.getUserId());
    return ResponseEntity.ok("Request Processed Successfully");
  }

}
