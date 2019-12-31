package com.argo.common.domain.vendor;
import lombok.Data;

import java.util.Date;


@Data
public class VendorWorkplaceShorten {
    public Long workplaceId;
    public String type; //
    public String workplaceName;
    public String address; //
    public Double latitude;
    public Double longitude;
    public Date createdAt;
}
