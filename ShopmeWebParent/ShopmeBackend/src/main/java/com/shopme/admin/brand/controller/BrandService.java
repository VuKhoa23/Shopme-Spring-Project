package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    List<Brand> listAll(){
        return (List<Brand>) brandRepository.findAll();
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }
}
