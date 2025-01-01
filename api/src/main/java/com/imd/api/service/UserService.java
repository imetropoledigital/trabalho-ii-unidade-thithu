package com.imd.api.service;

import com.imd.api.exception.UserNotFoundException;
import com.imd.api.model.User;
import com.imd.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.PageImpl;



import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * Buscar usuário pelo ID.
     *
     * @param userId ID do usuário.
     * @return Usuário encontrado.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    // Método para buscar com a query gerada dinamicamente
    public Page<User> findAllWithQuery(Query query, Pageable pageable) {
        // Usando a Query com paginação para buscar a lista de usuários
        List<User> users = mongoTemplate.find(query.with(pageable), User.class);
        long count = mongoTemplate.count(query, User.class);  // Conta o número total de registros para a paginação
        return new PageImpl<>(users, pageable, count);
    }

    /**
     * Buscar todos os usuários cadastrados.
     *
     * @return Lista de usuários.
     */
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    // Método para encontrar todos os usuários com projeção de campos
    public Page<User> findAllWithFields(Query query, String fields, Pageable pageable) {
        // Definir os campos a serem projetados (inclusão/exclusão)
        String[] fieldList = fields.split(",");
        for (String field : fieldList) {
            if (field.startsWith("-")) {
                query.fields().exclude(field.substring(1));  // Exclui o campo
            } else {
                query.fields().include(field);  // Inclui o campo
            }
        }

        // Executa a consulta com projeção de campos
        List<User> users = mongoTemplate.find(query.with(pageable), User.class);
        long count = mongoTemplate.count(query, User.class);  // Conta o número total de registros
        return new PageImpl<>(users, pageable, count);  // Retorna como uma Page
    }

    /**
     * Salvar ou atualizar um usuário.
     *
     * @param user Usuário a ser salvo ou atualizado.
     * @return Usuário salvo ou atualizado.
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Deletar usuário pelo ID.
     *
     * @param userId ID do usuário a ser deletado.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    public void deleteById(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Cannot delete. User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
