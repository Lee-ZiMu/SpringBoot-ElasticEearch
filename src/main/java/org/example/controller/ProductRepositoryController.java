package org.example.controller;

import org.example.document.Product;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @Version v1.0
 * @Description:
 * @Author Lee
 * @Date 2023/9/10 16:57
 * @Copyright 李子木
 */
@RestController
@RequestMapping("/test")
public class ProductRepositoryController {

    @Autowired
    private ProductRepository repository;

    @RequestMapping("/addDocument")
    public void addDocument(){
        Product product = new Product(1, "测试1", "Elaticsearch，简称为es，es是一个开源的高扩展的分布式全文检索引擎，它可以近乎实时的存储、检索数据;本身扩展性很好，可以扩展到上百台服务器，处理PB级别(大数据时代）的数据");
        repository.save(product);
    }

    @RequestMapping("/updateDocument")
    public void updateDocument(){
        Product product = new Product(1, "测试1", "Elaticsearch，简称为es，es是一个开源的高扩展的分布式全文检索引擎，它可以近乎实时的存储、检索数据;本身扩展性很好，可以扩展到上百台服务器，处理PB级别(大数据时代）的数据");
        repository.save(product);
    }

    @RequestMapping("/findAllDocument")
    public void findAllDocument(){
        Iterable<Product> all = repository.findAll();
        for (Product product : all) {
            System.out.println(product);
        }
    }

    @RequestMapping("/findDocumentById")
    public void findDocumentById(){
        Optional<Product> product = repository.findById(1);
        System.out.println(product.get());
    }

    @RequestMapping("/deleteDocument")
    public void deleteDocument(){
        repository.deleteById(1);
    }

    /**
     * @author Lee
     * @date 2023/9/10 17:39
     * @description DSL查询
     */
    @RequestMapping("/findByProductDescMatch")
    public void findByProductDescMatch(){
        repository.findByProductDescMatch("测试1");
    }

    @RequestMapping("/findByProductDescFuzzy")
    public void findByProductDescFuzzy(){
        repository.findByProductDescFuzzy("测试1");
    }


    /**
     * @author Lee
     * @date 2023/9/10 17:15
     * @description 分页查询
     */
    public void findPage(){
        // 参数1：页数   参数2：每页条数
        Pageable pageable = PageRequest.of(1, 3);
        Page<Product> page = repository.findAll(pageable);
        System.out.println("总条数"+page.getTotalElements());
        System.out.println("总页数"+page.getTotalPages());
        System.out.println("数据"+page.getContent());
    }

    public void findPage2(){
        Pageable pageable = PageRequest.of(1, 2);
        Page<Product> page = repository.findByProductDesc("测试1", pageable);
        System.out.println("总条数"+page.getTotalElements());
        System.out.println("总页数"+page.getTotalPages());
        System.out.println("数据"+page.getContent());
    }


    /**
     * @author Lee
     * @date 2023/9/10 17:39
     * @description 结果排序
     */
    public void testFindSort(){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Iterable<Product> all = repository.findAll(sort);
        for (Product product : all) {
            System.out.println(product);
        }
    }

    /**
     * @author Lee
     * @date 2023/9/10 17:41
     * @description 分页加排序
     */
    public void testFindPage2(){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(0, 2,sort);
        Page<Product> page = repository.findByProductDesc("测试1", pageable);
        System.out.println("总条数"+page.getTotalElements());
        System.out.println("总页数"+page.getTotalPages());
        System.out.println("数据"+page.getContent());
    }

}
