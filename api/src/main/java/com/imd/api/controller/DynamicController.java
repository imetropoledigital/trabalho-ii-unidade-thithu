package com.imd.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imd.api.service.DynamicService;

import java.util.List;

import org.bson.Document;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DynamicController {
  private final DynamicService dynamicService;

  @PostMapping("/{collectionName}")
    public ResponseEntity<Document> saveDocument(
            @PathVariable String collectionName,
            @RequestBody Document document) {
        Document savedDocument = dynamicService.save(collectionName, document);
        return ResponseEntity.status(201).body(savedDocument);
    }

  @GetMapping("/{collectionName}")
    public ResponseEntity<List<Document>> getAllDocuments(@PathVariable String collectionName) {
      List<Document> documents = dynamicService.findAll(collectionName);
      return ResponseEntity.ok(documents);
    }
}
