package com.store.backend.DBConnections.ItemCategories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ItemCategories")
public class ItemCat {
    
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Integer category_id;
    private String categoryName;

    public ItemCat(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryId() {
        return this.category_id;
    }
    
    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}