package com.imd.api.service;

import org.springframework.stereotype.Service;

import com.imd.api.repository.DynamicRepository;

import java.util.List;

import org.bson.Document;
import lombok.RequiredArgsConstructor;

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
}
