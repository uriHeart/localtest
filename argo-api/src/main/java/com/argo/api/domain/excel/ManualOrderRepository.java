package com.argo.api.domain.excel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManualOrderRepository extends JpaRepository<ManualOrder,ManualOrderPk> {
    @Override
    void deleteById(ManualOrderPk manualOrderPk);

    List<ManualOrder> findAllByVendorId(Long vendorId);
}
