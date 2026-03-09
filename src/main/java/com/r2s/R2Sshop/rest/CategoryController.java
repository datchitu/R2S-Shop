package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.repository.CategoryRepository;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController extends BaseRestController{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public ResponseEntity<?> getAllCategories() {
//        try {
            List<Category> categories = this.categoryService.getAllCategory();
            return new ResponseEntity<>(categories, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @GetMapping("/getById")
    public ResponseEntity<Category> getById(@RequestParam(name = "id", required = false, defaultValue = "2") long id,
                                            @RequestParam(defaultValue = "0")Integer offset,
                                            @RequestParam(defaultValue = "20")Integer limit) {
        Category foundCategory = this.categoryService.findCategoryById(id);
        if (ObjectUtils.isEmpty(foundCategory)) {
            return new ResponseEntity<>(foundCategory, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundCategory, HttpStatus.OK);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException() {
        return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
}
