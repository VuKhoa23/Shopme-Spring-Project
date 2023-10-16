package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>, CrudRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public Page<Category> listRootCategories(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public List<Category> listRootCategoriesInForm(Sort sort);

    public Category findByName(String name);

    public Category findByAlias(String alias);

}
