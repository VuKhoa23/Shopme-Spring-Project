package com.shopme.admin.brand;

import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.brand.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BrandService {
    public static final int PAGE_SIZE = 4;
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listAll() {
        return (List<Brand>) brandRepository.findAll();
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand findById(Integer id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new BrandNotFoundException("This brand doesn't exists");
        }
    }

    public void remove(Brand brand) {
        brandRepository.delete(brand);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNewBrand = (id == null);
        if (isCreatingNewBrand) {
            if (brandRepository.findByName(name) != null) {
                return "Duplicate Name";
            }
        }
        return "OK";
    }

    public Page<Brand> listByPage(String keyWord, int pageNumber, String sortOrder) {
        Sort sort = Sort.by("name");
        if(sortOrder.equals("asc")){
            sort = sort.ascending();
        }
        else{
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, sort);
        if(keyWord != null || !keyWord.isEmpty()){

            return brandRepository.findByKeyWord(keyWord, pageable);
        }
        return brandRepository.findAll(pageable);
    }

    public List<Brand> listAllProjection(){
        return brandRepository.findAllProjection();
    }

}
