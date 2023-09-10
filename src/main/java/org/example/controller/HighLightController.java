package org.example.controller;

import org.example.document.Product;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Version v1.0
 * @Description:
 * @Author Lee
 * @Date 2023/9/10 17:57
 * @Copyright 李子木
 */
@RestController
@RequestMapping("/testHighLight")
public class HighLightController {

    @Autowired
    private ProductRepository repository;
    
    /** 
     * @author Lee
     * @date 2023/9/10 17:58
     * @description 高亮
     */
    @RequestMapping("/highLightSearch")
    public List<Product> highLightSearch(String keyword){
        List<SearchHit<Product>> result = repository.findByTitleMatchesOrContentMatches(keyword, keyword);
        // 处理结果，封装为Product类型的集合
        List<Product> newsList = new ArrayList();
        for (SearchHit<Product> productSearchHit : result) {
            Product Product = productSearchHit.getContent();
            // 高亮字段
            Map<String, List<String>> highlightFields = productSearchHit.getHighlightFields();
            if (highlightFields.get("title") != null){
                Product.setProductName(highlightFields.get("title").get(0));
            }
            if (highlightFields.get("content") != null){
                Product.setProductDesc(highlightFields.get("content").get(0));
            }
            newsList.add(Product);
        }
        return newsList;
    }
}
