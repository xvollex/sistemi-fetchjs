package it.corso.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.corso.dto.UserRegisterDTO;
import it.corso.dto.UtenteAggiornamentoDTO;
import it.corso.dto.UtenteLoginRequestDTO;
import it.corso.dto.UtenteShowDTO;
import it.corso.model.Utente;

@Service
public interface UserService {
	// inserimento utente
	void userRegistration(UserRegisterDTO user);
	
	// ritorno un utente in base all'id
	Utente getUserById(int id);
	
	// aggiornamento dati utente
	void updateUserData(UtenteAggiornamentoDTO user);
	
	// ritorna la lista degli utenti
	List<UtenteShowDTO> getUsers();
	
	// cancellazione utente in base alla mail
	void deleteUser(String email);
	
	// cancellazione utente in base all'id
	void deleteUserById(int id);
	
	// esistenza utente tramite email
	boolean existsUserByEmail(String email);
	
	UtenteShowDTO getUserByEmail(String email);
	
	boolean loginUtente(UtenteLoginRequestDTO utenteLoginRequestDTO);
	
	Utente findByEmail(String email);

}
