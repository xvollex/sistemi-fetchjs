package it.corso.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categoria")
public class Categoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CA")
	private int id;
	
	@Column(name = "Nome_Categoria")
	@Enumerated(EnumType.STRING)
	private NomeCategoria nomeCategoria;
	
	//più corsi appartengono a una categoria
	@OneToMany(
		mappedBy = "categoria",
		cascade = CascadeType.REFRESH,
		fetch = FetchType.EAGER,
		orphanRemoval = true //quando cancello un record dalla tabella figlia, si rimuovono anche le righe corrispondenti dalla tabella padre (si aggiorna anche nel database)
	)
	private List<Corso> corsi = new ArrayList<>();

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public NomeCategoria getNomeCategoria() {
		return nomeCategoria;
	}
	public void setNomeCategoria(NomeCategoria nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public List<Corso> getCorsi() {
		return corsi;
	}
	public void setCorsi(List<Corso> corsi) {
		this.corsi = corsi;
	}
	
}
