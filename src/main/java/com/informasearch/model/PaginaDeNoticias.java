package com.informasearch.model;

import java.util.List;

public class PaginaDeNoticias {
    private List<Noticia> noticias;
    private int totalResultados;

    public PaginaDeNoticias(List<Noticia> noticias, int totalResultados) {
        this.noticias = noticias;
        this.totalResultados = totalResultados;
    }

    public List<Noticia> getNoticias() {
        return noticias;
    }

    public void setNoticias(List<Noticia> noticias) {
        this.noticias = noticias;
    }

    public int getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(int totalResultados) {
        this.totalResultados = totalResultados;
    }
}
