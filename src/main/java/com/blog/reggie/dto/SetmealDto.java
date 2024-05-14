package com.blog.reggie.dto;

import com.blog.reggie.entity.Setmeal;
import com.blog.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
