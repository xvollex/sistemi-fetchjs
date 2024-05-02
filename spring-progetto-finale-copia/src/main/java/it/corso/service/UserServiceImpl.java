package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
//preso dal Maven Central Repository
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.RuoloDao;
import it.corso.dao.UserDao;
import it.corso.dto.UserRegisterDTO;
import it.corso.dto.UtenteAggiornamentoDTO;
import it.corso.dto.UtenteLoginRequestDTO;
import it.corso.dto.UtenteShowDTO;
import it.corso.model.Ruolo;
import it.corso.model.Utente;

// @service indica che questa classe è un servizio di Spring, e dietro le quinte spring farà l'istanza dell'interfaccia tramite questa classe
@Service
public class UserServiceImpl implements UserService {
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RuoloDao ruoloDao;

	@Override
	public void userRegistration(UserRegisterDTO user) {
		Utente utente = new Utente();
		utente.setNome(user.getNome());
		utente.setCognome(user.getCognome());
		utente.setEmail(user.getEmail());
		utente.setPassword(DigestUtils.sha256Hex(user.getPassword()));
		
		userDao.save(utente);
	}

	@Override
	// non si dovrebbe utilizzare questo approccio perché si creerebbe un errore quando va a selezionare i ruoli, si dovrebbe fare con il model mapper
	public Utente getUserById(int id) {
		Optional<Utente> utenteOption = userDao.findById(id);
		
		if (utenteOption.isPresent()) {
			return utenteOption.get();
		}
		return null;
	}

	@Override
	public void updateUserData(UtenteAggiornamentoDTO user) {
		try {
			Utente utenteDb = userDao.findByEmail(user.getEmail());
			
			if (utenteDb != null) {
				utenteDb.setNome(user.getNome());
				utenteDb.setCognome(user.getCognome());
				utenteDb.setEmail(user.getEmail());
				
				/*List<Ruolo> ruoliUtente = new ArrayList<>();
				Optional<Ruolo> ruoloDb = ruoloDao.findById(user.getIdRuolo());
				
				if (ruoloDb.isPresent()) {
					Ruolo ruolo = ruoloDb.get();
					ruolo.setId(user.getIdRuolo());
					
					ruoliUtente.add(ruolo);
					utenteDb.setRuoli(ruoliUtente);
				}
				*/
				
				userDao.save(utenteDb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<UtenteShowDTO> getUsers() {
		List<Utente> listaUtenti = (List<Utente>) userDao.findAll();
		
		List<UtenteShowDTO> userShowDTO = new ArrayList<>();
		
		listaUtenti.forEach(u -> userShowDTO.add(modelMapper.map(u, UtenteShowDTO.class)));
		
		return userShowDTO;
	}

	@Override
	public void deleteUser(String email) {
		Utente utente = userDao.findByEmail(email);
		int id = utente.getId();
		
		Optional<Utente> utenteOption = userDao.findById(id);
		
		if (utenteOption.isPresent()) {
			userDao.delete(utenteOption.get());
		}
	}
	
	@Override
	public boolean existsUserByEmail(String email) {
		return userDao.existsByEmail(email);
	}
	
	//utilizzo il modelmapper per mappare il singolo utente sulla classe UtenteShowDTO
	@Override
	public UtenteShowDTO getUserByEmail(String email) {
		Utente utente = userDao.findByEmail(email);
		
		UtenteShowDTO userShowDTO = modelMapper.map(utente, UtenteShowDTO.class);
		
		return userShowDTO;
	}
	
	public boolean loginUtente(UtenteLoginRequestDTO utenteLoginRequestDTO) {
		Utente utente = new Utente();
		
		utente.setEmail(utenteLoginRequestDTO.getEmail());
		//fingiamo che la password sia "ciao"
		//getPassword di utenteLoginRequestDTO mi recupera questa password e la setto su utente tramite il metodo setPassword
		utente.setPassword(utenteLoginRequestDTO.getPassword());
		
		System.out.println(utente.getPassword());
		
		String passwordHash = DigestUtils.sha256Hex(utente.getPassword());
		
		Utente credenzialiUtente = userDao.findByEmailAndPassword(utente.getEmail(), passwordHash);
		
		/**
		 * operatore ternario dato dalla condizione
		 * ? | espressione a se è vero
		 * : | espressione b se è falso
		 */
		return credenzialiUtente != null ? true : false;
	}
	
	@Override
	public Utente findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public void deleteUserById(int id) 
	{
		Optional<Utente> utenteOption = userDao.findById(id);
		
		if (utenteOption.isPresent()) 
		{
			userDao.delete(utenteOption.get());
		}
		
	}
}
