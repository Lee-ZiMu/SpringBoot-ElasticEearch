package org.example.controller;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.example.document.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Version v1.0
 * @Description:
 * @Author Lee
 * @Date 2023/9/10 17:44
 * @Copyright 李子木
 */
@RestController
@RequestMapping("/testTemplate")
public class ElasticSearchTemplateController {

    @Autowired
    private ElasticsearchRestTemplate template;

    // 操作索引
    /**
     * @author Lee
     * @date 2023/9/10 17:47
     * @description 新增索引
     */
    @RequestMapping("/addIndex")
    public void addIndex(){
        // 获得索引操作对象
        IndexOperations indexOperations = template.indexOps(Product.class);
        // 创建索引,注：该方法无法设置索引结构，不推荐使用
        indexOperations.create();
    }

    /**
     * @author Lee
     * @date 2023/9/10 17:47
     * @description 删除索引
     */
    @RequestMapping("/delIndex")
    public void delIndex(){
        // 获得索引操作对象
        IndexOperations indexOperations = template.indexOps(Product.class);
        // 删除索引
        indexOperations.delete();
    }

    // 操作文档
    /**
     * @author Lee
     * @date 2023/9/10 17:48
     * @description 新增/修改
     */
    @RequestMapping("/addDocument")
    public void addDocument(){
        Product product = new Product(10, "es1", "es是一款优秀的搜索引擎");
        template.save(product);
    }

    /**
     * @author Lee
     * @date 2023/9/10 17:49
     * @description 删除
     */
    @RequestMapping("/delDocument")
    public void delDocument(){
        template.delete("10",Product.class);
    }

    /**
     * @author Lee
     * @date 2023/9/10 17:50
     * @description 查询
     */
    @RequestMapping("/searchDocument")
    public void searchDocument(){
        // 1.确定查询方式
        // MatchAllQueryBuilder builder = QueryBuilders.matchAllQuery();
        // TermQueryBuilder builder = QueryBuilders.termQuery("productDesc", "搜索引擎");
        MatchQueryBuilder builder = QueryBuilders.matchQuery("productDesc", "搜索引擎");
        // 2.构建查询条件
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(builder).build();
        // 3.查询
        SearchHits<Product> result = template.search(query, Product.class);
        // 4.处理查询结果
        for (SearchHit<Product> productSearchHit : result) {
            Product product = productSearchHit.getContent();
            System.out.println(product);
        }
    }

    /**
     * @author Lee
     * @date 2023/9/10 17:51
     * @description 复杂查询
     */
    @RequestMapping("/searchDocument2")
    public void searchDocument2() {
        //     String productName = "elasticsearch";
        //     String productDesc = "优秀";
        String productName = null;
        String productDesc = null;

        // 1.确定查询方式
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // 如果没有传入参数，查询所有
        if (productName == null && productDesc == null) {
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            builder.must(matchAllQueryBuilder);
        } else {
            if (productName != null && productName.length() > 0) {
                MatchQueryBuilder queryBuilder1 = QueryBuilders.matchQuery("productName", productName);
                builder.must(queryBuilder1);
            }
            if (productDesc != null && productDesc.length() > 0) {
                MatchQueryBuilder queryBuilder2 = QueryBuilders.matchQuery("productDesc", productDesc);
                builder.must(queryBuilder2);
            }
        }

        // 2.构建查询条件
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(builder).build();

        // 3.查询
        SearchHits<Product> result = template.search(query, Product.class);

        // 4.处理查询结果
        for (SearchHit<Product> productSearchHit : result) {
            Product product = productSearchHit.getContent();
            System.out.println(product);
        }
    }

    /**
     * @author Lee
     * @date 2023/9/10 17:53
     * @description 分页查询
     */
    @RequestMapping("/searchDocumentPage")
    public void searchDocumentPage() {
        // 1.确定查询方式
        MatchAllQueryBuilder builder = QueryBuilders.matchAllQuery();

        // 2.构建查询条件
        // 分页条件
        Pageable pageable = PageRequest.of(0, 3);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .withPageable(pageable)
                .build();

        // 3.查询
        SearchHits<Product> result = template.search(query, Product.class);

        // 4.将查询结果封装为Page对象
        List<Product> content = new ArrayList();
        for (SearchHit<Product> productSearchHit : result) {
            Product product = productSearchHit.getContent();
            content.add(product);
        }
        /**
         * 封装Page对象，参数1：具体数据，参数2：分页条件对象，参数3：总条数
         */
        Page<Product> page = new PageImpl(content, pageable, result.getTotalHits());

        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getContent());
    }

    /**
     * @author Lee
     * @date 2023/9/10 17:54
     * @description 结果排序
     */
    @RequestMapping("/searchDocumentSort")
    public void searchDocumentSort() {
        // 1.确定查询方式
        MatchAllQueryBuilder builder = QueryBuilders.matchAllQuery();

        // 2.构建查询条件
        // 排序条件
        SortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.DESC);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .withSort(sortBuilder)
                .build();

        // 3.查询
        SearchHits<Product> result = template.search(query, Product.class);

        // 4.处理查询结果
        for (SearchHit<Product> productSearchHit : result) {
            Product product = productSearchHit.getContent();
            System.out.println(product);
        }
    }

}
