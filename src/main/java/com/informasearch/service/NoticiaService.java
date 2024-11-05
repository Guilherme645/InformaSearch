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

    private static final String RSS_FEED_URL = "https://portalnoticiasr7.webnode.page/rss/noticias.xml";

    // Método para obter notícias do dia do feed RSS
    public List<Noticia> obterNoticiasDoDia() {
        List<Noticia> noticias = new ArrayList<>();
        try {
            // Configurando a conexão HTTP com o User-Agent e a codificação UTF-8
            URL url = new URL(RSS_FEED_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            // Converte o XML em um objeto Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document doc = factory.newDocumentBuilder().parse(new org.xml.sax.InputSource(reader));

            // Extrai cada item (notícia) do XML
            NodeList items = doc.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                String titulo = item.getElementsByTagName("title").item(0).getTextContent();
                String link = item.getElementsByTagName("link").item(0).getTextContent();
                String descricao = item.getElementsByTagName("description").item(0).getTextContent();
                descricao = descricao.replaceAll("<[^>]*>", ""); // Remove tags HTML

                // Cria uma instância de Noticia e adiciona à lista
                Noticia noticia = new Noticia(titulo, link, descricao, LocalDate.now());
                noticias.add(noticia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noticias;
    }

    // Método para buscar notícias por termo (filtragem em memória)
    public List<Noticia> buscarNoticiasPorTermo(String termo) {
        List<Noticia> noticias = obterNoticiasDoDia();
        List<Noticia> noticiasFiltradas = new ArrayList<>();

        for (Noticia noticia : noticias) {
            if (noticia.getTitulo().toLowerCase().contains(termo.toLowerCase()) ||
                    noticia.getDescricao().toLowerCase().contains(termo.toLowerCase())) {
                noticiasFiltradas.add(noticia);
            }
        }
        return noticiasFiltradas;
    }
}
