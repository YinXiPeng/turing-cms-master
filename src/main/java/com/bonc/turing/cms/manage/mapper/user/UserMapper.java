package com.bonc.turing.cms.manage.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    List<Map> getUserListOfB(@Param("keyWord") String keyWord);

    void insertPermission(@Param("guid") String guid,@Param("permission") int i);
}
