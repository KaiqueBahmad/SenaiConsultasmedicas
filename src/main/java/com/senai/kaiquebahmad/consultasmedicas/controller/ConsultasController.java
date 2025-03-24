package com.senai.kaiquebahmad.consultasmedicas.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.senai.kaiquebahmad.consultasmedicas.dto.ConsultaDTO;
import com.senai.kaiquebahmad.consultasmedicas.dto.ConsultaRequestDTO;
import com.senai.kaiquebahmad.consultasmedicas.entity.Consulta;
import com.senai.kaiquebahmad.consultasmedicas.entity.Medico;
import com.senai.kaiquebahmad.consultasmedicas.entity.Paciente;
import com.senai.kaiquebahmad.consultasmedicas.entity.Consulta.Status;
import com.senai.kaiquebahmad.consultasmedicas.service.ConsultaService;
import com.senai.kaiquebahmad.consultasmedicas.service.MapperService;
import com.senai.kaiquebahmad.consultasmedicas.service.MedicoService;
import com.senai.kaiquebahmad.consultasmedicas.service.PacienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/consultas")
@Tag(name = "Consultas", description = "API para gerenciamento de consultas médicas")
public class ConsultasController {
    
    @Autowired
    private ConsultaService consultaService;
    
    @Autowired
    private MedicoService medicoService;
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private MapperService mapperService;
    
    @GetMapping
    @Operation(summary = "Listar todas as consultas", description = "Retorna uma lista com todas as consultas cadastradas")
    public ResponseEntity<List<ConsultaDTO>> listarTodas() {
        List<Consulta> consultas = consultaService.listarTodas();
        List<ConsultaDTO> consultaDTOs = mapperService.toConsultaDTOList(consultas);
        return ResponseEntity.ok(consultaDTOs);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta por ID", description = "Retorna uma consulta específica pelo seu ID")
    public ResponseEntity<ConsultaDTO> buscarPorId(@PathVariable Integer id) {
        Optional<Consulta> consulta = consultaService.buscarPorId(id);
        return consulta.map(c -> ResponseEntity.ok(mapperService.toConsultaDTO(c)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta não encontrada"));
    }
    
    @GetMapping("/medico/{medicoId}")
    @Operation(summary = "Listar consultas por médico", description = "Retorna todas as consultas de um determinado médico")
    public ResponseEntity<List<ConsultaDTO>> buscarPorMedico(@PathVariable Integer medicoId) {
        Optional<Medico> medico = medicoService.buscarPorId(medicoId);
        if (medico.isPresent()) {
            List<Consulta> consultas = consultaService.buscarPorMedico(medico.get());
            return ResponseEntity.ok(mapperService.toConsultaDTOList(consultas));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado");
    }
    
    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar consultas por paciente", description = "Retorna todas as consultas de um determinado paciente")
    public ResponseEntity<List<ConsultaDTO>> buscarPorPaciente(@PathVariable Integer pacienteId) {
        Optional<Paciente> paciente = pacienteService.buscarPorId(pacienteId);
        if (paciente.isPresent()) {
            List<Consulta> consultas = consultaService.buscarPorPaciente(paciente.get());
            return ResponseEntity.ok(mapperService.toConsultaDTOList(consultas));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado");
    }
    
    @GetMapping("/data")
    @Operation(summary = "Listar consultas por data", description = "Retorna todas as consultas agendadas para uma determinada data")
    public ResponseEntity<List<ConsultaDTO>> buscarPorData(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date data) {
        List<Consulta> consultas = consultaService.buscarPorData(data);
        return ResponseEntity.ok(mapperService.toConsultaDTOList(consultas));
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Listar consultas por status", description = "Retorna todas as consultas com um determinado status")
    public ResponseEntity<List<ConsultaDTO>> buscarPorStatus(@PathVariable Status status) {
        List<Consulta> consultas = consultaService.buscarPorStatus(status);
        return ResponseEntity.ok(mapperService.toConsultaDTOList(consultas));
    }
    
    @PostMapping
    @Operation(summary = "Agendar consulta", description = "Registra uma nova consulta no sistema")
    public ResponseEntity<ConsultaDTO> agendar(@RequestBody @Valid ConsultaRequestDTO consultaDTO) {
        Optional<Medico> medico = medicoService.buscarPorId(consultaDTO.getMedicoId());
        Optional<Paciente> paciente = pacienteService.buscarPorId(consultaDTO.getPacienteId());
        
        if (medico.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado");
        }
        
        if (paciente.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado");
        }
        
        if (!consultaService.verificarDisponibilidadeMedico(medico.get(), consultaDTO.getData())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Médico já possui consulta agendada para esta data/horário");
        }
        
        Consulta consulta = mapperService.toConsultaEntity(consultaDTO, medico.get(), paciente.get());
        
        consulta.setStatus(Status.AGENDADA);
        
        Consulta novaConsulta = consultaService.salvar(consulta);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapperService.toConsultaDTO(novaConsulta));
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status da consulta", description = "Atualiza o status de uma consulta existente")
    public ResponseEntity<ConsultaDTO> atualizarStatus(
            @PathVariable Integer id, 
            @RequestParam Status novoStatus) {
        
        Consulta consultaAtualizada = consultaService.atualizarStatus(id, novoStatus);
        
        if (consultaAtualizada == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta não encontrada");
        }
        
        return ResponseEntity.ok(mapperService.toConsultaDTO(consultaAtualizada));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar consulta", description = "Atualiza os dados de uma consulta existente")
    public ResponseEntity<ConsultaDTO> atualizar(
            @PathVariable Integer id, 
            @RequestBody @Valid ConsultaRequestDTO consultaDTO) {
        
        Optional<Consulta> consultaOptional = consultaService.buscarPorId(id);
        if (consultaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta não encontrada");
        }
        
        Optional<Medico> medico = medicoService.buscarPorId(consultaDTO.getMedicoId());
        Optional<Paciente> paciente = pacienteService.buscarPorId(consultaDTO.getPacienteId());
        
        if (medico.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado");
        }
        
        if (paciente.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado");
        }
        
        Consulta consultaExistente = consultaOptional.get();
        if (!consultaExistente.getData().equals(consultaDTO.getData())) {
            if (!consultaService.verificarDisponibilidadeMedico(medico.get(), consultaDTO.getData())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                        "Médico já possui consulta agendada para esta data/horário");
            }
        }
        
        mapperService.updateConsultaFromDTO(consultaExistente, consultaDTO, medico.get(), paciente.get());
        
        Consulta consultaAtualizada = consultaService.salvar(consultaExistente);
        return ResponseEntity.ok(mapperService.toConsultaDTO(consultaAtualizada));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir consulta", description = "Remove uma consulta do sistema")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!consultaService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta não encontrada");
        }
        
        consultaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/hoje/agendadas")
    @Operation(summary = "Listar consultas agendadas para hoje", description = "Retorna todas as consultas agendadas para o dia atual")
    public ResponseEntity<List<ConsultaDTO>> consultasAgendadasHoje() {
        List<Consulta> consultas = consultaService.consultasAgendadasHoje();
        return ResponseEntity.ok(mapperService.toConsultaDTOList(consultas));
    }
    
    @GetMapping("/hoje/em-atendimento")
    @Operation(summary = "Listar consultas em atendimento hoje", description = "Retorna todas as consultas em atendimento no dia atual")
    public ResponseEntity<List<ConsultaDTO>> consultasEmAtendimentoHoje() {
        List<Consulta> consultas = consultaService.consultasEmAtendimentoHoje();
        return ResponseEntity.ok(mapperService.toConsultaDTOList(consultas));
    }
}