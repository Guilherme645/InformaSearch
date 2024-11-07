package com.informasearch.service;

import com.informasearch.model.Noticia;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticiaService {

    // Lista de URLs de feeds RSS para obter notícias de múltiplas fontes
    private static final List<String> RSS_FEED_URLS = List.of(
            "https://portalnoticiasr7.webnode.page/rss/noticias.xml",
            "https://www.uol.com.br/rss.xml",
            "https://g1.globo.com/rss/g1/",
            "https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml"  // Exemplo de novo RSS
            // Adicione outras URLs de RSS aqui
    );

    // Método para obter notícias do dia de todos os feeds RSS
    public List<Noticia> obterNoticiasDoDia() {
        List<Noticia> todasNoticias = new ArrayList<>();
        for (String rssUrl : RSS_FEED_URLS) {
            todasNoticias.addAll(obterNoticiasDeRSS(rssUrl));
        }
        return todasNoticias;
    }

    // Método auxiliar para obter notícias de um feed RSS específico
    private List<Noticia> obterNoticiasDeRSS(String rssUrl) {
        List<Noticia> noticias = new ArrayList<>();
        try {
            URL url = new URL(rssUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document doc = factory.newDocumentBuilder().parse(new org.xml.sax.InputSource(reader));

            NodeList items = doc.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                String titulo = item.getElementsByTagName("title").item(0).getTextContent();
                String link = item.getElementsByTagName("link").item(0).getTextContent();
                String descricao = item.getElementsByTagName("description").item(0).getTextContent();
                descricao = descricao.replaceAll("<[^>]*>", ""); // Remove tags HTML

                Noticia noticia = new Noticia(titulo, link, descricao, LocalDate.now());
                noticias.add(noticia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noticias;
    }

    // Método para buscar e paginar notícias filtradas por termo
    public List<Noticia> buscarNoticiasPorTermo(String termo, int page, int pageSize) {
        List<Noticia> todasNoticias = obterNoticiasDoDia();
        List<Noticia> noticiasFiltradas = new ArrayList<>();

        // Filtra as notícias pelo termo de busca
        for (Noticia noticia : todasNoticias) {
            if (noticia.getTitulo().toLowerCase().contains(termo.toLowerCase()) ||
                    noticia.getDescricao().toLowerCase().contains(termo.toLowerCase())) {
                noticiasFiltradas.add(noticia);
            }
        }

        // Aplica a paginação
        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, noticiasFiltradas.size());
        if (fromIndex > noticiasFiltradas.size()) {
            return new ArrayList<>(); // Retorna lista vazia se a página está fora do intervalo
        }
        return noticiasFiltradas.subList(fromIndex, toIndex);
    }
}
