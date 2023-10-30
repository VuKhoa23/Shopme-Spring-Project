package com.shopme.admin.user;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void addAcer(){
        Brand acer = new Brand();
        acer.setName("Acer");
        acer.setLogo("brand-logo.png");

        Set<Category> categories = new HashSet<>();

        Category laptops = categoryRepository.findByName("Laptops");
        categories.add(laptops);
        acer.setCategories(categories);

        brandRepository.save(acer);

        assertThat(acer.getId()).isGreaterThan(0);
    }

    @Test
    public void addApple(){
        Brand apple = new Brand();
        apple.setName("Apple");
        apple.setLogo("brand-logo.png");

        Set<Category> categories = new HashSet<>();

        Category cellphonesAndAccessories = categoryRepository.findByName("Cell Phones & Accessories");
        categories.add(cellphonesAndAccessories);
        Category Tablets = categoryRepository.findByName("Tablets");
        categories.add(Tablets);
        apple.setCategories(categories);

        brandRepository.save(apple);

        assertThat(apple.getId()).isGreaterThan(0);
    }

    @Test
    public void addSamsung(){
        Brand samsung = new Brand();
        samsung.setName("Samsung");
        samsung.setLogo("brand-logo.png");

        Set<Category> categories = new HashSet<>();

        Category memory = categoryRepository.findByName("Memory");
        categories.add(memory);
        Category internalHardDrives = categoryRepository.findByName("Internal Hard Drives");
        categories.add(internalHardDrives);
        samsung.setCategories(categories);

        brandRepository.save(samsung);

        assertThat(samsung.getId()).isGreaterThan(0);
    }

    @Test
    public void displayAllBrands(){
        List<Brand> brands = (List<Brand>) brandRepository.findAll();

        brands.forEach(brand->{
            System.out.println("Brand name: " + brand.getName());
            System.out.print("Brand categories: ");
            brand.getCategories().forEach(cate->{
                System.out.print(cate.getName() + " | ");
            });
            System.out.println();
        });
    }

    @Test
    public void changeSamsungBrandName(){
        Brand samsung = brandRepository.findById(3).get();
        // samsung is in persistent context so no need to save it. Just set the name
        samsung.setName("Samsung Electronics");
    }

    @Test
    public void findByName(){
        Brand samsung = brandRepository.findByName("Samsung Electronics");
        System.out.println(samsung);
    }

    @Test
    public void findAllWithProjection(){
        List<Brand> brands = brandRepository.findAllProjection();

        System.out.println(brands);
    }
}
