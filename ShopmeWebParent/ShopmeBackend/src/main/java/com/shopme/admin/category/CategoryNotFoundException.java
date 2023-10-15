package com.shopme.admin.category;

import com.shopme.common.entity.Category;

public class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String message){
        super(message);
    }
}
