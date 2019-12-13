package com.argo.api.service.gopotal;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
<<<<<<< HEAD

=======
>>>>>>> 4b81c6ee3ba997ff2807b784209579eabc7dc2ed

public class BusinessNumberResultTest {

    @Test
    public void isLastPage_EmptyObject() {
        BusinessNumberResult result = new BusinessNumberResult();
        Assert.assertFalse(result.isLastPage());
    }

    @Test
    public void isLastPage_noLast() {
        Body body = new Body();
        body.setTotalCount(100);
        body.setNumOfRows(10);
        body.setPageNo(1);

        Response response = new Response();
        response.setBody(body);

        BusinessNumberResult result = new BusinessNumberResult();
        result.setResponse(response);
        Assert.assertFalse(result.isLastPage());
    }

    @Test
    public void isLastPage_Last() {
        Body body = new Body();
        body.setTotalCount(100);
        body.setNumOfRows(100);
        body.setPageNo(1);

        Response response = new Response();
        response.setBody(body);

        BusinessNumberResult result = new BusinessNumberResult();
        result.setResponse(response);
        Assert.assertTrue(result.isLastPage());
    }

    @Test
    public void isCompanyNameContains() {
        Item item1 = new Item();
        item1.setWkplNm("ARGO");

        Items items = new Items();
        items.setItem(Lists.newArrayList(item1));

        Body body = new Body();
        body.setItems(items);

        Response response = new Response();
        response.setBody(body);

        BusinessNumberResult result = new BusinessNumberResult();
        result.setResponse(response);
        Assert.assertTrue(result.isCompanyNameContains("ARGO"));
    }

    @Test
    public void isCompanyNameContains_no() {
        Item item1 = new Item();
        item1.setWkplNm("ARGO");

        Items items = new Items();
        items.setItem(Lists.newArrayList(item1));

        Body body = new Body();
        body.setItems(items);

        Response response = new Response();
        response.setBody(body);

        BusinessNumberResult result = new BusinessNumberResult();
        result.setResponse(response);
        Assert.assertFalse(result.isCompanyNameContains("RG"));
    }
}
