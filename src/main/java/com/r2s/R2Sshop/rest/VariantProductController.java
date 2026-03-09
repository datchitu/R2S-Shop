package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.repository.VariantProductRepository;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/variant_product")
public class VariantProductController {
    private VariantProductRepository variantProductRepository;


}
