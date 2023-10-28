package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface BrandRepository extends CrudRepository<Brand, Integer>, PagingAndSortingRepository<Brand, Integer> {
    public Brand findByName(@Param("name") String name);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %:keyWord%")
    public Page<Brand> findByKeyWord(@Param("keyWord") String keyWord, Pageable pageable);

}
