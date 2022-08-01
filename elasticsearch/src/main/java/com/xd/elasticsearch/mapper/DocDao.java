package com.xd.elasticsearch.mapper;

import com.xd.elasticsearch.bean.DocBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DocDao {

    DocBean getById(Long id);
}
