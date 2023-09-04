package com.desafios.projetos.repository;

import com.desafios.projetos.model.Geracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeracaoRepository extends JpaRepository<Geracao, Long> {

}
