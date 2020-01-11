package com.argo.common.domain.vendor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class VendorWorkplaceShorten {

    public Long workplaceId;
    public String type; //
    public String workplaceName;
    public String address; //
    public Double latitude;
    public Double longitude;
    public Date createdAt;
}
