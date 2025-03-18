package com.senai.kaiquebahmad.consultasmedicas.entity;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class Especialidade {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;
	private String nome;
	
	@OneToMany()
	private List<Medico> medicos;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public List<Medico> getMedicos() {
		return medicos;
	}
	public void setMedicos(List<Medico> medicos) {
		this.medicos = medicos;
	}
}
