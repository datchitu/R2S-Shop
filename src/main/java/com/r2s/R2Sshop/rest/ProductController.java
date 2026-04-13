package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.ProductDTORequest;
import com.r2s.R2Sshop.DTO.ProductDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/products")
public class ProductController extends BaseRestController{
    @Autowired
    private ProductService productService;

    /**
     * Return all products by category id and status.
     * <p>
     * This function returns all product by categoryId and status,
     * with the categoryId, status as the input parameter
     * and pagination is applied.
     * @param categoryId
     * @param status
     * @param offset
     * @param limit
     * @return products by categoryId
     * @throws AppException(ResponseCode.NOT_FOUND) if the Category cannot be found by categoryId
     * based on the passed-in ID parameter
     * @author HoangVu
     * @since 1.3
     */
    @RequestMapping("/get-by-category-id")
    public ResponseEntity<?> getAllByCategoryIdAndDeleted(@RequestParam(name = "categoriesId", required = false,
                                                                  defaultValue = "1") Long categoryId,
                                              @RequestParam(defaultValue = "-1") Integer status,
                                              @RequestParam(defaultValue = "0") Integer offset,
                                              @RequestParam(defaultValue = "2") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<Product> productPage = productService.findAllByCategoryIdAndDeleted(categoryId, status, pageable);
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
     * @author HoangVu
     * @since 1.4
     */
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@RequestParam(name = "id", required = false
            , defaultValue = "1") long id) {
        Product foundProduct = productService.findById(id);
        return super.success(new ProductDTOResponse(foundProduct));
    }
    /**
     * Add new product with category.
     * <p>
     * This function is used to add a new product with category.
     * @param categoryId
     * @param dtoRequest
     * @return information of product if the add process is successful
     * @throws AppException(ResponseCode.MISSING_PARAM) if categoryId is empty
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * name is missing
     * @throws AppException(ResponseCode.INSERT_FAILURE) if it is added fails
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addByCategoryId(@RequestParam Long categoryId,
                                             @Valid @RequestBody ProductDTORequest dtoRequest) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        Product insertedProduct = productService.addByCategoryId(categoryId, dtoRequest);
        if (ObjectUtils.isEmpty(insertedProduct)) {
            throw new AppException(ResponseCode.INSERT_FAILURE);
        }
        return super.success(new ProductDTOResponse(insertedProduct));
    }
    /**
     * Update product.
     * <p>
     * This function is used to update product by id.
     * @param id
     * @param categoryId
     * @param dtoRequest
     * @return product information by id if it is updated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if categoryId or id are empty
     * @throws AppException(ResponseCode.NO_PARAM) if dtoRequest is empty
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * name is missing
     * @throws AppException(ResponseCode.FAILURE_PRODUCT_UPDATE) if it is updated fails
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateById(@RequestParam Long id,
                                        @RequestParam Long categoryId,
                                        @Valid @RequestBody ProductDTORequest dtoRequest) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        Product updatedProduct = productService.updateById(id, categoryId, dtoRequest);
        if (ObjectUtils.isEmpty(updatedProduct)) {
            throw new AppException(ResponseCode.FAILURE_PRODUCT_UPDATE);
        }
        return super.success(new ProductDTOResponse(updatedProduct));
    }
    /**
     * Delete product.
     * <p>
     * This function is used to delete product by id.
     * @param id
     * @return "Deleted successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @throws AppException(ResponseCode.FAILURE_PRODUCT_DELETE) if it is deleted fails
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        productService.deleteById(id);
        return super.success("Deleted successfully");
    }
    /**
     * Reactivated product.
     * <p>
     * This function is used to reactivated product by id.
     * @param id
     * @return "Reactivated successfully" if it is reactivated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @throws AppException(ResponseCode.FAILURE_PRODUCT_REACTIVATE) if it is deleted fails
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reactivate-by-id")
    public ResponseEntity<?> reactivateById(@RequestParam Long id) {
        productService.reactivateById(id);
        return super.success("Reactivated successfully");
    }
}
