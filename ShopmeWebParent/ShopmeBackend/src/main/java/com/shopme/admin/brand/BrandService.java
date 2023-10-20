package com.shopme.admin.brand;

import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.brand.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listAll(){
        return (List<Brand>) brandRepository.findAll();
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand findById(Integer id) throws BrandNotFoundException {
        try{
            return brandRepository.findById(id).get();
        }catch(NoSuchElementException e){
            throw new BrandNotFoundException("This brand doesn't exists");
        }
    }

    public void remove(Brand brand) {
        brandRepository.delete(brand);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNewBrand = (id == null);
        if(isCreatingNewBrand){
            if(brandRepository.findByName(name) != null){
                return "Duplicate Name";
            }
        }
        return "OK";
    }
}
