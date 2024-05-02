package it.corso.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "corso")
public class Corso {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_C")
	private int id;
	
	@Column(name = "Nome_Corso")
	private String nomeCorso;
	
	@Column(name = "Descrizione_breve")
	private String descrizioneBreve;
	
	@Column(name = "Descrizione_completa")
	private String descrizioneCompleta;
	
	@Column(name = "Durata") //che ci sia la maiuscola o la minuscola non è un problema, basta che ci sia scritto il nome della tabella
	private String durata;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(
		name = "FK_CA",
		referencedColumnName = "ID_CA"
	)
	private Categoria categoria;
}
