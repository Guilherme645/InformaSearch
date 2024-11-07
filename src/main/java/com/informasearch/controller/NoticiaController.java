package com.informasearch.controller;

import com.informasearch.model.Noticia;
import com.informasearch.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/noticias")
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    // Endpoint para obter notícias do dia de múltiplos feeds RSS
    @GetMapping("/hoje")
    public List<Noticia> obterNoticiasDoDia() {
        return noticiaService.obterNoticiasDoDia();
    }

    // Endpoint para buscar notícias com termo e paginar os resultados
    @GetMapping("/buscar")
    public List<Noticia> buscarNoticiasPorTermo(
            @RequestParam String termo,
            @RequestParam int page,
            @RequestParam int pageSize) {
        return noticiaService.buscarNoticiasPorTermo(termo, page, pageSize);
    }
}
