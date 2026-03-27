package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.CategoryDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.repository.CategoryRepository;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
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
     * @return super.success(responses)
     * @throws AppException(ResponseCode.NO_CONTENT) if list of categories is empty
     * @author HoangVu
     * @since 1.0
     */
    @GetMapping("")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = this.categoryService.getAllCategory();
        if (ObjectUtils.isEmpty(categories)) {
            throw new AppException(ResponseCode.NO_CONTENT);
        }
        List<CategoryDTOResponse> responses = categories.stream()
                .map(category -> new CategoryDTOResponse(category))
                .collect(Collectors.toList());
        return super.success(responses);
    }

    /**
     * Return category by id.
     * <p>
     * This function returns category by id, with the id as the input parameter.
     * @param id
     * @return category by id
     * @throws AppException(ResponseCode.NO_PARAM) if id is null
     * @throws AppException(ResponseCode.NOT_FOUND) if foundCategory is null
     * @author HoangVu
     * @since 1.1
     */
    @GetMapping("/get-category-by-id")
    public ResponseEntity<?> getCategoryById(@RequestParam(name = "id", required = false,
            defaultValue = "1") Long id) {
        if (id == null) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        Category foundCategory = this.categoryService.findCategoryById(id);
        if (ObjectUtils.isEmpty(foundCategory)) {
            throw new AppException(ResponseCode.NOT_FOUND);
        }
        return super.success(new CategoryDTOResponse(foundCategory));
    }
}
