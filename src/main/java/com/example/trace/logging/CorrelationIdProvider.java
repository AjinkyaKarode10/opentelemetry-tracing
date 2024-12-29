/*******************************************************************************
 *
 * Copyright (c) 2024 by ACI Worldwide Inc.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of ACI
 * Worldwide Inc ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered with ACI Worldwide Inc.
 ******************************************************************************/

package com.example.trace.logging;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;

@Component
public class CorrelationIdProvider {

  public static final String CORRELATION_ID_HEADER = "X-ACI-App-Correlation-ID";

  public String getCurrentCorrelationId() {
    return getCurrentRequest().map(r -> r.getHeader(CORRELATION_ID_HEADER)).orElse(null);
  }

  public String getCorrelationId(final ServletRequest request) {
    return request instanceof HttpServletRequest
        ? ((HttpServletRequest) request).getHeader(CORRELATION_ID_HEADER)
        : null;
  }

  private Optional<HttpServletRequest> getCurrentRequest() {
    val requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes) {
      return Optional.of(((ServletRequestAttributes) requestAttributes).getRequest());
    }

    return Optional.empty();
  }
}
