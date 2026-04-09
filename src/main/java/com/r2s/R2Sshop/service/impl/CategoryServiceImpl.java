package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.CategoryDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.repository.CategoryRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Return category list by deleted.
     * <p>
     * This function returns all category by deleted, with the deleted as the input parameter.
     * @param deleted
     * @return Category list by deleted
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public List<Category> getAllByDeleted(Boolean deleted) {
        return this.categoryRepository.findAllByDeleted(deleted);
    }

    /**
     * Return category by id.
     * <p>
     * This function returns category by id, with the id as the input parameter.
     * @param id
     * @return Category by id
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Optional<Category> findById(Long id){
        return this.categoryRepository.findById(id);
    }

    /**
     * Check exists category by id.
     * <p>
     * This function check exists category by id, with the id as the input parameter.
     * @param id
     * @return True if it exists and false if it does not.
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    /**
     * Check exists category by name.
     * <p>
     * This function check exists category by name, with the name as the input parameter.
     * @param name
     * @return True if it exists and false if it does not.
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    /**
     * Add new category.
     * <p>
     * This function is category to add a new category.
     * @param newCategory
     * @return information of category if the add process is successful
     * @throws AppException(ResponseCode.DATA_ALREADY_EXISTS) if category be found by categoryName
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Category add(CategoryDTORequest newCategory) {
        if (existsByName(newCategory.getName())) {
            throw new AppException(ResponseCode.DATA_ALREADY_EXISTS);
        }
        Category category = new Category();
        category.setName(newCategory.getName());
        category.setDeleted(false);
        return categoryRepository.save(category);
    }

    /**
     * Update category by id.
     * <p>
     * This function updates category by id, with the id as the input parameter.
     * @param id
     * @param DTORequest
     * @return Category by id if the update process is successful
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if category does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED) if category already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Category updateById(Long id, CategoryDTORequest DTORequest) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.CATEGORY_NOT_FOUND));
        if (Boolean.TRUE.equals(foundCategory.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundCategory.setName(DTORequest.getName());
        return this.categoryRepository.save(foundCategory);
    }

    /**
     * Delete category by id and.
     * <p>
     * This function delete category by id, with the id as the input parameter.
     * @param id
     * @return category by id if the delete process is successful
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if category does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED) if category already been deleted in the database
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Category deleteById(Long id) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.CATEGORY_NOT_FOUND));
        if (Boolean.TRUE.equals(foundCategory.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundCategory.setDeleted(true);
        return this.categoryRepository.save(foundCategory);
    }
    /**
     * Reactivate category by id.
     * <p>
     * This function reactive category by id, with the id as the input parameter.
     * @param id
     * @return category by id if the reactivate process is successful
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if category does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED) if category already been deleted in the database
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Category reactivateById(Long id) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.CATEGORY_NOT_FOUND));
        if (Boolean.FALSE.equals(foundCategory.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundCategory.setDeleted(false);
        return this.categoryRepository.save(foundCategory);
    }
}
