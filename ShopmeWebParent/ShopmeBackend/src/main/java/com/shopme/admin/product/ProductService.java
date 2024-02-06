package com.shopme.admin.product;

import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }
        product.setUpdatedTime(new Date());
        return productRepository.save(product);
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);
        Product productByName = productRepository.findByName(name);

        if (isCreatingNew) {
            if (productByName != null) {
                return "DuplicateName";
            } else {
                Product productByAlias = productRepository.findByAlias(alias);
                if (productByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (productByName != null && productByName.getId().intValue() != id) {
                return "DuplicateName";
            }
            if (productRepository.findByAlias(alias) != null && productRepository.findByAlias(alias).getId().intValue() != id) {
                return "DuplicateAlias";
            }
        }

        return "OK";
    }

    public Product findById(Integer id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("No such product");
        }
    }
    public void remove(Product product) {
        productRepository.delete(product);
    }

}
