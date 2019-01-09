package com.nicepick.juicy.tbksdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicepick.juicy.tbksdk.Configuration.PLATFORM;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.NTbkItem;
import com.taobao.api.request.TbkItemGetRequest;
import com.taobao.api.response.TbkItemGetResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetItem {
    private ObjectMapper mapper;
    @Autowired
    Configuration configuration;

    @Before
    public void prepare() {
        mapper = new ObjectMapper();
    }

    @Test
    public void test() throws JsonProcessingException, ApiException {
        testApi(configuration.getAppUrl(), PLATFORM.PC);
        testApi(configuration.getAppUrl(), PLATFORM.MOBILE);
        testApi(configuration.getWorldUrl(), PLATFORM.PC);
        testApi(configuration.getWorldUrl(), PLATFORM.MOBILE);
    }

    private void testApi(String apiUrl, PLATFORM platform) throws ApiException, JsonProcessingException {
        System.out.println("===== api: " + apiUrl);
        System.out.println("===== platform: " + platform.name());
        TaobaoClient client = new DefaultTaobaoClient(apiUrl, configuration.getAppKey(), configuration.getAppSecret());
        TbkItemGetRequest req = new TbkItemGetRequest();
        req.setFields("num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick");
        req.setQ("女装");
        req.setPlatform(platform.value());
        req.setPageNo(1L);
        req.setPageSize(5L);
        TbkItemGetResponse rsp = client.execute(req);
        Assert.assertTrue(rsp.getTotalResults() >= rsp.getResults().size());
        for (NTbkItem i : rsp.getResults()) {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(i));
        }
    }
}
