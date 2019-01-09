package com.nicepick.juicy.tbksdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicepick.juicy.Main;
import com.nicepick.juicy.tbksdk.Configuration.PLATFORM;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkItemInfoGetResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class GetItemInfo {
    private final static String ITEMS = DataSet.TAOBAO_ITEM + "," + DataSet.TMALL_ITEM;
    private ObjectMapper mapper;
    @Autowired
    Configuration configuration;

    @Before
    public void prepare() {
        mapper = new ObjectMapper();
    }

    @Test
    public void test() throws ApiException, IOException {
        testApi(configuration.getAppUrl(), PLATFORM.PC);
        testApi(configuration.getAppUrl(), PLATFORM.MOBILE);
        testApi(configuration.getWorldUrl(), PLATFORM.PC);
        testApi(configuration.getWorldUrl(), PLATFORM.MOBILE);
    }

    private void testApi(String apiUrl, PLATFORM platform) throws UnknownHostException, ApiException, JsonProcessingException {
        System.out.println("===== api: " + apiUrl);
        System.out.println("===== items: " + ITEMS);
        System.out.println("===== platform: " + platform.name());
        TaobaoClient client = new DefaultTaobaoClient(apiUrl, configuration.getAppKey(), configuration.getAppSecret());
        TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
        req.setNumIids(ITEMS);
        req.setPlatform(platform.value());
        req.setIp(InetAddress.getLocalHost().getHostAddress());
        TbkItemInfoGetResponse rsp = client.execute(req);
        for (TbkItemInfoGetResponse.NTbkItem i : rsp.getResults()) {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(i));
        }
    }
}
