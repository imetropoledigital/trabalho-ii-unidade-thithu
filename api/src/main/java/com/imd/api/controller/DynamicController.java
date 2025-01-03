package com.imd.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import com.imd.api.service.DynamicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Sort;


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

  @GetMapping("/{collectionName}/{id}")
  public ResponseEntity<Document> getById(@PathVariable String collectionName, @PathVariable String id) {
    try {
      ObjectId objectId = new ObjectId(id);
      Document document = dynamicService.findById(collectionName, objectId);
      if (document != null) {
        return ResponseEntity.ok(document);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }



  @PutMapping("/{collectionName}/{id}")
  public ResponseEntity<Document> updateDocument(
          @PathVariable String collectionName,
          @PathVariable String id,
          @RequestBody Document updatedDocument) {
    try {
      ObjectId objectId = new ObjectId(id);


      Document existingDocument = dynamicService.findById(collectionName, objectId);

      if (existingDocument == null) {
        return ResponseEntity.notFound().build();
      }

      updatedDocument.put("_id", existingDocument.get("_id"));

      Document savedDocument = dynamicService.save(collectionName, updatedDocument);

      return ResponseEntity.ok(savedDocument);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 em caso de erro
    }
  }

  // Endpoint atualizado para buscar com filtro de consulta

  @GetMapping("/{collectionName}/query")
  public ResponseEntity<Page<Document>> getPaginated(
          @PathVariable String collectionName, // Nome da coleção
          @RequestParam String query,          // Consulta JSON
          @RequestParam int page,              // Número da página
          @RequestParam int size               // Tamanho da página
  ) {
    try {
      // Cria a consulta com base no JSON recebido
      BasicQuery mongoQuery = new BasicQuery(query);

      // Busca paginada no serviço
      Page<Document> result = dynamicService.findPaginated(collectionName, mongoQuery, page, size);

      // Retorna os resultados
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      // Log da exceção (opcional, para depuração)
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }


}
