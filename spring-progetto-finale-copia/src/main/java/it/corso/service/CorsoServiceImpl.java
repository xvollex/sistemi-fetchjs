package it.corso.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CorsoDao;
import it.corso.dto.CorsoDTO;
import it.corso.model.Corso;

@Service
public class CorsoServiceImpl implements CorsoService {
	@Autowired
	private CorsoDao corsoDao;
	
	private ModelMapper mapper = new ModelMapper();
	
	@Override
	public List<CorsoDTO> getCourses() {
		List<Corso> corso = (List<Corso>) corsoDao.findAll();
		List<CorsoDTO> corsoDto = new ArrayList<>();
		
		corso.forEach(c -> corsoDto.add(mapper.map(c, CorsoDTO.class)));
		
		return corsoDto;
	}
}
