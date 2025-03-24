package com.senai.kaiquebahmad.consultasmedicas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senai.kaiquebahmad.consultasmedicas.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    
    Optional<Paciente> findByCPF(String cpf);
    
    List<Paciente> findByNomeContainingIgnoreCase(String nome);
    
    boolean existsByCPF(String cpf);
}