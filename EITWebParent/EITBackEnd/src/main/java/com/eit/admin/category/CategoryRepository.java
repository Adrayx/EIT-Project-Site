package com.eit.admin.category;

import com.eit.common.entity.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    @Query("SELECT c from Category c WHERE c.parent.id is NULL")
    public List<Category> findRootCategories();

    public Long countById(Integer id);

    @Query("SELECT c FROM Category c WHERE CONCAT(c.id, ' ', c.name, ' ', c.alias) LIKE %?1%")
    public List<Category> findAll(String keyword);

    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

}
