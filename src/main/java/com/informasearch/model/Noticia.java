package com.informasearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import java.time.LocalDate;

@Document(indexName = "noticias")  // Define o índice do Elasticsearch
public class Noticia {

    @Id
    private String id;  // Campo de identificação

    private String titulo;
    private String link;
    private String descricao;
    private LocalDate dataPublicacao;

    // Construtor com todos os parâmetros, exceto o id
    public Noticia(String titulo, String link, String descricao, LocalDate dataPublicacao) {
        this.titulo = titulo;
        this.link = link;
        this.descricao = descricao;
        this.dataPublicacao = dataPublicacao;
    }

    // Getters e setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }
}
