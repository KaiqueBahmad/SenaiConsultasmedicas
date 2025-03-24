package com.senai.kaiquebahmad.consultasmedicas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.senai.kaiquebahmad.consultasmedicas.dto.ConsultaDTO;
import com.senai.kaiquebahmad.consultasmedicas.dto.ConsultaRequestDTO;
import com.senai.kaiquebahmad.consultasmedicas.dto.MedicoDTO;
import com.senai.kaiquebahmad.consultasmedicas.dto.PacienteDTO;
import com.senai.kaiquebahmad.consultasmedicas.entity.Consulta;
import com.senai.kaiquebahmad.consultasmedicas.entity.Medico;
import com.senai.kaiquebahmad.consultasmedicas.entity.Paciente;

@Service
public class MapperService {
    
    public MedicoDTO toMedicoDTO(Medico medico) {
        if (medico == null) {
            return null;
        }
        
        MedicoDTO dto = new MedicoDTO();
        dto.setId(medico.getID());
        dto.setNome(medico.getNome());
        dto.setEspecialidade(medico.getEspecialidade());
        dto.setCrm(medico.getCRM());
        
        return dto;
    }
    
    public Medico toMedicoEntity(MedicoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Medico medico = new Medico();
        if (dto.getId() != null) {
            medico.setID(dto.getId());
        }
        medico.setNome(dto.getNome());
        medico.setEspecialidade(dto.getEspecialidade());
        medico.setCRM(dto.getCrm());
        
        return medico;
    }
    
    public List<MedicoDTO> toMedicoDTOList(List<Medico> medicos) {
        return medicos.stream()
                .map(this::toMedicoDTO)
                .collect(Collectors.toList());
    }
    
    public PacienteDTO toPacienteDTO(Paciente paciente) {
        if (paciente == null) {
            return null;
        }
        
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getID());
        dto.setNome(paciente.getNome());
        dto.setCpf(paciente.getCPF());
        dto.setDataNascimento(paciente.getDataNascimento());
        dto.setTelefone(paciente.getTelefone());
        
        return dto;
    }
    
    public Paciente toPacienteEntity(PacienteDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Paciente paciente = new Paciente();
        if (dto.getId() != null) {
            paciente.setID(dto.getId());
        }
        paciente.setNome(dto.getNome());
        paciente.setCPF(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setTelefone(dto.getTelefone());
        
        return paciente;
    }
    
    public List<PacienteDTO> toPacienteDTOList(List<Paciente> pacientes) {
        return pacientes.stream()
                .map(this::toPacienteDTO)
                .collect(Collectors.toList());
    }
    
    public ConsultaDTO toConsultaDTO(Consulta consulta) {
        if (consulta == null) {
            return null;
        }
        
        ConsultaDTO dto = new ConsultaDTO();
        dto.setId(consulta.getID());
        dto.setData(consulta.getData());
        dto.setStatus(consulta.getStatus());
        
        if (consulta.getMedico() != null) {
            dto.setMedicoId(consulta.getMedico().getID());
            dto.setMedicoNome(consulta.getMedico().getNome());
        }
        
        if (consulta.getPaciente() != null) {
            dto.setPacienteId(consulta.getPaciente().getID());
            dto.setPacienteNome(consulta.getPaciente().getNome());
        }
        
        return dto;
    }
    
    public Consulta toConsultaEntity(ConsultaRequestDTO dto, Medico medico, Paciente paciente) {
        if (dto == null) {
            return null;
        }
        
        Consulta consulta = new Consulta();
        consulta.setData(dto.getData());
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setStatus(Consulta.Status.AGENDADA);
        
        return consulta;
    }
    
    public List<ConsultaDTO> toConsultaDTOList(List<Consulta> consultas) {
        return consultas.stream()
                .map(this::toConsultaDTO)
                .collect(Collectors.toList());
    }
    
    public void updateConsultaFromDTO(Consulta consulta, ConsultaRequestDTO dto, 
                                     Medico medico, Paciente paciente) {
        consulta.setData(dto.getData());
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
    }
}