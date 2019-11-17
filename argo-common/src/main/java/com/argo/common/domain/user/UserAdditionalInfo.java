package com.argo.common.domain.user;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "user_additional_infos", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserAdditionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_additional_info_seq")
    @SequenceGenerator(name="user_additional_info_seq", sequenceName="user_additional_info_seq", allocationSize=1)
    @Column(name = "user_additional_info_id", nullable = false)
    private Long userAdditionalInfoId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "argo_user_id")
    private ArgoUser argoUser;

    @ElementCollection
    @CollectionTable(name = "user_additional_info_values")
    @MapKeyColumn(name = "user_additional_info_id")
    @Column(name = "value")
    private Map<String, String> infos = Maps.newHashMap();

    public void setLicenseNumber(String input) {
        infos.put("LicenseNumber", input);
    }

    public void setBusinessType(String input) {
        infos.put("BusinessType", input);
    }

    public void setRepresentativeName(String input) {
        infos.put("RepresentativeName", input);
    }

    public void setPostCode(String input) {
        infos.put("PostCode", input);
    }

    public void setBaseAddress(String input) {
        infos.put("BaseAddress", input);
    }

    public void setDetailAddress(String input) {
        infos.put("DetailAddress", input);
    }

    public void setSaleForm(String input) {
        infos.put("SaleForm", input);
    }

    public void setSaleType(String input) {
        infos.put("SaleType", input);
    }

    public void setLicenseLocation(String input) {
        infos.put("licenseLocation", input);
    }

    public void setFileName(String input) {
        infos.put("LicenseFileName", input);
    }

    public String getFileName() {
        return infos.get("LicenseFileName");
    }

    public String getFileLocation() {
        return infos.get("licenseLocation");
    }
 }
