package com.senai.kaiquebahmad.consultasmedicas.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.kaiquebahmad.consultasmedicas.entity.Consulta;
import com.senai.kaiquebahmad.consultasmedicas.entity.Medico;
import com.senai.kaiquebahmad.consultasmedicas.entity.Paciente;
import com.senai.kaiquebahmad.consultasmedicas.entity.Consulta.Status;
import com.senai.kaiquebahmad.consultasmedicas.repository.ConsultaRepository;

@Service
public class ConsultaService {
    
    @Autowired
    private ConsultaRepository consultaRepository;
    
    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }
    
    public Optional<Consulta> buscarPorId(Integer id) {
        return consultaRepository.findById(id);
    }
    
    public List<Consulta> buscarPorMedico(Medico medico) {
        return consultaRepository.findByMedico(medico);
    }
    
    public List<Consulta> buscarPorPaciente(Paciente paciente) {
        return consultaRepository.findByPaciente(paciente);
    }
    
    public List<Consulta> buscarPorData(Date data) {
        return consultaRepository.findByData(data);
    }
    
    public List<Consulta> buscarPorStatus(Status status) {
        return consultaRepository.findByStatus(status);
    }
    
    public Consulta salvar(Consulta consulta) {
        return consultaRepository.save(consulta);
    }
    
    public void excluir(Integer id) {
        consultaRepository.deleteById(id);
    }
    
    public Consulta atualizarStatus(Integer id, Status novoStatus) {
        Optional<Consulta> consultaOptional = consultaRepository.findById(id);
        if (consultaOptional.isPresent()) {
            Consulta consulta = consultaOptional.get();
            consulta.setStatus(novoStatus);
            return consultaRepository.save(consulta);
        }
        return null;
    }
    
    public boolean verificarDisponibilidadeMedico(Medico medico, Date data) {
        List<Consulta> consultasNaData = consultaRepository.findByMedicoAndData(medico, data);
        return consultasNaData.isEmpty();
    }
    
    public List<Consulta> consultasAgendadasHoje() {
        Date hoje = new Date();
        return consultaRepository.findByDataAndStatus(hoje, Status.AGENDADA);
    }
    
    public List<Consulta> consultasEmAtendimentoHoje() {
        Date hoje = new Date();
        return consultaRepository.findByDataAndStatus(hoje, Status.EM_ATENDIMENTO);
    }
}
