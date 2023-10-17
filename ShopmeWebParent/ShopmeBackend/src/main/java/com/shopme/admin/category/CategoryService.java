package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CategoryService {
    public static final int ROOT_CATEGORIES_PER_PAGE = 4;
    @Autowired
    CategoryRepository categoryRepository;

    public Category findById(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new CategoryNotFoundException("No such category");
        }
    }

    public List<Category> listByPage(CategoryPageInfo info, int pageNum, String sortOrder, String keyWord) {
        Sort sort = Sort.by("name");
        if (sortOrder.equals("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);

        Page<Category> pageOfRoots = null;

        if (keyWord != null && !keyWord.isEmpty()) {
            pageOfRoots = categoryRepository.search(keyWord, pageable);
            System.out.println("Yes");
        } else {
            System.out.println("No");
            pageOfRoots = categoryRepository.listRootCategories(pageable);
        }

        info.setTotalElements(pageOfRoots.getTotalElements());
        info.setTotalPages(pageOfRoots.getTotalPages());

        if (keyWord != null && !keyWord.isEmpty()) {
            List<Category> searchResults = pageOfRoots.getContent();
            searchResults.forEach(System.out::println);
            return searchResults;
        } else {
            List<Category> roots = pageOfRoots.getContent();
            return listTree(roots, sortOrder);
        }
    }

    private List<Category> listTree(List<Category> roots, String sortOrder) {
        List<Category> tree = new ArrayList<>();

        roots.forEach(root -> {
            tree.add(Category.copy(root));
            Set<Category> children = sortedChildren(root.getChildren(), sortOrder);

            children.forEach(child -> {
                String name = "--" + child.getName();
                tree.add(Category.copy(child, name));
                getChildrenForListing(tree, child, 1, sortOrder);
            });
        });
        return tree;
    }

    private void getChildrenForListing(List<Category> tree, Category parent, int level, String sortOrder) {
        Set<Category> children = sortedChildren(parent.getChildren(), sortOrder);
        int newLevel = level + 1;
        children.forEach(child -> {
            String prefix = "";
            for (int i = 0; i < newLevel; i++) {
                prefix += "--";
            }
            tree.add(Category.copy(child, prefix + child.getName()));

            getChildrenForListing(tree, child, newLevel, sortOrder);
        });
    }


    public List<Category> listCategoryByTreeInForm() {
        List<Category> tree = new ArrayList<>();

        List<Category> categoryList = categoryRepository.listRootCategoriesInForm(Sort.by("name").ascending());
        int level = 0;
        categoryList.forEach(cate -> {
            // Get child of top level categories
            if (cate.getParent() == null) {
                tree.add(new Category(cate.getId(), cate.getName()));
                getChildren(tree, cate, level, "asc");
            }
        });

        return tree;
    }

    private void getChildren(List<Category> tree, Category parent, int level, String sortOrder) {
        int newLevel = level + 1;
        Set<Category> children = sortedChildren(parent.getChildren(), sortOrder);

        for (Category child : children) {
            String prefix = "";
            for (int i = 0; i < newLevel; i++) {
                prefix += "--";
            }
            tree.add(new Category(child.getId(), prefix + child.getName()));
            getChildren(tree, child, newLevel, sortOrder);
        }
    }

    private SortedSet<Category> sortedChildren(Set<Category> children, String sortOrder) {
        SortedSet<Category> sorted = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                if (sortOrder.equals("asc")) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o2.getName().compareTo(o1.getName());
                }
            }
        });
        sorted.addAll(children);
        return sorted;
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = categoryRepository.findByName(name);

        if (isCreatingNew) {
            if (categoryByName != null) {
                return "DuplicateName";
            } else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId().intValue() != id) {
                return "DuplicateName";
            }
            if (categoryRepository.findByAlias(alias) != null && categoryRepository.findByAlias(alias).getId().intValue() != id) {
                return "DuplicateAlias";
            }
        }

        return "OK";
    }

    public void remove(Category category) {
        categoryRepository.delete(category);
    }
}
