package com.hang.manage.system.repository;

import com.hang.manage.system.entity.Literature;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface LiteratureRepository extends ElasticsearchRepository<Literature, String> {
}
