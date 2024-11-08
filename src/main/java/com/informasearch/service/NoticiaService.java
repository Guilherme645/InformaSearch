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
import java.util.Map;
import java.util.HashMap;

@Service
public class NoticiaService {

    // Lista de URLs de feeds RSS com categorias associadas
    private static final Map<String, String[]> RSS_FEEDS = new HashMap<>() {{
        put("https://portalnoticiasr7.webnode.page/rss/noticias.xml", new String[]{"geral"});
        put("https://www.uol.com.br/rss.xml", new String[]{"geral", "esporte"});
        put("https://g1.globo.com/rss/g1/", new String[]{"tecnologia", "politica"});
        put("https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml", new String[]{"tecnologia"});
    }};

    // Método para obter notícias do dia de todos os feeds RSS
    public List<Noticia> obterNoticiasDoDia() {
        List<Noticia> todasNoticias = new ArrayList<>();

        for (Map.Entry<String, String[]> feed : RSS_FEEDS.entrySet()) {
            todasNoticias.addAll(obterNoticiasDeRSS(feed.getKey(), feed.getValue()));
        }
        return todasNoticias;
    }

    private List<Noticia> obterNoticiasDeRSS(String rssUrl, String[] categorias) {
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

                // Define o favicon e o siteUrl para cada notícia com base no feed
                String faviconUrl = rssUrl + "/favicon.ico";

                // Associa as categorias do feed à notícia
                for (String categoria : categorias) {
                    Noticia noticia = new Noticia(titulo, link, descricao, LocalDate.now(), faviconUrl, rssUrl, categoria);
                    noticias.add(noticia);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noticias;
    }

    // Método para buscar e filtrar notícias por categoria e termo
    public List<Noticia> buscarNoticias(String termo, String categoria, int page, int pageSize) {
        List<Noticia> todasNoticias = obterNoticiasDoDia();
        List<Noticia> noticiasFiltradas = new ArrayList<>();

        // Filtra as notícias pelo termo e pela categoria
        for (Noticia noticia : todasNoticias) {
            if ((categoria == null || noticia.getCategoria().equalsIgnoreCase(categoria)) &&
                    (termo == null || noticia.getTitulo().toLowerCase().contains(termo.toLowerCase()) ||
                            noticia.getDescricao().toLowerCase().contains(termo.toLowerCase()))) {
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
