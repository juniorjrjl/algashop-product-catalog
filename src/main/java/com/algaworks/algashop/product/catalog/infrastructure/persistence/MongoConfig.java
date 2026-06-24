package com.algaworks.algashop.product.catalog.infrastructure.persistence;

import org.bson.UuidRepresentation;
import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Configuration
class MongoConfig {

    //@Bea ativado usando o application.yaml na config spring.data.mongodb.representation=uuid
    MongoClientSettingsBuilderCustomizer uuidCustomizer(){
        return builder -> builder.uuidRepresentation(UuidRepresentation.STANDARD);
    }

    @Bean
    MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                List.of(
                        new OffsetDateTimeReaderConverter(),
                        new OffsetDateTimeWriteConverter()
                )
        );
    }

    public static class OffsetDateTimeReaderConverter implements Converter<Date, OffsetDateTime> {

        @Override
        public OffsetDateTime convert(final Date source) {
            return source.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }
    }

    public static class OffsetDateTimeWriteConverter implements  Converter<OffsetDateTime, Date> {

        @Override
        public Date convert(final OffsetDateTime source) {
            return Date.from(source.toInstant());
        }
    }

}
