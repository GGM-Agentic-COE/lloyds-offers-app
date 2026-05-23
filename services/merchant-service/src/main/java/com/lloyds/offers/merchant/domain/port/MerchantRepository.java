package com.lloyds.offers.merchant.domain.port;

import com.lloyds.offers.merchant.domain.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
}
