package com.shop.project.service;import com.shop.project.exceptions.APIException;import com.shop.project.exceptions.ResourceNotFoundException;import com.shop.project.model.Category;import com.shop.project.pyaload.CategoryDTO;import com.shop.project.pyaload.CategoryResponse;import com.shop.project.repository.CategoryRepository;import org.modelmapper.ModelMapper;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import java.util.List;import java.util.stream.Collectors;//tell spring to manage it as a bean@Servicepublic class CategoryServiceImpl implements CategoryService {//    private List<Category> categories= new ArrayList<>();    //manageId    @Autowired    private CategoryRepository categoryRepository;    @Autowired    private ModelMapper modelMapper;    @Override    public CategoryResponse getAllCategories() {        //fetch the categories        List<Category> categories = categoryRepository.findAll();        //validation        if (categories.isEmpty())            throw new APIException("No category created till now.");        List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();        CategoryResponse categoryResponse = new CategoryResponse();        categoryResponse.setContent(categoryDTOS);        return categoryResponse;    }    @Override    public CategoryDTO createCategory(CategoryDTO categoryDTO) {        //傳入dto 轉成一般型態 等到處理完了再改回dto傳出        Category category = modelMapper.map(categoryDTO, Category.class);        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());        if (categoryFromDB != null)            throw new APIException("Category with the name " + category.getCategoryName() + " already exists !!!");        Category savedCategory= categoryRepository.save(category);        return modelMapper.map(savedCategory, CategoryDTO.class);    }    @Override    public String deleteCategory(Long categoryId) {        Category category = categoryRepository.findById(categoryId)                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));        categoryRepository.delete(category);        return "Category with categoryId: " + categoryId + " deleted successfully !!";    }    @Override    public Category updateCategory(Category category, Long categoryId) {        Category savedCategory = categoryRepository.findById(categoryId)                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));        category.setCategoryId(categoryId);        savedCategory = categoryRepository.save(category);        return savedCategory;    }}