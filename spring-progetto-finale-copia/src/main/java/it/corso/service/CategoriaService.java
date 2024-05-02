package it.corso.service;

import java.util.List;

import it.corso.dto.AddCategoriaDTO;
import it.corso.dto.CategoriaShowDTO;
import it.corso.model.Categoria;
import jakarta.validation.Valid;

public interface CategoriaService {
	Categoria getCategoriaById(int id);
	List<CategoriaShowDTO> getCategorie();
	void creaCategoria(AddCategoriaDTO categoria);
	void deleteCategoriaById(int id);
	void updateCategoria(CategoriaShowDTO categoria);
	List<CategoriaShowDTO> getCategorieFiltrate(String filterText);
}
