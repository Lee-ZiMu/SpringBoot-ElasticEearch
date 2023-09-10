package org.example.repository;

import org.example.document.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @Version v1.0
 * @Description:
 * @Author Lee
 * @Date 2023/9/10 16:55
 * @Copyright 李子木
 */
public interface ProductRepository extends ElasticsearchRepository<Product, Integer> {

    /**
     * @author Lee
     * @date 2023/9/10 17:11
     * @description DSL查询
     */
    @Query("{" +
            "   \"match\": {" +
            "    \"productDesc\": \"?0\"" +
            "   }" +
            "  }")
    List<Product> findByProductDescMatch(String keyword);

    @Query("{" +
            " \"match\": {" +
            "  \"productDesc\": {" +
            "   \"query\": \"?0\"," +
            "   \"fuzziness\": 1" +         // 自动纠错
            "  }" +
            " }" +
            "}")
    List<Product> findByProductDescFuzzy(String keyword);

    /**
     * @author Lee
     * @date 2023/9/10 17:12
     * @description 按照规则命名查询  查询方法以findBy开头，涉及查询条件时，条件的属性用条件关键字连接。
     */
    List<Product> findByProductName(String productName);

    List<Product> findByProductNameOrProductDesc(String productName, String productDesc);

    List<Product> findByIdBetween(Integer startId,Integer endId);

    /**
     * @author Lee
     * @date 2023/9/10 17:18
     * @description 分页查询
     */
    Page<Product> findByProductDesc(String productDesc, Pageable pageable);


    /**
     * @author Lee
     * @date 2023/9/10 17:56
     * @description 高亮
     */
    @Highlight(fields = {@HighlightField(name = "title"), @HighlightField(name = "content")})
    List<SearchHit<Product>> findByTitleMatchesOrContentMatches(String title, String content);

}
