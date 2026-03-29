package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.VariantProductDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.VariantProduct;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.repository.VariantProductRepository;
import com.r2s.R2Sshop.service.ProductService;
import com.r2s.R2Sshop.service.VariantProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/variant_products")
public class VariantProductController extends BaseRestController {
    @Autowired
    private VariantProductRepository variantProductRepository;
    @Autowired
    private VariantProductService variantProductService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    /**
     * Return all variant product by productId and deleted(status).
     * <p>
     * This function returns all variant product by product id and
     * deleted(With the passed-in status -1, return all by productId works;
     * with 0, return all by productId and deleted == false works;
     * and otherwise, it's return all by product id and deleted == false.),
     * with the productId, status as the input parameter
     * and pagination is applied.
     * @param productId
     * @param offset
     * @param limit
     * @param status (-1, 0, 1)
     * @return products by productId
     * @throws AppException(ResponseCode.NO_PARAM) if status is outside the value (-1, 0, 1)
     * @throws AppException(ResponseCode.NOT_FOUND) if the Product cannot be found by productId
     * based on the passed-in ID parameter
     * @author HoangVu
     * @since 1.0
     */
    @RequestMapping("/get-all-by-product-id-and-deleted")
    public ResponseEntity<?> getAllByProductIdAndDeleted(@RequestParam(name = "productId",
                                                         required = false, defaultValue = "1") Long productId,
                                                         @RequestParam(defaultValue = "0") Integer offset,
                                                         @RequestParam(defaultValue = "2") Integer limit,
                                                         @RequestParam(defaultValue = "-1") Integer status) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        if (!productService.existsById(productId)) {
            throw new AppException(ResponseCode.NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").descending());
        Page<VariantProduct> variantProducts;
        if (status == -1) {
            variantProducts = this.variantProductService.findAllByProductId(productId, pageable);
        } else if (status == 0) {
            variantProducts = this.variantProductService.findAllByProductIdAndDeleted(productId, false, pageable);
        } else {
            variantProducts = this.variantProductService.findAllByProductIdAndDeleted(productId, true, pageable);
        }
        List<VariantProductDTOResponse> responses = variantProducts.stream()
                .map(VariantProductDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
}
