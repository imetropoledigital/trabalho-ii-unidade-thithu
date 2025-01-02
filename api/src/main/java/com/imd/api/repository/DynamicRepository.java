package com.imd.api.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.stereotype.Repository;

@Repository
public class DynamicRepository {
  private final MongoTemplate mongoTemplate;

  @Autowired
  public DynamicRepository(MongoTemplate mongoTemplate) {
      this.mongoTemplate = mongoTemplate;
  }

  public Document saveDocument(String collectionName, Document document) {
    return mongoTemplate.save(document, collectionName);
  }

  public List<Document> findAll(String collectionName) {
    return mongoTemplate.findAll(Document.class, collectionName);
  }
}
