package com.shopme.admin.user;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
    // create a fake category repo
    @MockBean
    private CategoryRepository categoryRepository;

    // inject that fake repo into the service
    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void checkUniqueName() {
        Integer id = null;
        String name = "Computers";
        String alias = "abc";

        Category category = new Category(id, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(name)).thenReturn(null);


        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void checkUniqueAlias(){
        Integer id = null;
        String name = "random";
        String alias = "computers";

        Category category = new Category(id, name, alias);

        // we assume that the repo code will return this category when findByAlias is called.
        // but actually no repo code was run. We just return explicitly the result
        // so when findByAlias is called. It will return the predefined category
        // findByAlias and findByName is in the checkUnique method
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void checkUniqueNameExistingId(){
        Integer id = 2;
        String name = "Computers";
        String alias = "computers";

        Category category = new Category(id, name, alias);

        // we assume that the repo code will return this category when findByAlias is called.
        // but actually no repo code was run. We just return explicitly the result
        // so when findByAlias is called. It will return the predefined category
        // findByAlias and findByName is in the checkUnique method
        Mockito.when(categoryRepository.findByName(name)).thenReturn(new Category(1, name, alias));
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(new Category(1, name, alias));

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void checkUniqueAliasExistingId(){
        Integer id = 2;
        String name = "abc";
        String alias = "computers";

        Category category = new Category(id, name, alias);

        // we assume that the repo code will return this category when findByAlias is called.
        // but actually no repo code was run. We just return explicitly the result
        // so when findByAlias is called. It will return the predefined category
        // findByAlias and findByName is in the checkUnique method
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(new Category(1, name, alias));

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void checkUniquePassed(){
        Integer id = null;
        String name = "random";
        String alias = "random";

        Category category = new Category(id, name, alias);

        // we assume that the repo code will return this category when findByAlias is called.
        // but actually no repo code was run. We just return explicitly the result
        // so when findByAlias is called. It will return the predefined category
        // findByAlias and findByName is in the checkUnique method
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void checkKeepNameAndAlias(){
        Integer id = 1;
        String name = "Computers";
        String alias = "computers";

        Category category = new Category(id, name, alias);

        // we assume that the repo code will return this category when findByAlias is called.
        // but actually no repo code was run. We just return explicitly the result
        // so when findByAlias is called. It will return the predefined category
        // findByAlias and findByName is in the checkUnique method
        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");
    }

}
