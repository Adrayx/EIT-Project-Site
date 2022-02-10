package com.eit.admin.category;

import com.eit.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    public List<Category> listAll() {
        List<Category> rootCategories = categoryRepo.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories){
        List<Category>  hierarchicalCategories = new ArrayList<>();

        for(Category rootCategory : rootCategories){
            hierarchicalCategories.add(rootCategory);

            Set<Category> children = rootCategory.getChildren();
            for(Category sub : children){
                String name = "--" + sub.getName();
                hierarchicalCategories.add(Category.deepCopy(sub, name));

                listSubHierarchicalCategories(hierarchicalCategories, sub, 1);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category>  hierarchicalCategories, Category category, int level){
        Set<Category> children = category.getChildren();
        int newLevel = level + 1;

        for (Category sub : children){
            String name = "";
            for(int i = 0; i < newLevel ; i++){
                name += "--";
            }
            name += sub.getName();

            hierarchicalCategories.add(Category.deepCopy(sub, name));
            listSubHierarchicalCategories(hierarchicalCategories, sub, newLevel);
        }
    }

    public Category save(Category category){
        return categoryRepo.save(category);
    }

    public List<Category> listCategoriesUsedInForm(){
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDb = categoryRepo.findAll();

        for(Category sub : categoriesInDb){
            if(sub.getParent() == null){
                categoriesUsedInForm.add(Category.copyIdAndName(sub));

                Set<Category> children = sub.getChildren();

                for(Category subCategory : children){
                    String name = "--" + subCategory.getName();
                    Category test = new Category();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
                    listChildren(categoriesUsedInForm, subCategory, 1);
                }
            }
        }

        return categoriesUsedInForm;
    }

    private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel){
        int newSubLevel = subLevel + 1;

        for (Category subCategory : parent.getChildren()){
            String name = "";
            for(int i = 0; i < newSubLevel ; i++){
                name += "--";
            }

            name += subCategory.getName();
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
            listChildren(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }

    public Category get(Integer id) throws CategoryNotFoundException{
        try{
            return categoryRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find anu category with ID " + id);
        }
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepo.countById(id);
        if(countById == null || countById == 0){
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }

        categoryRepo.deleteById(id);
    }

    public void updateCategoryEnabled(Integer id, boolean enabled){
        categoryRepo.updateEnabledStatus(id, enabled);
    }
}
