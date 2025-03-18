package com.senai.kaiquebahmad.consultasmedicas.entity;

import java.util.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class Consulta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ID;
	private Date data;
	@OneToMany
	private Medico medico;
	@OneToMany
	private Paciente paciente;
	private Status status;
	
	public static enum Status {
		AGENDADA, CANCELADA, EXPIRADA, ATENDIDO, EM_ATENDIMENTO
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
