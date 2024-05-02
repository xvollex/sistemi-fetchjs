package it.corso.dto;

import jakarta.validation.constraints.Pattern;

public class UserRegisterDTO {
	@Pattern(regexp = "^[A-Za-zÀ-ÿ' -]+$")
	private String nome;
	@Pattern(regexp = "^[A-Za-zÀ-ÿ' -]+$")
	private String cognome;
	@Pattern(regexp = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$")
	private String email;
	/**
	 * @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,}$")
	 * siccome la validazione rimane attiva ed hasho la password, e quindi potrei non
	 * rispettare la regex, tiro via questa annotation
	 */
	private String password;

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
}
