package com.shopme.admin.user;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandServiceTest {
    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void listByPage(){
        Pageable pageable = PageRequest.of(1 - 1, 4);
        List<Brand> brandList = brandRepository.findAll(pageable).getContent();

        System.out.println(brandList);
    }
}
