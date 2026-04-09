package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.CategoryDTORequest;
import com.r2s.R2Sshop.DTO.CategoryDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.repository.CategoryRepository;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController extends BaseRestController{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;

    /**
     * Return category list.
     * <p>
     * This function returns category list by status.
     * @param status
     * @return the category list entity if the data is retrieved successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.4
     */
    @GetMapping
    public ResponseEntity<?> getAllByDeleted(@RequestParam(defaultValue = "-1") Integer status) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        List<Category> categoryList;
        if (status == -1) {
            categoryList = categoryService.getAllByDeleted(null);
        } else if (status == 0){
            categoryList = categoryService.getAllByDeleted(false);
        } else {
            categoryList = categoryService.getAllByDeleted(true);
        }
        List<CategoryDTOResponse> responses = categoryList.stream()
                .map(CategoryDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }

    /**
     * Return category by id.
     * <p>
     * This function returns category by id, with the id as the input parameter.
     * @param id
     * @return category by id
     * @throws AppException(ResponseCode.NOT_FOUND) if the Category cannot be found by categoriesId
     * @author HoangVu
     * @since 1.3
     */
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        Category foundCategory = categoryService.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.NOT_FOUND));
        return super.success(new CategoryDTOResponse(foundCategory));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> add(@RequestBody CategoryDTORequest dtoRequest) {
        if  (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        if (ObjectUtils.isEmpty(dtoRequest.getName())) {
            throw new AppException(ResponseCode.MISSING_PARAM);
        }
        Category insertedCategory = categoryService.add(dtoRequest);
        if (ObjectUtils.isEmpty(insertedCategory)) {
            throw new AppException(ResponseCode.INSERT_FAILURE);
        }
        return super.success(new CategoryDTOResponse(insertedCategory));
    }

    /**
     * Update category.
     * <p>
     * This function is used to update category by id.
     * @param id
     * @param dtoRequest
     * @return category information by id if it is updated successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if dtoRequest or id is empty
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * name are missing
     * @throws AppException(ResponseCode.FAILURE_CATEGORY_UPDATE) if it is updated fails
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateById(@RequestParam Long id,
                                        @RequestBody CategoryDTORequest dtoRequest) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        if (ObjectUtils.isEmpty(dtoRequest.getName())) {
            throw new AppException(ResponseCode.MISSING_PARAM);
        }
        Category updatedCategory = categoryService.updateById(id, dtoRequest);
        if (ObjectUtils.isEmpty(updatedCategory)) {
            throw new AppException(ResponseCode.FAILURE_CATEGORY_UPDATE);
        }
        return super.success(new CategoryDTOResponse(updatedCategory));
    }
    /**
     * Delete category.
     * <p>
     * This function is used to delete category by id.
     * @param id
     * @return category information by id if it is deleted successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if id is empty
     * @throws AppException(ResponseCode.FAILURE_CATEGORY_DELETE) if it is deleted fails
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/delete-by-id")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        Category deletedCategory = categoryService.deleteById(id);
        if (ObjectUtils.isEmpty(deletedCategory)) {
            throw new AppException(ResponseCode.FAILURE_CATEGORY_DELETE);
        }
        return super.success("Deleted successfully");
    }

    /**
     * Reactivated address.
     * <p>
     * This function is used to reactivated address by id.
     * @param id
     * @return category information by id if it is reactivated successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if id is empty
     * @throws AppException(ResponseCode.FAILURE_CATEGORY_REACTIVATE) if it is deleted fails
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reactivate-by-id")
    public ResponseEntity<?> reactivateById(@RequestParam Long id) {
        Category reactivatedCategory = categoryService.reactivateById(id);
        if (ObjectUtils.isEmpty(reactivatedCategory)) {
            throw new AppException(ResponseCode.FAILURE_CATEGORY_REACTIVATE);
        }
        return super.success("Reactivated successfully");
    }
}
