package com.senai.kaiquebahmad.consultasmedicas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MedicoDTO {
    
    private Integer id;
    
    @NotBlank(message = "O nome do médico é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "A especialidade é obrigatória")
    @Size(min = 2, max = 50, message = "A especialidade deve ter entre 2 e 50 caracteres")
    private String especialidade;
    
    @NotBlank(message = "O CRM é obrigatório")
    @Pattern(regexp = "^\\d{4,6}\\/[A-Z]{2}$", message = "CRM deve estar no formato XXXXX/UF")
    private String crm;
    
    public MedicoDTO() {
    }
    
    public MedicoDTO(Integer id, String nome, String especialidade, String crm) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.crm = crm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }
}
