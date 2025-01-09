package com.imd.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.data.mongodb.core.query.BasicQuery;
import com.imd.api.service.DynamicService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.springframework.data.domain.Page;

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
    public ResponseEntity<?> getDocuments(
        @PathVariable String collectionName,
        @RequestParam(required = false) String query, // Parâmetro de consulta JSON (opcional)
        @RequestParam(required = false) String fields, // Projeção de campos (opcional)
        @RequestParam(required = false) Integer page, // Número da página (opcional)
        @RequestParam(required = false) Integer size // Tamanho da página (opcional)
    ) {
        try {
            // Caso query, page ou size não sejam fornecidos, retornar todos os documentos
            if (query == null && page == null && size == null) {
                List<Document> documents = dynamicService.findAll(collectionName);
                return ResponseEntity.ok(documents);
            }

            // Decodificar e criar a consulta Mongo
            String decodedQuery = query != null ? URLDecoder.decode(query, StandardCharsets.UTF_8) : "{}";
            BasicQuery mongoQuery = new BasicQuery(decodedQuery);

            // Adiciona projeção, se fornecida
            if (fields != null && !fields.isBlank()) {
                Document projection = new Document();
                for (String field : fields.split(",")) {
                    if (field.startsWith("-")) {
                        projection.put(field.substring(1), 0); // Excluir campo
                    } else {
                        projection.put(field, 1); // Incluir campo
                    }
                }
                mongoQuery.setFieldsObject(projection);
            }

            // Retornar com paginação se page e size forem fornecidos
            if (page != null && size != null) {
                Page<Document> result = dynamicService.findPaginated(collectionName, mongoQuery, page, size);
                return ResponseEntity.ok(result);
            }

            // Caso nenhuma das condições anteriores seja satisfeita
            return ResponseEntity.badRequest().body("Parâmetros inválidos ou insuficientes");
        } catch (Exception e) {
            // Log da exceção (opcional, para depuração)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar a solicitação");
        }
    }

  @GetMapping("/{collectionName}/{id}")
  public ResponseEntity<Document> getDocumentById(
    @PathVariable String collectionName,
    @PathVariable String id) {
    // Chama o serviço para buscar o documento
    Document document = dynamicService.findById(collectionName, id);

    if (document == null) {
        return ResponseEntity.notFound().build(); // Retorna 404 se não encontrado
    }

    return ResponseEntity.ok(document); // Retorna 200 com o documento
    }

  @PutMapping("/{collectionName}/{id}")
  public ResponseEntity<Document> updateDocument(
          @PathVariable String collectionName,
          @PathVariable String id,
          @RequestBody Document updatedDocument) {
    try {
      Document existingDocument = dynamicService.findById(collectionName, id);

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
}
