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
        put(" https://g1.globo.com/dynamo/educacao/rss2.xml", new String[]{"educação"});
        put("https://g1.globo.com/dynamo/tecnologia/rss2.xml", new String[]{"tecnologia"});
        put(" https://g1.globo.com/dynamo/loterias/rss2.xml", new String[]{"loterias"});
        put("  https://g1.globo.com/dynamo/concursos-e-emprego/rss2.xml", new String[]{"Concursos e Emprego"});
        put("  https://g1.globo.com/dynamo/politica/mensalao/rss2.xml", new String[]{"politica"});
    }};

    public void adicionarFeed(String url, String[] categorias) {
        // Verifica se a URL ou categorias são inválidas
        if (url == null || categorias == null || url.isEmpty() || categorias.length == 0) {
            throw new IllegalArgumentException("URL ou categorias inválidas.");
        }

        // Sincronização para evitar problemas em ambientes multi-thread
        synchronized (RSS_FEEDS) {
            if (RSS_FEEDS.containsKey(url)) {
                // Caso a URL já exista, mescla as categorias
                String[] categoriasExistentes = RSS_FEEDS.get(url);
                List<String> novaListaCategorias = new ArrayList<>(List.of(categoriasExistentes));
                for (String categoria : categorias) {
                    if (!novaListaCategorias.contains(categoria)) {
                        novaListaCategorias.add(categoria);
                    }
                }
                RSS_FEEDS.put(url, novaListaCategorias.toArray(new String[0]));
                System.out.println("Feed atualizado: " + url + " com novas categorias.");
            } else {
                // Caso a URL não exista, adiciona o novo feed
                RSS_FEEDS.put(url, categorias);
                System.out.println("Novo feed adicionado: " + url);
            }
        }
    }

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
