package it.corso.dto;

import it.corso.model.NomeCategoria;

public class AddCategoriaDTO {
	private NomeCategoria nome;
	
	public NomeCategoria getNome() {
		return nome;
	}
	public void setNome(NomeCategoria nome) {
		this.nome = nome;
	}
}
