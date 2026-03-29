package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.CategoryDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.repository.CategoryRepository;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
     * @return super.success(responses)
     * @throws AppException(ResponseCode.NO_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.2
     */
    @GetMapping("")
    public ResponseEntity<?> getAllyByStatus(@RequestParam(defaultValue = "-1") Integer status) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        List<Category> categories;
        if (status == -1) {
            categories = this.categoryService.getAll();
        } else if (status == 0){
            categories = this.categoryService.getAllByDeleted(false);
        } else {
            categories = this.categoryService.getAllByDeleted(true);
        }
        List<CategoryDTOResponse> responses = categories.stream()
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
     * @since 1.2
     */
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        Category foundCategory = this.categoryService.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.NOT_FOUND));
        return super.success(new CategoryDTOResponse(foundCategory));
    }
}
