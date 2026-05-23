package com.lloyds.offers.merchant.application.command;

import com.lloyds.offers.merchant.domain.model.Merchant;
import com.lloyds.offers.merchant.domain.port.MerchantRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterMerchantHandler {

    private final MerchantRepository merchantRepository;

    public RegisterMerchantHandler(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant handle(Merchant merchant) {
        merchant.setStatus(Merchant.MerchantStatus.SUBMITTED);
        return merchantRepository.save(merchant);
    }
}
