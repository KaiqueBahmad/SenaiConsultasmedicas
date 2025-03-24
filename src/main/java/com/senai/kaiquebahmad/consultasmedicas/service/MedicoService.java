package com.senai.kaiquebahmad.consultasmedicas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.kaiquebahmad.consultasmedicas.entity.Consulta;
import com.senai.kaiquebahmad.consultasmedicas.entity.Medico;
import com.senai.kaiquebahmad.consultasmedicas.repository.ConsultaRepository;
import com.senai.kaiquebahmad.consultasmedicas.repository.MedicoRepository;

@Service
public class MedicoService {
    
    @Autowired
    private MedicoRepository medicoRepository;
    
    @Autowired
    private ConsultaRepository consultaRepository;
    
    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }
    
    public Optional<Medico> buscarPorId(Integer id) {
        return medicoRepository.findById(id);
    }
    
    public Optional<Medico> buscarPorCrm(String crm) {
        return medicoRepository.findByCRM(crm);
    }
    
    public List<Medico> buscarPorNome(String nome) {
        return medicoRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public List<Medico> buscarPorEspecialidade(String especialidade) {
        return medicoRepository.findByEspecialidadeIgnoreCase(especialidade);
    }
    
    public Medico salvar(Medico medico) {
        return medicoRepository.save(medico);
    }
    
    public void excluir(Integer id) {
        Optional<Medico> medicoOptional = medicoRepository.findById(id);
        if (medicoOptional.isPresent()) {
            Medico medico = medicoOptional.get();
            List<Consulta> consultas = consultaRepository.findByMedico(medico);
            
            if (consultas.isEmpty()) {
                medicoRepository.deleteById(id);
            } else {
                throw new RuntimeException("Não é possível excluir médico com consultas registradas");
            }
        }
    }
    
    public Medico atualizar(Medico medico) {
        if (medicoRepository.existsById(medico.getID())) {
            return medicoRepository.save(medico);
        }
        return null;
    }
    
    public boolean verificarCrmExistente(String crm) {
        return medicoRepository.existsByCRM(crm);
    }
    
    public List<Consulta> listarConsultasDoMedico(Integer medicoId) {
        Optional<Medico> medicoOptional = medicoRepository.findById(medicoId);
        if (medicoOptional.isPresent()) {
            Medico medico = medicoOptional.get();
            return consultaRepository.findByMedico(medico);
        }
        return List.of();
    }
    
    public int contarConsultasHoje(Integer medicoId) {
        return consultaRepository.countConsultasHojeByMedicoId(medicoId);
    }
    
    public List<String> listarEspecialidades() {
        return medicoRepository.findDistinctEspecialidades();
    }
}