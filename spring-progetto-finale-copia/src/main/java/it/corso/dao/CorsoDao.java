package it.corso.dao;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.Corso;

//DAO: la parte che si occupa dell'accesso al database

public interface CorsoDao extends CrudRepository<Corso, Integer> {

}
