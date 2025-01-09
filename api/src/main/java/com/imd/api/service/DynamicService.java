package com.imd.api.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import com.imd.api.repository.DynamicRepository;
import java.util.List;
import org.bson.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class DynamicService {
   private final DynamicRepository dynamicRepository;

  public Document save(String collectionName, Document document) {
    return dynamicRepository.saveDocument(collectionName, document);
  }

  public List<Document> findAll(String collectionName) {
      return dynamicRepository.findAll(collectionName);
  }

/*   public Document findById(String collectionName, ObjectId id) {
        return dynamicRepository.findById(collectionName, id);
    } */

public Document findById(String collectionName, String id) {
    // Criar uma query para buscar pelo campo _id
    Query query = new Query(Criteria.where("_id").is(id));
    // Executar a busca no MongoDB
    return dynamicRepository.findOne(query, collectionName);
}
    
    public List<Document> find(String collectionName, Query query) {
        return dynamicRepository.find(query, collectionName);
    }

    public Page<Document> findPaginated(String collectionName, Query query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);

        long total = dynamicRepository.count(query, collectionName);
        List<Document> documents = dynamicRepository.find(query, collectionName);

        return new PageImpl<>(documents, pageable, total);
    }
}
