package com.desafios.projetos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Regiao implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "regiao",fetch = FetchType.EAGER)
    private Geracao geracao;

    @OneToOne(mappedBy = "regiao",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Compra compra;

    @OneToOne(mappedBy = "regiao",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private PrecoMedio precoMedio;

    private String sigla;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Agente agente;
}
