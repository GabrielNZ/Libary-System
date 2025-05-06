package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.enumtype.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.exception.DataBaseException;
import com.example.demo.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public User insert(User user) {
        return repository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        try {
            if (!repository.existsById(id)) {
                throw new ResourceNotFoundException(id);
            }
            repository.deleteById(id);
        }catch(DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    @Transactional
    public User update (Long id, User user){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        User entity = repository.getReferenceById(id);
        updateData(entity, user);
        return repository.save(entity);
    }
    //Somente ADMINS vao poder
    public User updateRole (Long id, Role role){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        User entity = repository.getReferenceById(id);
        entity.setRole(role);
        return repository.save(entity);
    }

    private void updateData (User entity, User obj){
        entity.setName(obj.getName());
        entity.setEmail(obj.getEmail());
        entity.setPhone(obj.getPhone());
        entity.setPassword(obj.getPassword());
        entity.setRole(obj.getRole());
    }
}
