package com.imd.api.service;

import com.imd.api.model.User;
import com.imd.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Buscar usuário pelo ID
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)); // Exceção genérica ou personalizada
    }

    // Buscar todos os usuários
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Salvar ou atualizar um usuário
    public User save(User user) {
        return userRepository.save(user);
    }

    // Deletar usuário pelo ID
    public void deleteById(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Cannot delete. User not found with ID: " + userId); // Exceção genérica ou personalizada
        }
        userRepository.deleteById(userId);
    }
}
