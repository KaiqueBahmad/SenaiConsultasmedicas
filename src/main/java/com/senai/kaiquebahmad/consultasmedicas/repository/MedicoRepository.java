package com.senai.kaiquebahmad.consultasmedicas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.senai.kaiquebahmad.consultasmedicas.entity.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    
    Optional<Medico> findByCRM(String crm);
    
    List<Medico> findByNomeContainingIgnoreCase(String nome);
    
    List<Medico> findByEspecialidadeIgnoreCase(String especialidade);
    
    boolean existsByCRM(String crm);
    
    @Query("SELECT DISTINCT m.especialidade FROM Medico m ORDER BY m.especialidade")
    List<String> findDistinctEspecialidades();
}