package com.desafios.projetos.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Compra implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ElementCollection
    @CollectionTable(name = "valores_compra", joinColumns = @JoinColumn(name = "valor_id"))
    @Column(name = "valor")
    private List<BigDecimal> valor;

    @OneToOne(fetch = FetchType.EAGER)
    private Regiao regiao;
}
