package com.eit.common.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length=128, nullable=false, unique=true)
    private String name;

    @Column(length=64, nullable=false, unique=true)
    private String alias;

    @Column(length=128, nullable=false)
    private String image;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy="parent")
    private Set<Category> children = new HashSet<>();

    public Category(Integer id){
        this.id = id;
    }
    public Category(String name){
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    public static Category copyIdAndName(Category category){
        Category category1 = new Category();
        category1.setId(category.getId());
        category1.setName(category.getName());

        return category1;
    }

    public static Category copyIdAndName(Integer id, String name){
        Category category = new Category();
        category.setId(id);
        category.setName(name);

        return category;
    }

    public static Category deepCopy(Category category){
        Category newCategory = new Category();
        newCategory.setId(category.getId());
        newCategory.setName(category.getName());
        newCategory.setAlias(category.getAlias());
        newCategory.setImage(category.getImage());
        newCategory.setEnabled(category.isEnabled());
        newCategory.setChildren(category.getChildren());
        newCategory.setParent(category.getParent());

        return newCategory;
    }

    public static Category deepCopy(Category category, String name){
        Category newCategory = Category.deepCopy(category);
        newCategory.setName(name);

        return newCategory;
    }

    public Category(String name, Category parent){
        this(name);
        this.parent = parent;
    }

    public Category() {
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

    @Override
    public String toString() {
        return this.name;
    }

    @Transient
    public String getImagePath(){
        if(this.id == null) return "/images/image-thumbnail.png";
        return "/category-images/" + this.id + "/" + this.image;
    }
}
