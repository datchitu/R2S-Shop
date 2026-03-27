package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.repository.CategoryRepository;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController extends BaseRestController{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

//    public ResponseEntity<?> getAllProductsByCategory(@RequestParam(name = "categoriesId", required = false,
//                                                      defaultValue = "1") Long categoriesId,
//                                                      @RequestParam(defaultValue = "0") Integer offset,
//                                                      @RequestParam(defaultValue = "4")Integer limit) {
//        Pageable pageable = PageRequest.of(offset, limit);
//        List<Sort.Order> orders = new ArrayList<>();
//        if (!orders.isEmpty()) {
//            pageable = PageRequest.of(offset, limit, Sort.by(orders));
//        }
//        List<Product> products;
//        products = this.productService.findAllProductByCategoryId(categoriesId);
//    }

    @GetMapping("/getById")
    public ResponseEntity<Product> getById(@RequestParam(name = "id", required = false, defaultValue = "2") long id) {
        Product foundProduct = this.productService.findProductById(id);
        if (ObjectUtils.isEmpty(foundProduct)){
            return new ResponseEntity<>(foundProduct, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundProduct, HttpStatus.OK);
    }
}
