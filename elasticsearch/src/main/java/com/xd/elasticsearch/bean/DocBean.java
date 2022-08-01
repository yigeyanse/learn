package com.xd.elasticsearch.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Setting(shards = 3, replicas = 0,refreshInterval = "30s")
@Document(indexName = "test-data")
public class DocBean {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String firstCode;

    @Field(type = FieldType.Keyword)
    private String secordCode;

    //@Field(type = FieldType.Text, analyzer = "ik_max_word")
    @Field(type = FieldType.Text)
    private String content;

    //@Field(type = FieldType.Text, analyzer = "ik_smart")
    @Field(type = FieldType.Text)
    private String message;

    @Field(type = FieldType.Integer)
    private Integer type;

    public DocBean(Long id, String firstCode, String secordCode, String content,String message, Integer type) {
        this.id = id;
        this.firstCode = firstCode;
        this.secordCode = secordCode;
        this.content = content;
        this.message = message;
        this.type = type;
    }
}
