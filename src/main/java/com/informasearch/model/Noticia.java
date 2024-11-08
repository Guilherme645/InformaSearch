package com.informasearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;

@Document(indexName = "noticias")
public class Noticia {

    @Id
    private String id;  // Propriedade de identificação

    private String titulo;
    private String link;
    private String descricao;
    private LocalDate dataPublicacao;
    private String faviconUrl;
    private String sourceUrl;
    private String categoria; // Novo campo categoria

    // Construtor que inclui o campo 'id'
    public Noticia(String id, String titulo, String link, String descricao, LocalDate dataPublicacao, String faviconUrl, String sourceUrl, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.link = link;
        this.descricao = descricao;
        this.dataPublicacao = dataPublicacao;
        this.faviconUrl = faviconUrl;
        this.sourceUrl = sourceUrl;
        this.categoria = categoria; // Inicializa categoria
    }

    // Construtor sem o campo 'id'
    public Noticia(String titulo, String link, String descricao, LocalDate dataPublicacao, String faviconUrl, String sourceUrl, String categoria) {
        this.titulo = titulo;
        this.link = link;
        this.descricao = descricao;
        this.dataPublicacao = dataPublicacao;
        this.faviconUrl = faviconUrl;
        this.sourceUrl = sourceUrl;
        this.categoria = categoria; // Inicializa categoria
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

    public String getFaviconUrl() {
        return faviconUrl;
    }

    public void setFaviconUrl(String faviconUrl) {
        this.faviconUrl = faviconUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
