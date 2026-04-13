package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.VariantProductDTOResponse;
import com.r2s.R2Sshop.DTO.VariantServiceDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.VariantProduct;
import com.r2s.R2Sshop.service.VariantProductService;
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
@RequestMapping(path = "/variant_products")
public class VariantProductController extends BaseRestController {
    @Autowired
    private VariantProductService variantProductService;

    /**
     * Return variant product list by productId and status.
     * <p>
     * This function returns variant product list by product id and
     * deleted (With the passed-in status -1, return all by productId works;
     * with 0, return all by productId and deleted == false works;
     * and otherwise, it's return all by product id and deleted == true),
     * with the productId, status as the input parameter
     * and pagination is applied.
     * @param productId
     * @param offset
     * @param limit
     * @param status (-1, 0, 1)
     * @return the variant product list by productId and status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.3
     */
    @RequestMapping("/get-all-by-product-id-and-deleted")
    public ResponseEntity<?> getAllByProductIdAndDeleted(@RequestParam(name = "productId",
                                                         required = false, defaultValue = "1") Long productId,
                                                         @RequestParam(defaultValue = "-1") Integer status,
                                                         @RequestParam(defaultValue = "0") Integer offset,
                                                         @RequestParam(defaultValue = "2") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<VariantProduct> variantProductPage = variantProductService.findAllByProductIdAndDeleted(productId,
                status, pageable);
        List<VariantProductDTOResponse> responses = variantProductPage.stream()
                .map(VariantProductDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return product by id.
     * <p>
     * This function returns product by id, with the id as the input parameter.
     * @param id
     * @return product by id
     * @author HoangVu
     * @since 1.0
     */
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@RequestParam(name = "id", required = false
            , defaultValue = "1") long id) {
        VariantProduct foundVariantProduct = variantProductService.findById(id);
        return super.success(new VariantProductDTOResponse(foundVariantProduct));
    }
    /**
     * Add new variant product with category.
     * <p>
     * This function is used to add a new variant product with product.
     * @param productId
     * @param dtoRequest
     * @return information of variant product if the add process is successful
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * productId is missing
     * @throws AppException(ResponseCode.NO_PARAM) if the passed-in parameter values such as
     * dtoRequest is missing
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * name, price, color, modelYear and quantity are missing
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addByProductId(@RequestParam Long productId,
                                            @Valid @RequestBody VariantServiceDTORequest dtoRequest) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        VariantProduct insertedVariantProduct = variantProductService.addByProductId(
                productId, dtoRequest);
        return super.success(new VariantProductDTOResponse(insertedVariantProduct));
    }
    /**
     * Update variant product.
     * <p>
     * This function is used to update variant product by id.
     * @param id
     * @param productId
     * @param dtoRequest
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id and productId are missing
     * @return variant product information by id if it is updated successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if dtoRequestis empty
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateById(@RequestParam Long id,
                                        @RequestParam Long productId,
                                        @Valid @RequestBody VariantServiceDTORequest dtoRequest) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        VariantProduct updatedVariantProduct = variantProductService.updateById(id, productId, dtoRequest);
        return super.success(new VariantProductDTOResponse(updatedVariantProduct));
    }
    /**
     * Delete variant product.
     * <p>
     * This function is used to delete variant product by id.
     * @param id
     * @return "Deleted successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        variantProductService.deleteById(id);
        return super.success("Deleted successfully");
    }
    /**
     * Reactivated variant product.
     * <p>
     * This function is used to reactivated variant product by id.
     * @param id
     * @return "Reactivated successfully" if it is reactivated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reactivate-by-id")
    public ResponseEntity<?> reactivateById(@RequestParam Long id) {
        variantProductService.reactivateById(id);
        return super.success("Reactivated successfully");
    }
}
