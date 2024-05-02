package it.corso.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CategoriaDao;
import it.corso.dto.AddCategoriaDTO;
import it.corso.dto.CategoriaShowDTO;
import it.corso.dto.UtenteAggiornamentoDTO;
import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;
import it.corso.model.Ruolo;
import it.corso.model.Utente;

@Service
public class CategoriaServiceImpl implements CategoriaService {
	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private CategoriaDao categoriaDao;

	@Override
	public Categoria getCategoriaById(int id) {
		Optional<Categoria> categoriaOption = categoriaDao.findById(id);
		
		if (categoriaOption.isPresent()) {
			return categoriaOption.get();
		}
		return null;
	}

	@Override
	public List<CategoriaShowDTO> getCategorie() {
		List<Categoria> listaCategorie = (List<Categoria>) categoriaDao.findAll();
		
		List<CategoriaShowDTO> categoriaShowDTO = new ArrayList<>();
		
		listaCategorie.forEach(c -> categoriaShowDTO.add(modelMapper.map(c, CategoriaShowDTO.class)));
		
		return categoriaShowDTO;
	}
	
	@Override
	public List<CategoriaShowDTO> getCategorieFiltrate(String filterText) {
	    Iterable<Categoria> categorie = categoriaDao.findAll();
	    
	    List<CategoriaShowDTO> categorieFiltrate = new ArrayList<>();
	    for (Categoria categoria : categorie) {
	        if (categoria.getNomeCategoria().name().toLowerCase().contains(filterText.toLowerCase())) {
	            categorieFiltrate.add(modelMapper.map(categoria, CategoriaShowDTO.class));
	        }
	    }
	    
	    return categorieFiltrate;
	}

	@Override
	public void creaCategoria(AddCategoriaDTO categoria) {
		Categoria categoriaObj = new Categoria();
		categoriaObj.setNomeCategoria(categoria.getNome());
		
		categoriaDao.save(categoriaObj);
	}
	
	public void deleteCategoriaById(int id) {
		Optional<Categoria> categoriaOption = categoriaDao.findById(id);
		
		if (categoriaOption.isPresent()) {
			categoriaDao.delete(categoriaOption.get());
		}
	}
	
	@Override
	public void updateCategoria(CategoriaShowDTO categoria) {
		try {
		    Optional<Categoria> categoriaDbOptional = categoriaDao.findById(categoria.getId());

		    if (categoriaDbOptional.isPresent()) {
		        Categoria categoriaDb = categoriaDbOptional.get();
		        categoriaDb.setNomeCategoria(NomeCategoria.valueOf(categoria.getNome()));
		        categoriaDao.save(categoriaDb);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
