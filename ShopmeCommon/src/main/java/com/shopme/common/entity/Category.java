package com.shopme.common.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false, unique = true)
    private String name;
    @Column(length = 64, nullable = false, unique = true)
    private String alias;
    @Column(length = 128, nullable = false)
    private String image;
    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private Set<Category> children = new HashSet<>();

    public static Category copy(Category category){
        Category copy = new Category();
        copy.setId(category.getId());
        copy.setName(category.getName());
        copy.setImage(category.getImage());
        copy.setAlias(category.getAlias());
        copy.setEnabled(category.isEnabled());

        return copy;
    }

    public static Category copy(Category category, String name){
        Category copy = new Category();
        copy.setId(category.getId());
        copy.setName(name);
        copy.setImage(category.getImage());
        copy.setAlias(category.getAlias());
        copy.setEnabled(category.isEnabled());

        return copy;
    }

    public Category() {
    }

    public Category(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public Category(String name, String alias, String image, boolean enabled, Category parent, Set<Category> children) {
        this.name = name;
        this.alias = alias;
        this.image = image;
        this.enabled = enabled;
        this.parent = parent;
        this.children = children;
    }

    public Category(Integer id,String name) {
        this.id = id;
        this.name = name;
    }


    public Category(String name, String alias, String image) {
        this.name = name;
        this.alias = alias;
        this.image = image;
    }

    public Category(String name, String alias, String image, Category parent) {
        this.name = name;
        this.alias = alias;
        this.image = image;
        this.parent = parent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }


    @Transient
    public String getImagePath(){
        if(image == null || image.isEmpty()){
            return "/images/empty.jpg";
        }
        return "/categories-images/" + this.id + "/" + this.image;
    }
}
