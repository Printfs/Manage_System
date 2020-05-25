package com.hang.manage.system.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Data
@ToString
@Document(indexName="myliteratures",type="Literature",indexStoreType="fs",shards=5,replicas=1,refreshInterval="-1")
public class Literature implements Serializable{
    private static final long serialVersionUID = 7754569732674385528L;
    @Id
    private String id;

    /**
     * 词牌名，或者叫标题
     */
    @Field(analyzer = "ik_max_word")
    private String title;
    /**
     * 内容
     */
    @Field(analyzer = "ik_max_word")
    private String content;

    //作者
    private String auther;

    private String datetime;
}
