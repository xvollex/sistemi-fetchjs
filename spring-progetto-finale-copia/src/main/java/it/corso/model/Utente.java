package it.corso.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "utente")
public class Utente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_U") //dobbiamo specificarlo perché l'attributo creato ha un nome diverso dal campo nel database
	private int id;
	
	@Column(name = "Nome")
	private String nome;
	
	@Column(name = "Cognome")
	private String cognome;
	
	@Column(name = "email") //per best practice è meglio farlo sempre comunque
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	//JoinTable: da utilizzare solo quando si hanno problemi di raccordo
	@JoinTable(
		name = "utente_ruolo",
		joinColumns = @JoinColumn(
			name = "FK_U",
			referencedColumnName = "ID_U"
		),
		inverseJoinColumns = @JoinColumn(
			name = "FK_R",
			referencedColumnName = "ID_G"
		) //G perché prima la tabella si chiamava "gruppo"
	)
	private List<Ruolo> ruoli = new ArrayList<>();
	//private List<Corso> corsi = new ArrayList<>();

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public List<Ruolo> getRuoli() {
		return ruoli;
	}
	public void setRuoli(List<Ruolo> ruoli) {
		this.ruoli = ruoli;
	}
	
	/*public List<Corso> getCorsi() {
		return corsi;
	}
	public void setCorsi(List<Corso> corsi) {
		this.corsi = corsi;
	}*/
}
