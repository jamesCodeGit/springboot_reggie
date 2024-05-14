package com.blog.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.reggie.dto.SetmealDto;
import com.blog.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    SetmealDto getDataById(Long id);

    void updateWithDish(SetmealDto setmealDto);
}
