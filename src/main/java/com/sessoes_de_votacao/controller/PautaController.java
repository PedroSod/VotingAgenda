package com.sessoes_de_votacao.controller;

import com.sessoes_de_votacao.DTO.PautaDTO;
import com.sessoes_de_votacao.model.Pauta;
import com.sessoes_de_votacao.service.PautaService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequestMapping("/pauta")
@Controller
public class PautaController {
    private PautaService pautaService;
    private ModelMapper defaultModelMapper;

    public PautaController(PautaService pautaService, ModelMapper defaultModelMapper) {
        this.pautaService = pautaService;
        this.defaultModelMapper = defaultModelMapper;
    }

    @PostMapping
    public ResponseEntity criarPauta(@RequestBody PautaDTO pautaDTO) {
        Pauta pauta = defaultModelMapper.map(pautaDTO, Pauta.class);
        String id = pautaService.save(pauta).getId();
        URI location = UriComponentsBuilder.fromUriString("pauta")
                .path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<PautaDTO> getPautaById(@PathVariable String id) {
        PautaDTO pautaDTO = defaultModelMapper.map(pautaService.findById(id), PautaDTO.class);
        return ResponseEntity.ok(pautaDTO);
    }

}
