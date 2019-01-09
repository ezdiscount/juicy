package com.nicepick.juicy;

import com.nicepick.juicy.bean.ProductAttribute;
import com.nicepick.juicy.helper.TaobaoDetail;
import com.nicepick.juicy.tbksdk.Configuration;
import com.nicepick.juicy.tbksdk.Configuration.PLATFORM;
import com.nicepick.juicy.tbksdk.DataSet;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkItemInfoGetResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class ProductAttributeTest {
    @Autowired
    Configuration configuration;

    @Test
    public void test() throws ApiException {
        ProductAttribute pa = new ProductAttribute();
        pa.version = 1;
        pa.pid = DataSet.TAOBAO_ITEM;
        pa.detailImages = TaobaoDetail.get(pa.pid);

        Assert.assertNotEquals(pa.detailImages.size(), 0);

        TaobaoClient client = new DefaultTaobaoClient(configuration.getAppUrl(), configuration.getAppKey(), configuration.getAppSecret());
        TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
        req.setNumIids(pa.pid);
        req.setPlatform(PLATFORM.MOBILE.value());
        TbkItemInfoGetResponse rsp = client.execute(req);
        TbkItemInfoGetResponse.NTbkItem item = rsp.getResults().get(0);
        pa.thumbnailUrl = item.getPictUrl();
        pa.scrollImages = item.getSmallImages();
        pa.volume = item.getVolume();

        System.out.println(pa);
    }
}
