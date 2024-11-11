package com.informasearch.controller;

import com.informasearch.model.Noticia;
import com.informasearch.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/noticiasC")
    public ResponseEntity<List<Noticia>> buscarNoticias(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String categoria,
            @RequestParam int page,
            @RequestParam int pageSize) {
        List<Noticia> noticias = noticiaService.buscarNoticias(termo, categoria, page, pageSize);
        return ResponseEntity.ok(noticias);
    }

    @PostMapping("/adicionar-feed")
    public ResponseEntity<String> adicionarFeed(@RequestBody AdicionarFeedRequest request) {
        try {
            // Adiciona o feed usando o serviço
            noticiaService.adicionarFeed(request.getUrl(), request.getCategorias());
            return ResponseEntity.ok("Feed adicionado/atualizado com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao adicionar o feed: " + e.getMessage());
        }
    }
}
