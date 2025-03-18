package com.senai.kaiquebahmad.consultasmedicas.entity;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class Medico {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;
	@OneToMany()
	private List<Consulta> consultas;
	private String nome;
	private Especialidade especialidade;
	private String CRM;

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Especialidade getEspecialidade() {
		return especialidade;
	}
	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}
	public String getCRM() {
		return CRM;
	}
	public void setCRM(String cRM) {
		CRM = cRM;
	}
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public List<Consulta> getConsultas() {
		return consultas;
	}
	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}
}
