package com.argo.api.service.gopotal;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BusinessNumberCheckService {

    @Value("${goData.token}")
    private String goDataToken;

    @Value("${goData.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public boolean checkNumber(String number, String companyName, int pageNo) {
        try {
            BusinessNumberResult searchResult = getBusinessNumberResult(number, pageNo);
            if (searchResult ==  null) {
                return false;
            }

            boolean matched = searchResult.isCompanyNameContains(companyName);
            if (searchResult.isLastPage()) {
                return matched;
            }

            return matched || checkNumber(number, companyName, pageNo + 1);
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private BusinessNumberResult getBusinessNumberResult(String number, int pageNo) throws URISyntaxException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                                           .queryParam("bzowr_rgst_no", number)
                                           .queryParam("pageNo", pageNo)
                                           .queryParam("numOfRows", 1000);
        URI uri = new URI(builder.toUriString() + "&serviceKey=" + goDataToken);
        return restTemplate.getForObject(uri, BusinessNumberResult.class);
    }
}
