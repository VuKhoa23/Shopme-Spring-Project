package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> listAll() {
        List<Category> roots = categoryRepository.listRootCategories();
        return listTree(roots);
    }

    private List<Category> listTree(List<Category> roots){
        List<Category> tree = new ArrayList<>();

        roots.forEach(root->{
            tree.add(Category.copy(root));
            Set<Category> children = root.getChildren();

            children.forEach(child->{
                String name = "--" + child.getName();
                tree.add(Category.copy(child, name));
                getChildrenForListing(tree, child, 1);
            });
        });
        return tree;
    }

    private void getChildrenForListing(List<Category> tree, Category parent, int level){
        Set<Category> children = parent.getChildren();
        int newLevel = level + 1;
        children.forEach(child->{
            String prefix = "";
            for (int i = 0; i < newLevel; i++) {
                prefix += "--";
            }
            tree.add(Category.copy(child, prefix + child.getName()));

            getChildrenForListing(tree, child, newLevel);
        });
    }


    public List<Category> listCategoryByTreeInForm() {
        List<Category> tree = new ArrayList<>();

        List<Category> categoryList = (List<Category>) categoryRepository.findAll();
        int level = 0;
        categoryList.forEach(cate -> {
            // Get child of top level categories
            if (cate.getParent() == null) {
                tree.add(new Category(cate.getId(), cate.getName()));
                getChildren(tree, cate, level);
            }
        });

        return tree;
    }

    private void getChildren(List<Category>  tree, Category parent, int level) {
        int newLevel = level + 1;
        Set<Category> children = parent.getChildren();

        for (Category child : children) {
            String prefix = "";
            for (int i = 0; i < newLevel; i++) {
                prefix += "--";
            }
            tree.add(new Category(child.getId(), prefix + child.getName()));
            getChildren(tree, child, newLevel);
        }
    }

    public Category save(Category category){
        return categoryRepository.save(category);
    }
}
