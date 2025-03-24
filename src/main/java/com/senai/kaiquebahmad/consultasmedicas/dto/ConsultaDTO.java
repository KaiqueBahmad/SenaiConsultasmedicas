package com.senai.kaiquebahmad.consultasmedicas.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.senai.kaiquebahmad.consultasmedicas.entity.Consulta.Status;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public class ConsultaDTO {
    
    private Integer id;
    
    @NotNull(message = "A data da consulta é obrigatória")
    @FutureOrPresent(message = "A data da consulta deve ser no presente ou futuro")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date data;
    
    @NotNull(message = "O ID do médico é obrigatório")
    private Integer medicoId;
    
    @NotNull(message = "O ID do paciente é obrigatório")
    private Integer pacienteId;
    
    private Status status;
    
    private String medicoNome;
    private String pacienteNome;
    
    public ConsultaDTO() {
    }
    
    public ConsultaDTO(Integer id, Date data, Integer medicoId, Integer pacienteId, Status status) {
        this.id = id;
        this.data = data;
        this.medicoId = medicoId;
        this.pacienteId = pacienteId;
        this.status = status;
    }
    
    public ConsultaDTO(Integer id, Date data, Integer medicoId, Integer pacienteId, 
                       Status status, String medicoNome, String pacienteNome) {
        this.id = id;
        this.data = data;
        this.medicoId = medicoId;
        this.pacienteId = pacienteId;
        this.status = status;
        this.medicoNome = medicoNome;
        this.pacienteNome = pacienteNome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Integer medicoId) {
        this.medicoId = medicoId;
    }

    public Integer getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Integer pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMedicoNome() {
        return medicoNome;
    }

    public void setMedicoNome(String medicoNome) {
        this.medicoNome = medicoNome;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public void setPacienteNome(String pacienteNome) {
        this.pacienteNome = pacienteNome;
    }
}
