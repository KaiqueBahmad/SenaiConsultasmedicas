package com.senai.kaiquebahmad.consultasmedicas.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public class ConsultaRequestDTO {
    
    @NotNull(message = "A data da consulta é obrigatória")
    @FutureOrPresent(message = "A data da consulta deve ser no presente ou futuro")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date data;
    
    @NotNull(message = "O ID do médico é obrigatório")
    private Integer medicoId;
    
    @NotNull(message = "O ID do paciente é obrigatório")
    private Integer pacienteId;
    
    public ConsultaRequestDTO() {
    }
    
    public ConsultaRequestDTO(Date data, Integer medicoId, Integer pacienteId) {
        this.data = data;
        this.medicoId = medicoId;
        this.pacienteId = pacienteId;
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
}