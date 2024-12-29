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

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * IMPORTANT: this filter must be executed AFTER spring filter responsible for creation of
 * Micrometer observations (see {@link org.springframework.web.filter.ServerHttpObservationFilter}.
 * Otherwise, tracing details will not be available (extracted from requests of created). As result
 * use the lowest precedence here.
 */
@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class MdcLogFilter implements Filter {

  private final TracingBasedMdcProvider tracingBasedMdcProvider;
  private final CorrelationIdProvider correlationIdProvider;

  @Override
  public void doFilter(
      final ServletRequest servletRequest,
      final ServletResponse servletResponse,
      final FilterChain filterChain)
      throws IOException, ServletException {
    try {
      tracingBasedMdcProvider.useTraceInfoFromTracer(
          correlationIdProvider.getCorrelationId(servletRequest));
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      tracingBasedMdcProvider.clearTraceInfo();
    }
  }

  @Override
  public void destroy() {
    // no specific implementation
  }

  @Override
  public void init(final FilterConfig filterConfig) {
    // no specific implementation
  }
}
