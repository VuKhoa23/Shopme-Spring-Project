package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends CrudRepository<Brand, Integer>, PagingAndSortingRepository<Brand, Integer> {
    public Brand findByName(@Param("name") String name);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %:keyWord%")
    public Page<Brand> findByKeyWord(@Param("keyWord") String keyWord, Pageable pageable);

    // use projection to retrieve 2 columns, use Brand(Integer id, String name) construction
    @Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
    public List<Brand> findAllProjection();

}
