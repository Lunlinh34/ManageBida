package com.example.Bida.Bida.Bida.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public abstract class BaseService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    public BaseService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public T save(T t) {
        return repository.save(t);
    }

    public void delete(ID id) {
        repository.deleteById(id);
    }

    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
