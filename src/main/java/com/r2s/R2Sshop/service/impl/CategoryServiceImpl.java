package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.CategoryDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.repository.CategoryRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Return category list by deleted.
     * <p>
     * This function returns all category by deleted(
     * With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status as the input parameter.
     * @param status
     * @return Category list by deleted
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public List<Category> getAllByDeleted(Integer status) {
        if (status == -1) {
            return categoryRepository.findAllByDeleted(null);
        } else if (status == 0){
            return categoryRepository.findAllByDeleted(false);
        } else {
            return categoryRepository.findAllByDeleted(true);
        }
    }

    /**
     * Return category by id.
     * <p>
     * This function returns category by id, with the id as the input parameter.
     * @param id
     * @return Category by id
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if the Category cannot be found by categoriesId
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public Category findById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.CATEGORY_NOT_FOUND));
    }

    /**
     * Add new category.
     * <p>
     * This function is used to add a new category.
     * @param dtoRequest
     * @return information of category if the add process is successful
     * @throws AppException(ResponseCode.DATA_ALREADY_EXISTS) if category be found by categoryName
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Category add(CategoryDTORequest dtoRequest) {
        if (categoryRepository.existsByName(dtoRequest.getName())) {
            throw new AppException(ResponseCode.DATA_ALREADY_EXISTS);
        }
        Category category = new Category();
        category.setName(dtoRequest.getName());
        category.setDeleted(false);
        return categoryRepository.save(category);
    }

    /**
     * Update category by id.
     * <p>
     * This function updates category by id, with the id as the input parameter.
     * @param id
     * @param dtoRequest
     * @return Category by id if the update process is successful
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if category does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED) if category already been deleted in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_EXISTS) if category be found by categoryName
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public Category updateById(Long id, CategoryDTORequest dtoRequest) {
        Category foundCategory = findById(id);
        if (Boolean.TRUE.equals(foundCategory.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        if (Objects.equals(foundCategory.getName(), dtoRequest.getName())) {
            throw new AppException(ResponseCode.IMMUTABLE);
        }
        if (categoryRepository.existsByName(dtoRequest.getName())) {
            throw new AppException(ResponseCode.DATA_ALREADY_EXISTS);
        }
        foundCategory.setName(dtoRequest.getName());
        return this.categoryRepository.save(foundCategory);
    }

    /**
     * Delete category by id and.
     * <p>
     * This function delete category by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if category does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED) if category already been deleted in the database
     * @author HoangVu
     * @since 1.3
     */
    @Override
    public void deleteById(Long id) {
        Category foundCategory = findById(id);
        if (Boolean.TRUE.equals(foundCategory.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundCategory.setDeleted(true);
        categoryRepository.save(foundCategory);
    }
    /**
     * Reactivate category by id.
     * <p>
     * This function reactive category by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if category does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED) if category already been deleted in the database
     * @author HoangVu
     * @since 1.3
     */
    @Override
    public void reactivateById(Long id) {
        Category foundCategory = findById(id);
        if (Boolean.FALSE.equals(foundCategory.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundCategory.setDeleted(false);
        categoryRepository.save(foundCategory);
    }
}
