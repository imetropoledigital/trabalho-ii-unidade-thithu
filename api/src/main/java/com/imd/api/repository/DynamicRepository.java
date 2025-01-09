package com.imd.api.repository;

import java.util.List;
import java.util.UUID;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class DynamicRepository {
  private final MongoTemplate mongoTemplate;

  @Autowired
  public DynamicRepository(MongoTemplate mongoTemplate) {
      this.mongoTemplate = mongoTemplate;
  }

/*   public Document saveDocument(String collectionName, Document document) {
    return mongoTemplate.save(document, collectionName);
  }
 */

  public Document saveDocument(String collectionName, Document document) {
    // Verificar se o campo _id está presente
    if (!document.containsKey("_id")) {
    // Adicionar um ID único (UUID) ao documento
      document.put("_id", UUID.randomUUID().toString());
    }
    return mongoTemplate.save(document, collectionName);
  }

  public List<Document> findAll(String collectionName) {
    return mongoTemplate.findAll(Document.class, collectionName);
  }

  // Método findById corrigido

  /* public Document findById(String collectionName, ObjectId id) {
    return mongoTemplate.findById(id, Document.class, collectionName);
  }
 */
  
 public Document findOne(Query query, String collectionName){
  return mongoTemplate.findOne(query, Document.class, collectionName);
}

 public List<Document> find(Query query, String collectionName) {
    return mongoTemplate.find(query, Document.class, collectionName);
  }

  public long count(Query query, String collectionName) {
    return mongoTemplate.count(query, collectionName);
  }
}
