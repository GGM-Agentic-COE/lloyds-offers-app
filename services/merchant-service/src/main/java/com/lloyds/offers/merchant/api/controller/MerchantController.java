package com.lloyds.offers.merchant.api.controller;

import com.lloyds.offers.merchant.application.command.RegisterMerchantHandler;
import com.lloyds.offers.merchant.domain.model.Merchant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final RegisterMerchantHandler registerMerchantHandler;

    public MerchantController(RegisterMerchantHandler registerMerchantHandler) {
        this.registerMerchantHandler = registerMerchantHandler;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Merchant register(@RequestBody Merchant merchant) {
        return registerMerchantHandler.handle(merchant);
    }
}
