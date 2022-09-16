package com.translink.api.repository;

import com.translink.api.repository.model.StopTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class BulkBatchProcessor {
    @Value(value = "${batch-size}")
    private int batchSize;

    private MongoTemplate mongoTemplate;

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void bulkInsert(Class<?> clazz, List<?> list) {
        long timelapse = Instant.now().toEpochMilli();
        List<Object> batch = new ArrayList<>();
        int batchNumber = 0;
        int totalBatch = list.size() / batchSize + 1;
        int count = 0;
        for (Object object : list) {
            batch.add(object);
            if (batch.size() == batchSize) {
                batchNumber++;
                count = insertBatch(clazz, batch, batchNumber, totalBatch, count);
            }
        }

        batchNumber++;
        count = insertBatch(clazz, batch, batchNumber, totalBatch, count);

        log.info("Saved all , inserted {} documents in {}ms", count, Instant.now().toEpochMilli() - timelapse);
    }

    private int insertBatch(Class<?> clazz, List<Object> list, int batchNumber, int totalBatch, int count) {
        long timelapse = Instant.now().toEpochMilli();
        int inserted = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, clazz)
                .insert(list)
                .execute()
                .getInsertedCount();

        timelapse = Instant.now().toEpochMilli() - timelapse;
        count += inserted;
        list.clear();

        log.info("{}: Inserted {} for {}ms, batch {}/{}", clazz.getSimpleName(), inserted, timelapse, batchNumber, totalBatch);
        return count;
    }
}
