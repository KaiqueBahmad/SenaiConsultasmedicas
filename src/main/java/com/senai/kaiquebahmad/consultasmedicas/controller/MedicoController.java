package com.senai.kaiquebahmad.consultasmedicas.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.senai.kaiquebahmad.consultasmedicas.dto.MedicoDTO;
import com.senai.kaiquebahmad.consultasmedicas.entity.Medico;
import com.senai.kaiquebahmad.consultasmedicas.service.MapperService;
import com.senai.kaiquebahmad.consultasmedicas.service.MedicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medicos")
@Tag(name = "Médicos", description = "API para gerenciamento de médicos")
public class MedicoController {
    
    @Autowired
    private MedicoService medicoService;
    
    @Autowired
    private MapperService mapperService;
    
    @GetMapping
    @Operation(summary = "Listar todos os médicos", description = "Retorna uma lista com todos os médicos cadastrados")
    public ResponseEntity<List<MedicoDTO>> listarTodos() {
        List<Medico> medicos = medicoService.listarTodos();
        List<MedicoDTO> medicoDTOs = mapperService.toMedicoDTOList(medicos);
        return ResponseEntity.ok(medicoDTOs);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar médico por ID", description = "Retorna um médico específico pelo seu ID")
    public ResponseEntity<MedicoDTO> buscarPorId(@PathVariable Integer id) {
        Optional<Medico> medico = medicoService.buscarPorId(id);
        return medico.map(m -> ResponseEntity.ok(mapperService.toMedicoDTO(m)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado"));
    }
    
    @GetMapping("/crm/{crm}")
    @Operation(summary = "Buscar médico por CRM", description = "Retorna um médico específico pelo seu CRM")
    public ResponseEntity<MedicoDTO> buscarPorCrm(@PathVariable String crm) {
        Optional<Medico> medico = medicoService.buscarPorCrm(crm);
        return medico.map(m -> ResponseEntity.ok(mapperService.toMedicoDTO(m)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado"));
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar médicos por nome", description = "Retorna uma lista de médicos cujo nome contenha o termo buscado")
    public ResponseEntity<List<MedicoDTO>> buscarPorNome(@RequestParam String nome) {
        List<Medico> medicos = medicoService.buscarPorNome(nome);
        List<MedicoDTO> medicoDTOs = mapperService.toMedicoDTOList(medicos);
        return ResponseEntity.ok(medicoDTOs);
    }
    
    @GetMapping("/especialidade/{especialidade}")
    @Operation(summary = "Listar médicos por especialidade", description = "Retorna uma lista de médicos de uma determinada especialidade")
    public ResponseEntity<List<MedicoDTO>> buscarPorEspecialidade(@PathVariable String especialidade) {
        List<Medico> medicos = medicoService.buscarPorEspecialidade(especialidade);
        List<MedicoDTO> medicoDTOs = mapperService.toMedicoDTOList(medicos);
        return ResponseEntity.ok(medicoDTOs);
    }
    
    @GetMapping("/especialidades")
    @Operation(summary = "Listar todas as especialidades", description = "Retorna uma lista com todas as especialidades cadastradas")
    public ResponseEntity<List<String>> listarEspecialidades() {
        return ResponseEntity.ok(medicoService.listarEspecialidades());
    }
    
    @PostMapping
    @Operation(summary = "Cadastrar médico", description = "Registra um novo médico no sistema")
    public ResponseEntity<MedicoDTO> cadastrar(@RequestBody @Valid MedicoDTO medicoDTO) {
        if (medicoService.verificarCrmExistente(medicoDTO.getCrm())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CRM já cadastrado");
        }
        
        Medico medico = mapperService.toMedicoEntity(medicoDTO);
        Medico novoMedico = medicoService.salvar(medico);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(mapperService.toMedicoDTO(novoMedico));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar médico", description = "Atualiza os dados de um médico existente")
    public ResponseEntity<MedicoDTO> atualizar(
            @PathVariable Integer id, 
            @RequestBody @Valid MedicoDTO medicoDTO) {
        
        if (!medicoService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado");
        }
        
        Optional<Medico> medicoExistente = medicoService.buscarPorCrm(medicoDTO.getCrm());
        if (medicoExistente.isPresent() && !medicoExistente.get().getID().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CRM já cadastrado para outro médico");
        }
        
        medicoDTO.setId(id);
        Medico medico = mapperService.toMedicoEntity(medicoDTO);
        Medico medicoAtualizado = medicoService.atualizar(medico);
        
        return ResponseEntity.ok(mapperService.toMedicoDTO(medicoAtualizado));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir médico", description = "Remove um médico do sistema")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!medicoService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado");
        }
        
        try {
            medicoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    
    @GetMapping("/{id}/consultas")
    @Operation(summary = "Listar consultas do médico", description = "Retorna todas as consultas de um determinado médico")
    public ResponseEntity<List<ConsultaDTO>> listarConsultas(@PathVariable Integer id) {
        if (!medicoService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado");
        }
        
        return ResponseEntity.ok(mapperService.toConsultaDTOList(medicoService.listarConsultasDoMedico(id)));
    }
    
    @GetMapping("/{id}/consultas/hoje/quantidade")
    @Operation(summary = "Contar consultas de hoje", description = "Retorna o número de consultas agendadas para o médico no dia atual")
    public ResponseEntity<Integer> contarConsultasHoje(@PathVariable Integer id) {
        if (!medicoService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado");
        }
        
        return ResponseEntity.ok(medicoService.contarConsultasHoje(id));
    }
}