package com.desafios.projetos.repository;

import com.desafios.projetos.model.PrecoMedio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecoMedioRepository extends JpaRepository<PrecoMedio, Long> {

}
