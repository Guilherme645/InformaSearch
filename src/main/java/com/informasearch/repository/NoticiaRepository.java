package com.informasearch.repository;

import com.informasearch.model.Noticia;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepository extends ElasticsearchRepository<Noticia, String> {
}
