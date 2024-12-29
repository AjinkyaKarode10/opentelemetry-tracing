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

import org.springframework.stereotype.Service;

import io.opentelemetry.api.trace.Span;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor
@Service
public class TracingService {

  public void addTagToCurrentSpan(final String key, final String value) {
    val span = Span.current();
    try {
      span.setAttribute(key, value);
    } catch (final RuntimeException ex) {
      // should never throw exception
      log.warn("Unable to set attribute", ex);
    }
  }
}
