package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.ProductDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.repository.CategoryRepository;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.service.CategoryService;
import com.r2s.R2Sshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/products")
public class ProductController extends BaseRestController{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    /**
     * Return all products by category id.
     * <p>
     * This function returns all product by categoryId, with the categoryId as the input parameter
     * and pagination is applied.
     * @param categoryId
     * @return products by categoryId
     * @throws AppException(ResponseCode.NOT_FOUND) if the Category cannot be found by categoryId
     * based on the passed-in ID parameter
     * @author HoangVu
     * @since 1.1
     */
    @RequestMapping("/get-products-by-category-id")
    public ResponseEntity<?> getAllProductsByCategory(@RequestParam(name = "categoriesId", required = false,
                                                                  defaultValue = "1") Long categoryId,
                                                      @RequestParam(defaultValue = "0") Integer offset,
                                                      @RequestParam(defaultValue = "2") Integer limit) {
        if (!categoryService.existsById(categoryId)) {
            throw new AppException(ResponseCode.NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").descending());
        Page<Product> productPage = this.productService.findAllProductsByCategoryId(categoryId, pageable);
        List<ProductDTOResponse> responses = productPage.stream()
                .map(ProductDTOResponse :: new)
                .collect(Collectors.toList());
        return success(responses);
    }

    /**
     * Return product by id.
     * <p>
     * This function returns product by id, with the id as the input parameter.
     * @param id
     * @return product by id
     * @throws AppException(ResponseCode.NOT_FOUND) if the Product cannot be found by id
     * @author HoangVu
     * @since 1.1
     */
    @GetMapping("/get-product-by-id")
    public ResponseEntity<?> getProductById(@RequestParam(name = "id", required = false
            , defaultValue = "1") long id) {
        Product foundProduct = this.productService.findProductById(id);
        if (ObjectUtils.isEmpty(foundProduct)){
            throw new AppException(ResponseCode.NOT_FOUND);
        }
        return super.success(new ProductDTOResponse(foundProduct));
    }
}
