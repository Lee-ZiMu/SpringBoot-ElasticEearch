package org.example.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Version v1.0
 * @Description:
 * @Author Lee
 * @Date 2023/9/10 16:53
 * @Copyright 李子木
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "product", createIndex = true)
public class Product {

    @Id
    @Field(type = FieldType.Integer, store = true, index = true)
    private Integer id;

    @Field(type = FieldType.Text, store = true, index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String productName;

    @Field(type = FieldType.Text, store = true, index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String productDesc;


    /*
            @Document：标记在类上，标记实体类为文档对象，一般有如下属性：

            indexName：对应索引的名称

            createIndex：是否自动创建索引

            @Id：标记在成员变量上，标记一个字段为主键，该字段的值会同步到ES该文档的id值。

            @Field：标记在成员变量上，标记为文档中的域，一般有如下属性：

            type：域的类型

            index：是否创建索引，默认是 true

            store：是否单独存储，默认是 false

            analyzer：分词器

            searchAnalyzer：搜索时的分词器
     */

}
