package io.programmingnotes.apigenerator.repository;

import io.programmingnotes.apigenerator.data.OpenAPISummary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OpenAPISummaryRepository extends MongoRepository<OpenAPISummary, String> {
    OpenAPISummary findByUsernameSpecId(String username, String specId);
}
