package com.xd.elasticsearch.controller;

import com.xd.elasticsearch.bean.DocBean;
import com.xd.elasticsearch.service.IElasticService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/elastic")
public class ElasticController {

    @Autowired
    private IElasticService elasticService;

    @GetMapping("/init")
    public void init() {
        elasticService.createIndex();
        long sum = 200000;
        int loop = 1000;

        List<DocBean> list = new ArrayList<>();
        for (int i = 0; i < sum; i++) {
            list.clear();
            for (int j = 0; j < loop; j++) {
                list.add(new DocBean(1L + j + loop * i,
                        "园园园园园园园人寿人寿人寿人寿人寿人寿人寿人寿人寿人寿人寿人寿人寿人寿",
                        "XX8068黄芪属约有2000种，除大洋洲外，全世界亚热带和温带地区均产",
                        "一般的业务数时间分索空间。这种“热暖”区的架构是完全可以存储到pb级别的一般的业务务数据都可以按照时间分索引，我们在操作数据的时候往往是操作最近的热数据索",
                        "一般的业务数时间分索空间。这种“热暖”区的架构是完全可以存储到pb级别的一般的业务务数据都可以按照时间分索引，我们在操作数据的时候往往是操作最近的热数据索",
                        (int)(j%5)));
            }
            elasticService.saveAll(list);
        }
    }

    @GetMapping("/all")
    public Iterator<DocBean> all() {
        return elasticService.findAll();
    }

    @GetMapping("/get/{id}")
    public DocBean get(@PathVariable Long id){
        return elasticService.getById(id);
    }

}
