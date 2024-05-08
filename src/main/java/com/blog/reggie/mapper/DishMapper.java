package com.blog.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}