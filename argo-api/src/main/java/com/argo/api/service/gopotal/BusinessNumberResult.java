
package com.argo.api.service.gopotal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "response"
})
public class BusinessNumberResult {

    private Response response;

    public boolean isLastPage() {
        try {
            Body body = getResponse().getBody();
            return body.getNumOfRows() * body.getPageNo() >= body.getTotalCount();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCompanyNameContains(String companyName) {
        return getResponse().getBody().getItems().getItem().stream()
            .anyMatch(item -> item.getWkplNm().equals(companyName));
    }
}
