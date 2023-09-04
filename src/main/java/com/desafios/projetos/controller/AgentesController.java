package com.desafios.projetos.controller;

import com.desafios.projetos.model.*;
import com.desafios.projetos.repository.*;

import com.google.gson.Gson;
import io.codejournal.maven.xsd2java.Agentes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/agentes")
public class AgentesController {


    @Autowired
    private AgentesRepository agentesRepository;

    @Autowired
    private AgenteRepository agenteRepository;

    @Autowired
    private RegiaoRepository regiaoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private PrecoMedioRepository precoMedioRepository;

    @Autowired
    private GeracaoRepository geracaoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<?> handleXMLUpload(@RequestParam("xmlFile") MultipartFile xmlFile) throws JAXBException, JAXBException {
        try {

            // Verifique se o arquivo enviado é um XML
            if (!Objects.equals(xmlFile.getContentType(), "application/xml")) {
                return ResponseEntity.badRequest().body("O arquivo deve ser um XML.");
            }

            String xmlContent = new String(xmlFile.getBytes(), "UTF-8");
            JAXBContext jaxbContext = JAXBContext.newInstance(Agentes.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Agentes agentes = (Agentes) unmarshaller.unmarshal(new ByteArrayInputStream(xmlFile.getBytes()));

            for (Agentes.Agente agente : agentes.getAgente()){
                System.out.println("codigo agente = " + agente.getCodigo());
            }

            AgentesEntity agentesEntity = convertAgenteToAgenteEntity(agentes);

            return ResponseEntity.ok(salvarTodasEntidades(agentesEntity));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o arquivo XML");
        }
    }


    @Transactional
    public AgentesEntity salvarTodasEntidades(AgentesEntity agentes) {

        // Salvar a entidade AgentesEntity
        AgentesEntity agentesEntity = agentesRepository.saveAndFlush(agentes);

        for (Agente agente : agentes.getAgente()) {
            agente.setAgentes(agentesEntity);
            Agente savedAgente = agenteRepository.saveAndFlush(agente);

            for (Regiao regiao : agente.getRegiao()) {
                Compra compra = regiao.getCompra();
                PrecoMedio precoMedio = regiao.getPrecoMedio();
                Geracao geracao = regiao.getGeracao();

                // Salve as entidades associadas antes de associá-las à entidade Regiao
                Compra compraSaved = compraRepository.saveAndFlush(compra);
                Geracao geracaoSaved = geracaoRepository.saveAndFlush(geracao);
                PrecoMedio precoMedioSaved = precoMedioRepository.saveAndFlush(precoMedio);

                // Associe as entidades associadas à entidade Regiao
                regiao.setAgente(savedAgente);
                regiao.setGeracao(geracaoSaved);
                regiao.setCompra(compraSaved);
                regiao.setPrecoMedio(precoMedioSaved);

                // Salve a entidade Regiao após ter associado todas as entidades associadas
                regiaoRepository.saveAndFlush(regiao);
            }
        }
        System.out.println("SAlvo com sucesso!!!");
        return agentesEntity;
    }

    private AgentesEntity convertAgenteToAgenteEntity(Agentes agentes){
        AgentesEntity agentesEntity = new AgentesEntity();
        List<Agente> agentesToSave = new ArrayList<>();

        for (io.codejournal.maven.xsd2java.Agentes.Agente agenteFromXml : agentes.getAgente()) {
            com.desafios.projetos.model.Agente agenteToSave = new com.desafios.projetos.model.Agente();
            List<Regiao> regioesEntity = new ArrayList<>();
            // Converter a lista de Regiao para RegiaoEntity

            for (Agentes.Agente.Regiao regiao : agenteFromXml.getRegiao()) {
                Regiao regiaoEntity = new Regiao();
                Compra compraEntity = new Compra();
                PrecoMedio precoMedioEntity = new PrecoMedio();
                Geracao geracaoEntity = new Geracao();

                geracaoEntity.setValor(regiao.getGeracao().getValor());
                precoMedioEntity.setValor(regiao.getPrecoMedio().getValor());
                compraEntity.setValor(regiao.getCompra().getValor());

                regiaoEntity.setGeracao(geracaoEntity);
                regiaoEntity.setCompra(compraEntity);
                regiaoEntity.setPrecoMedio(precoMedioEntity);
                regiaoEntity.setSigla(regiao.getSigla());
                regioesEntity.add(regiaoEntity);
            }

            agenteToSave.setRegiao(regioesEntity);
            agenteToSave.setData(convertDataTime(agenteFromXml.getData()));
            agenteToSave.setCodigo(agenteFromXml.getCodigo());
            agentesToSave.add(agenteToSave);
        }

        agentesEntity.setAgente(agentesToSave);
        agentesEntity.setVersao(agentes.getVersao());

        return agentesEntity;
    }

    private LocalDateTime convertDataTime(XMLGregorianCalendar date){

        GregorianCalendar gregorianCalendar = date.toGregorianCalendar();
        // 2. Converter GregorianCalendar para java.util.Date
        Date dateNow = gregorianCalendar.getTime();
        // 3. Converter java.util.Date para LocalDateTime
        return dateNow.toInstant().atZone(gregorianCalendar.getTimeZone().toZoneId()).toLocalDateTime();

    }
}
