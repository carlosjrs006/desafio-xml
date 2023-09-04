package com.desafios.projetos.repository;

import com.desafios.projetos.model.AgentesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentesRepository extends JpaRepository<AgentesEntity, Long> {

}
