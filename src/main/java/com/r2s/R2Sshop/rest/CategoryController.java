package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.CategoryDTORequest;
import com.r2s.R2Sshop.DTO.CategoryDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.service.CategoryService;
import jakarta.validation.Valid;
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
    private CategoryService categoryService;

    /**
     * Return category list.
     * <p>
     * This method returns category list by status.
     * @param status
     * @return the category list entity if the data is retrieved successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.5
     */
    @GetMapping
    public ResponseEntity<?> getAllByDeleted(@RequestParam(defaultValue = "-1") Integer status) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        List<Category> categoryList = categoryService.getAllByDeleted(status);
        List<CategoryDTOResponse> responses = categoryList.stream()
                .map(CategoryDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }

    /**
     * Return category by id.
     * <p>
     * This method returns category by id, with the id as the input parameter.
     * @param id
     * @return category by id
     * @author HoangVu
     * @since 1.4
     */
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        return super.success(new CategoryDTOResponse(categoryService.findById(id)));
    }
    /**
     * Add new category.
     * <p>
     * This method is used to add a new category.
     * @param dtoRequest
     * @return category information if it is added successfully.
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * name are missing
     * @throws AppException(ResponseCode.INSERT_FAILURE) if it is added fails
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<?> add(@Valid @RequestBody CategoryDTORequest dtoRequest) {
        Category insertedCategory = categoryService.add(dtoRequest);
        if (ObjectUtils.isEmpty(insertedCategory)) {
            throw new AppException(ResponseCode.INSERT_FAILURE);
        }
        return super.success(new CategoryDTOResponse(insertedCategory));
    }

    /**
     * Update category.
     * <p>
     * This method is used to update category by id.
     * @param id
     * @param dtoRequest
     * @return category information by id if it is updated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @throws AppException(ResponseCode.INVALID_VALUE) if the passed-in parameter values such as
     * name are missing
     * @author HoangVu
     * @since 1.4
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin")
    public ResponseEntity<?> updateById(@RequestParam Long id,
                                        @Valid @RequestBody CategoryDTORequest dtoRequest) {
        Category updatedCategory = categoryService.updateById(id, dtoRequest);
        return super.success(new CategoryDTOResponse(updatedCategory));
    }
    /**
     * Delete category.
     * <p>
     * This method is used to delete category by id.
     * @param id
     * @return "Deleted successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.3
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete-by-id")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        categoryService.deleteById(id);
        return super.success("Deleted successfully");
    }

    /**
     * Restore address.
     * <p>
     * This method is used to restore address by id.
     * @param id
     * @return "Restored successfully" if it is restored successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/restore-by-id")
    public ResponseEntity<?> restoreById(@RequestParam Long id) {
        categoryService.restoreById(id);
        return super.success("Restored successfully");
    }
}
