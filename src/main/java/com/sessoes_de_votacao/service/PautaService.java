package com.sessoes_de_votacao.service;

import com.sessoes_de_votacao.model.Pauta;
import com.sessoes_de_votacao.repository.PautaRepository;
import org.springframework.stereotype.Service;

@Service
public class PautaService {

    private PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Pauta save(Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    public Pauta findById(String id) {
        return pautaRepository.findById(id).orElseThrow();
    }
}
