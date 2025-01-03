package com.imd.api.controller;

import com.imd.api.model.User;
import com.imd.api.service.UserService;
import com.imd.api.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.json.JSONObject;




import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    @Autowired
    private final MongoTemplate mongoTemplate;

    /**
     * Listar todos os usuários.
     *
     * @return Lista de usuários.
     */
    // Buscar usuários com query (como string JSON)
    @GetMapping
    public List<User>  getUsers(@RequestParam("query") String queryJson ){

        BasicQuery query = new BasicQuery(queryJson);
        return mongoTemplate.find(query, User.class);
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false) String query,
//            @RequestParam(required = false) String fields ) {

//        Pageable pageable = PageRequest.of(page, size);
//
//        // Caso a query seja fornecida, tentamos converte-la para um objeto MongoDB Criteria
//        if (query != null) {
//
//            try {
//
//                System.out.println("oi eu sou a query: " + query);
//                // Usando o ObjectMapper para converter a string JSON para um objeto Criteria
//                Criteria criteria = convertQueryToCriteria(query);
//
//                System.out.println(criteria);
//
//                Query mongoQuery = new Query(criteria);
//
//                System.out.println("string de palitinho"+mongoQuery);
//
//                // Se tiver fields, vamos aplicar projeção
//                if (fields != null) {
//                    return ResponseEntity.ok(userService.findAllWithFields(mongoQuery, fields, pageable));
//                }
//
//                // Caso contrário, apenas retornamos os usuários com a query
//                return ResponseEntity.ok(userService.findAllWithQuery(mongoQuery, pageable));
//            } catch (Exception e) {
//                return ResponseEntity.badRequest().body(null); // Se falhar na conversão, retorna erro 400
//            }
//        }



        // Caso não haja query, retornar todos os usuários
//        return ResponseEntity.ok(userService.findAll(pageable));
    }



    // Método para converter string JSON em um objeto Criteria
    private Criteria convertQueryToCriteria(String query) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return Criteria.where("id").is("dummy"); // Substitua por uma conversão para Criteria adequada
    }



    /**
     * Buscar usuário por ID.
     *
     * @param id ID do usuário.
     * @return Usuário encontrado ou erro 404 se não encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Criar um novo usuário.
     *
     * @param user Dados do usuário.
     * @return Usuário criado com status 201.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.status(201).body(createdUser);
    }

    /**
     * Atualizar usuário existente.
     *
     * @param id          ID do usuário.
     * @param updatedUser Dados atualizados do usuário.
     * @return Usuário atualizado ou erro 404 se não encontrado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @Valid @RequestBody User updatedUser) {
        try {
            User existingUser = userService.findById(id);
            updatedUser.setId(existingUser.getId()); // Garante que estamos atualizando o mesmo usuário
            User savedUser = userService.save(updatedUser);
            return ResponseEntity.ok(savedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletar usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return Status 204 se deletado ou erro 404 se não encontrado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
