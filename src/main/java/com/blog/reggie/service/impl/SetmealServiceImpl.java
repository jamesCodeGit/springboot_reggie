package com.blog.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.reggie.dto.SetmealDto;
import com.blog.reggie.entity.Setmeal;
import com.blog.reggie.entity.SetmealDish;
import com.blog.reggie.mapper.SetmealMapper;
import com.blog.reggie.service.SetmealDishService;
import com.blog.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public SetmealDto getDataById(Long id) {
        Setmeal setmeal = this.getById(id);
        if (setmeal != null){
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, id);
            List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
            setmealDto.setSetmealDishes(setmealDishList);
            return setmealDto;
        }
        return null;

    }

    @Transactional
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
            queryWrapper.eq(SetmealDish::getName, setmealDish.getName());
            int count = setmealDishService.count(queryWrapper);
            if (count == 0){
                setmealDish.setSetmealId(setmealId);
                setmealDishService.save(setmealDish);
            }
        }
    }
}
