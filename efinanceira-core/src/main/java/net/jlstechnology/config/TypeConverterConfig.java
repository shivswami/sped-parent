package net.jlstechnology.config;

import java.util.Arrays;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.SpringTypeConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class TypeConverterConfig {
	
	/**
     * Copied from org.apache.camel.spring.boot.TypeConversionConfiguration (they are package protected)
     **/
    @Bean
    SpringTypeConverter springTypeConverter(CamelContext camelContext, ConversionService[] conversionServices) {
        SpringTypeConverter springTypeConverter = new SpringTypeConverter(Arrays.asList(conversionServices));
        camelContext.getTypeConverterRegistry().addFallbackTypeConverter(springTypeConverter, true);
        return springTypeConverter;
    }

    @ConditionalOnMissingBean
    @Bean
    ConversionService defaultCamelConversionService(ApplicationContext applicationContext) {
        DefaultConversionService service = new DefaultConversionService();
        for (Converter converter : applicationContext.getBeansOfType(Converter.class).values()) {
            service.addConverter(converter);
        }
        return service;
    }

}
