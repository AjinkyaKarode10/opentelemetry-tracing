package com.example.trace.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.trace.logging.CorrelationIdProvider;
import com.example.trace.logging.MdcLogFilter;
import com.example.trace.logging.TracingBasedMdcProvider;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.semconv.ResourceAttributes;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "opentelemetry", name = "enabled", havingValue = "true")
public class TraceConfig {


  public static final String SERVICE_NAME = "consumer-mfa-service";
  public static final String CUSTOM_OTEL_SPAN_PROCESSOR = "customOtelSpanProcessor";

//  @Bean
//  public SdkTracerProvider sdkTracerProvider(
//      @Qualifier(CUSTOM_OTEL_SPAN_PROCESSOR) final SpanProcessor spanProcessor,
//      final Sampler sampler) {
//    val serviceNameResource =
//        Resource.create(
//            Attributes.of(
//                ResourceAttributes.SERVICE_NAME,
//                SERVICE_NAME + "@local"));
//    val builder =
//        SdkTracerProvider.builder()
//            .setResource(Resource.getDefault().merge(serviceNameResource))
//            .setSampler(sampler)
//            .addSpanProcessor(spanProcessor);
//
//    return builder.build();
//  }
//
//  @Bean
//  public OpenTelemetry openTelemetry(final SdkTracerProvider tracerProvider) {
//    return OpenTelemetrySdk.builder().setTracerProvider(tracerProvider).build();
//  }


}
