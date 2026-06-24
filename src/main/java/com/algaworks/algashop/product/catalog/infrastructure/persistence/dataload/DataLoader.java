package com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload;

import com.algaworks.algashop.product.catalog.infrastructure.utility.AlgashopResourceUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {

    private final MongoOperations mongoOperations;
    private final DataLoadProperties properties;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        if (!properties.enabled()) {
            return;
        }

        log.info("Data load started");
        if (properties.sources().isEmpty()) {
            log.info("No sources configured");
        }

        properties.sources().forEach(this::importJsonFileToCollection);
    }

    private void importJsonFileToCollection(final DataLoadProperties.DataLoadSource source) {
        final var rawJson= AlgashopResourceUtility.readContent(source.location());
        if (StringUtils.isBlank(rawJson)) {
            log.warn("Resource {} is empty or not found", source.location());
            return;
        }
        
        final var docs = parseJsonToDocuments(rawJson);
        final var inserted = insertInto(docs, source.collection());
        log.info("{} - imports: {}/{}", source.location(), inserted, docs.size());
    }

    private List<Document> parseJsonToDocuments(final String rawJson) {
        try{
            final var array = BsonArray.parse(rawJson);
            return array.stream().map(Object::toString).map(Document::parse).toList();
        } catch (Exception e) {
            log.error("Failed to parse JSON resource", e);
            return Collections.emptyList();
        }
    }

    private int insertInto(final List<Document> mongoDocs, final String collectionName) {
        if (mongoDocs.isEmpty()) {
            return 0;
        }

        try{
            if (properties.autoClean()){
                mongoOperations.getCollection(collectionName).deleteMany(new BsonDocument());
            }
            return mongoOperations.insert(mongoDocs, collectionName).size();
        } catch (final Exception e) {
            log.error("Failed to insert documents into collection {}", collectionName, e);
            return 0;
        }
    }

}
