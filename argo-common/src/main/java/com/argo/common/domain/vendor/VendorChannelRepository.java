package com.argo.common.domain.vendor;

import com.argo.common.domain.common.enums.YesOrNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorChannelRepository extends JpaRepository<VendorChannel, Long> {
    List<VendorChannel> findByEnabled(YesOrNo yesOrNo);
}
