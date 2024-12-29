/******************************************************************************
 *                                                                            *
 * Copyright (c) 2024 by ACI Worldwide Inc.                                   *
 * All rights reserved.                                                       *
 *                                                                            *
 * This software is the confidential and proprietary information of ACI       *
 * Worldwide Inc ("Confidential Information"). You shall not disclose such    *
 * Confidential Information and shall use it only in accordance with the      *
 * terms of the license agreement you entered with ACI Worldwide Inc.         *
 ******************************************************************************/

package com.example.trace.logging;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;

@Component
public class SessionCorrelationIdFilter implements Filter {

  public static final String CORRELATION_ID_LOG_VAR_NAME = "correlationIdNew";
  private static final String CORRELATION_ID_HEADER_NAME = "B-App-Correlation-ID";

  @Autowired
  private TracingService tracingService;

  @Override
  public void doFilter(
      final ServletRequest servletRequest,
      final ServletResponse servletResponse,
      final FilterChain filterChain)
      throws IOException, ServletException {
    val httpServletRequest = (HttpServletRequest) servletRequest;
    val correlationId = httpServletRequest.getHeader(CORRELATION_ID_HEADER_NAME);
    try {
      if (StringUtils.isNotBlank(correlationId)) {
        tracingService.addTagToCurrentSpan(CORRELATION_ID_HEADER_NAME, correlationId);
        MDC.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);
      }
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      MDC.remove(CORRELATION_ID_LOG_VAR_NAME);
    }
  }

  public boolean isValidUUID(String correlationId) {
    try {
      UUID.fromString(correlationId);
      return true;
    } catch (final RuntimeException e) {
      return false;
    }
  }
}
