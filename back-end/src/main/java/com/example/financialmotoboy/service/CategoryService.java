package com.example.financialmotoboy.service;

import com.example.financialmotoboy.dto.CategoryRequestDto;
import com.example.financialmotoboy.dto.CategoryResponseDto;
import com.example.financialmotoboy.entity.Category;
import com.example.financialmotoboy.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto findById(Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    public CategoryResponseDto create(CategoryRequestDto request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setIcon(request.getIcon());
        category.setColor(request.getColor());
        category.setType(request.getType());

        Category saved = categoryRepository.save(category);
        return mapToDto(saved);
    }

    private CategoryResponseDto mapToDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setIcon(category.getIcon());
        dto.setColor(category.getColor());
        dto.setType(category.getType());
        return dto;
    }
}
