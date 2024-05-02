package it.corso.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.corso.dto.CategoriaShowDTO;
import it.corso.model.Categoria;

public interface CategoriaDao extends CrudRepository<Categoria, Integer> {
    
}
