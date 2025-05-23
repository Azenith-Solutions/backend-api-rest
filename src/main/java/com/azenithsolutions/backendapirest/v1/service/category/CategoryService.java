package com.azenithsolutions.backendapirest.v1.service.category;

import com.azenithsolutions.backendapirest.v1.model.Category;
import com.azenithsolutions.backendapirest.v1.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAllCategorys() {return categoryRepository.findAll();}
}
