package com.blog.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.reggie.common.R;
import com.blog.reggie.dto.SetmealDto;
import com.blog.reggie.entity.Category;
import com.blog.reggie.entity.Setmeal;
import com.blog.reggie.service.CategoryService;
import com.blog.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> list(int page, int pageSize, String name){
        Page<Setmeal> setMealPage = new Page(page, pageSize);
        Page<SetmealDto> pagePage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        setmealService.page(setMealPage, queryWrapper);

        BeanUtils.copyProperties(setMealPage, pagePage, "records");

        List<Setmeal> records = setMealPage.getRecords();
        List<SetmealDto> list = records.stream().map((item) ->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        pagePage.setRecords(list);

        return R.success(pagePage);
    }

    @GetMapping("{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getDataById(id);
        if (setmealDto != null){
            return R.success(setmealDto);
        }
        return R.error("未找到相关信息");
    }

    @PutMapping
    public R<String> updateWithDish(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("修改套餐信息成功");
    }
}
