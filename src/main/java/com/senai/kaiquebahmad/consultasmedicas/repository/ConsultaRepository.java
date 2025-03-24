package com.senai.kaiquebahmad.consultasmedicas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.senai.kaiquebahmad.consultasmedicas.entity.Consulta;
import com.senai.kaiquebahmad.consultasmedicas.entity.Consulta.Status;
import com.senai.kaiquebahmad.consultasmedicas.entity.Medico;
import com.senai.kaiquebahmad.consultasmedicas.entity.Paciente;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {
    
    List<Consulta> findByMedico(Medico medico);
    
    List<Consulta> findByPaciente(Paciente paciente);
    
    List<Consulta> findByData(Date data);
    
    List<Consulta> findByStatus(Status status);
    
    List<Consulta> findByMedicoAndData(Medico medico, Date data);
    
    List<Consulta> findByDataAndStatus(Date data, Status status);
    
    @Query("SELECT COUNT(c) FROM Consulta c WHERE c.medico.ID = :medicoId AND FUNCTION('DATE', c.data) = CURRENT_DATE")
    int countConsultasHojeByMedicoId(@Param("medicoId") Integer medicoId);
}