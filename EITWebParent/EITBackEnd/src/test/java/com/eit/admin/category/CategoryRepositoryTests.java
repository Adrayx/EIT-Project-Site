package com.eit.admin.category;

import com.eit.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository repo;

    @Test
    public void testCreateRootCategory() {
        Category category = new Category("Electronics");
        Category saved = repo.save(category);
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory() {
        Category parent = new Category(6);
        Category subCategory = new Category("Gaming Laptops", parent);

        Category saved = repo.save(subCategory);
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategory(){
        Category category = repo.findById(1).get();

        System.out.println(category.getName());

        Set<Category> children = category.getChildren();

        for(Category child: children)
            System.out.println(child.getName());

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories(){
        Iterable<Category> categories = repo.findAll();

        for(Category sub : categories){
            if(sub.getParent() == null){
                System.out.println(sub.getName());

                Set<Category> children = sub.getChildren();

                for(Category subCategory : children){
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }

    private void printChildren(Category parent, int subLevel){
        int newSubLevel = subLevel + 1;

        for (Category subCategory : parent.getChildren()){
            for(int i = 0; i < newSubLevel ; i++){
                System.out.print("--");
            }
            System.out.println(subCategory.getName());

            printChildren(subCategory, newSubLevel);
        }
    }

    @Test
    public void testListCategories(){
        List<Category> rootCategories = repo.findRootCategories();
        rootCategories.forEach(cat -> System.out.println(cat.getName()));
    }

    @Test
    public void testEnableCategory(){
        Integer id = 1;
        repo.updateEnabledStatus(id, true);
    }

}
