package com.shopme.admin.user;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void createCategory() {
        Category category = new Category("Computers", "computers", "default.png");
        Category saved = categoryRepository.save(category);
        System.out.println(saved);
    }

    @Test
    public void createAnotherCategory() {
        Category category = new Category("Electronics", "electronics", "default.png");
        Category saved = categoryRepository.save(category);
        System.out.println(saved);
    }

    @Test
    public void createComputersSubCategory() {
        Category computer = categoryRepository.findById(1).get();
        Category desktop = new Category("Desktop", "Desktop", "default.png", computer);

        Category saved = categoryRepository.save(desktop);

        assertThat(saved.getId())
                .isGreaterThan(0);
    }

    @Test
    public void createAnotherComputersSubCategories() {
        Category computer = categoryRepository.findById(1).get();
        Category laptop = new Category("Laptops", "Laptops", "default.png", computer);
        Category component = new Category("Computer components", "Computer components", "default.png", computer);

        categoryRepository.saveAll(List.of(laptop, component));
    }

    @Test
    public void createRamForComputerComponent() {
        Category computerComponent = categoryRepository.findById(5).get();
        Category RAM = new Category("RAM", "RAM", "default.png", computerComponent);

        categoryRepository.save(RAM);
    }

    @Test
    public void getSubCategories() {
        Category computer = categoryRepository.findById(1).get();
        System.out.println(computer.getChildren());
    }

    @Test
    public void printCategoryTree() {
        List<Category> categoryList = (List<Category>) categoryRepository.findAll();
        int level = 0;
        categoryList.forEach(cate -> {
            if (cate.getParent() == null) {
                System.out.println(cate.getName());
                    printChildren(cate, level);
            }
        });
    }

    private void printChildren(Category parent, int level) {
        int newLevel = level + 1;
        Set<Category> children = parent.getChildren();

        for (Category child : children) {
            for (int i = 0; i < newLevel; i++) {
                System.out.print("--");
            }
            System.out.println(child.getName());
            printChildren(child, newLevel);
        }
    }

    @Test
    public void getRootCategories(){
        for (Category category : categoryRepository.listRootCategoriesInForm(Sort.by("name").ascending())) {
            System.out.println(category.getName());
        }
    }

    @Test
    public void findByName(){
        Category category = categoryRepository.findByName("Computers");
        System.out.println(category.getId());
        assertThat(category).isNotNull();
    }

    @Test
    public void findByAlias(){
        Category category = categoryRepository.findByAlias("computers");
        System.out.println(category.getId());
        assertThat(category).isNotNull();
    }

    @Test
    public void testPagingWithKeyWord(){
        Sort sort = Sort.by("name");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(0, 4, sort);

        Page<Category> page = categoryRepository.search("Com", pageable);
        List<Category> list = page.getContent();
        for (Category category : list) {
            System.out.println(category.getName());
        }
    }
}
