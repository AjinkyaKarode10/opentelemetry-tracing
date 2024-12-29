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

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import io.opentelemetry.api.trace.Span;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Component
@RequiredArgsConstructor
public class TracingBasedMdcProvider {
  static final String TRACE_INFO_MDC_KEY = "traceInfo";

  void useTraceInfoFromTracer(final String correlationId) {
    try {
      tryUseTraceInfoFromTracer(correlationId);
    } catch (final Exception ex) {
      // this method should never throw an exception
      log.warn("Failed to append trace info from Tracer to MDC", ex);
    }
  }

  void clearTraceInfo() {
    try {
      MDC.remove(TRACE_INFO_MDC_KEY);
    } catch (final Exception ex) {
      // actually should never happen
      log.warn("Failed to clear trace from MDC", ex);
    }
  }

  private void tryUseTraceInfoFromTracer(final String correlationId) {
    val spanContext = Span.current().getSpanContext();

    val traceId = (spanContext.isValid()) ? spanContext.getTraceId() : null;
    val spanId = (spanContext.isValid()) ? spanContext.getSpanId() : null;

    setTraceInfoIntoMdc(traceId, spanId, correlationId);
  }

  /**
   * TraceId can be empty/null only if jaeger is not enabled (e.g. can be so during service start
   * up) SpanId cannot be null by design
   *
   * <p>
   * CorrelationId can be null only if request is not ServletHttpRequest type, for example in
   * AMQP event handlers
   *
   * <p>
   * Cases: correlationId, traceId, spanId - log [zzzz] [xxxx:yyyy] correlationId, traceId ==
   * spanId - log [zzzz] [xxxx] correlationId, traceId, no spanId - log [zzzz] [xxxx] no
   * correlationId, no traceId, spanId - not possible but anyway log [:yyyy] (but not [null:yyyy])
   * no traceId, no spanId, no correlationId - log nothing
   *
   * @param traceId
   *          id of the trace
   * @param spanId
   *          id of the span
   * @param correlationId
   *          correlation id
   */
  void setTraceInfoIntoMdc(final String traceId, final String spanId, final String correlationId) {
    val traces = combineTraces(traceId, spanId);

    val traceInfo =
        StringUtils.isNotBlank(correlationId)
            ? String.join(" ", formatTraceInfo(correlationId), traces)
            : traces;

    if (StringUtils.isNotBlank(traceInfo)) {
      MDC.put(TRACE_INFO_MDC_KEY, traceInfo.trim());
    }
  }

  private static String combineTraces(final String traceId, final String spanId) {
    if (StringUtils.isAllBlank(traceId, spanId)) {
      return "";
    }

    return StringUtils.isBlank(spanId) || spanId.equals(traceId)
        ? formatTraceInfo(traceId)
        : formatTraceInfo(String.join(":", Objects.toString(traceId, ""), spanId));
  }

  private static String formatTraceInfo(final String traceInfo) {
    return "[" + traceInfo + "]";
  }
}
