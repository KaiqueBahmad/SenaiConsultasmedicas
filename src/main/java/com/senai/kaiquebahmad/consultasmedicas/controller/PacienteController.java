package com.senai.kaiquebahmad.consultasmedicas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.senai.kaiquebahmad.consultasmedicas.dto.PacienteDTO;
import com.senai.kaiquebahmad.consultasmedicas.entity.Paciente;
import com.senai.kaiquebahmad.consultasmedicas.service.MapperService;
import com.senai.kaiquebahmad.consultasmedicas.service.PacienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pacientes")
@Tag(name = "Pacientes", description = "API para gerenciamento de pacientes")
public class PacienteController {
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private MapperService mapperService;
    
    @GetMapping
    @Operation(summary = "Listar todos os pacientes", description = "Retorna uma lista com todos os pacientes cadastrados")
    public ResponseEntity<List<PacienteDTO>> listarTodos() {
        List<Paciente> pacientes = pacienteService.listarTodos();
        List<PacienteDTO> pacienteDTOs = mapperService.toPacienteDTOList(pacientes);
        return ResponseEntity.ok(pacienteDTOs);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Retorna um paciente específico pelo seu ID")
    public ResponseEntity<PacienteDTO> buscarPorId(@PathVariable Integer id) {
        Optional<Paciente> paciente = pacienteService.buscarPorId(id);
        return paciente.map(p -> ResponseEntity.ok(mapperService.toPacienteDTO(p)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));
    }
    
    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar paciente por CPF", description = "Retorna um paciente específico pelo seu CPF")
    public ResponseEntity<PacienteDTO> buscarPorCpf(@PathVariable String cpf) {
        Optional<Paciente> paciente = pacienteService.buscarPorCpf(cpf);
        return paciente.map(p -> ResponseEntity.ok(mapperService.toPacienteDTO(p)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar pacientes por nome", description = "Retorna uma lista de pacientes cujo nome contenha o termo buscado")
    public ResponseEntity<List<PacienteDTO>> buscarPorNome(@RequestParam String nome) {
        List<Paciente> pacientes = pacienteService.buscarPorNome(nome);
        List<PacienteDTO> pacienteDTOs = mapperService.toPacienteDTOList(pacientes);
        return ResponseEntity.ok(pacienteDTOs);
    }
    
    @PostMapping
    @Operation(summary = "Cadastrar paciente", description = "Registra um novo paciente no sistema")
    public ResponseEntity<PacienteDTO> cadastrar(@RequestBody @Valid PacienteDTO pacienteDTO) {
        if (pacienteService.verificarCpfExistente(pacienteDTO.getCpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        
        Paciente paciente = mapperService.toPacienteEntity(pacienteDTO);
        Paciente novoPaciente = pacienteService.salvar(paciente);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(mapperService.toPacienteDTO(novoPaciente));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar paciente", description = "Atualiza os dados de um paciente existente")
    public ResponseEntity<PacienteDTO> atualizar(
            @PathVariable Integer id, 
            @RequestBody @Valid PacienteDTO pacienteDTO) {
        
        if (!pacienteService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado");
        }
        
        Optional<Paciente> pacienteExistente = pacienteService.buscarPorCpf(pacienteDTO.getCpf());
        if (pacienteExistente.isPresent() && !pacienteExistente.get().getID().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado para outro paciente");
        }
        
        pacienteDTO.setId(id);
        Paciente paciente = mapperService.toPacienteEntity(pacienteDTO);
        Paciente pacienteAtualizado = pacienteService.atualizar(paciente);
        
        return ResponseEntity.ok(mapperService.toPacienteDTO(pacienteAtualizado));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir paciente", description = "Remove um paciente do sistema")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!pacienteService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado");
        }
        
        try {
            pacienteService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    
    @GetMapping("/{id}/consultas")
    @Operation(summary = "Listar consultas do paciente", description = "Retorna todas as consultas de um determinado paciente")
    public ResponseEntity<List<ConsultaDTO>> listarConsultas(@PathVariable Integer id) {
        if (!pacienteService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado");
        }
        
        return ResponseEntity.ok(mapperService.toConsultaDTOList(pacienteService.listarConsultasDoPaciente(id)));
    }
}