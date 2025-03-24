package com.senai.kaiquebahmad.consultasmedicas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.kaiquebahmad.consultasmedicas.entity.Consulta;
import com.senai.kaiquebahmad.consultasmedicas.entity.Paciente;
import com.senai.kaiquebahmad.consultasmedicas.repository.ConsultaRepository;
import com.senai.kaiquebahmad.consultasmedicas.repository.PacienteRepository;

@Service
public class PacienteService {
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private ConsultaRepository consultaRepository;
    
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }
    
    public Optional<Paciente> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }
    
    public Optional<Paciente> buscarPorCpf(String cpf) {
        return pacienteRepository.findByCPF(cpf);
    }
    
    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public Paciente salvar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }
    
    public void excluir(Integer id) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        if (pacienteOptional.isPresent()) {
            Paciente paciente = pacienteOptional.get();
            List<Consulta> consultas = consultaRepository.findByPaciente(paciente);
            
            if (consultas.isEmpty()) {
                pacienteRepository.deleteById(id);
            } else {
                throw new RuntimeException("Não é possível excluir paciente com consultas registradas");
            }
        }
    }
    
    public Paciente atualizar(Paciente paciente) {
        if (pacienteRepository.existsById(paciente.getID())) {
            return pacienteRepository.save(paciente);
        }
        return null;
    }
    
    public boolean verificarCpfExistente(String cpf) {
        return pacienteRepository.existsByCPF(cpf);
    }
    
    public List<Consulta> listarConsultasDoPaciente(Integer pacienteId) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(pacienteId);
        if (pacienteOptional.isPresent()) {
            Paciente paciente = pacienteOptional.get();
            return consultaRepository.findByPaciente(paciente);
        }
        return List.of();
    }
}
