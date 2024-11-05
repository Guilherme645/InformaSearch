package com.informasearch.controller;

import com.informasearch.model.Noticia;
import com.informasearch.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/noticias")
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    @GetMapping("/hoje")
    public List<Noticia> getNoticiasDoDia() {
        return noticiaService.obterNoticiasDoDia();
    }

    @GetMapping("/buscar")
    public List<Noticia> buscarNoticias(@RequestParam String termo) {
        return noticiaService.buscarNoticiasPorTermo(termo);
    }
}
