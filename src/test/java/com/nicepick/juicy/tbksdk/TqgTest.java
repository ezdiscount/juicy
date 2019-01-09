package com.nicepick.juicy.tbksdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkJuTqgGetRequest;
import com.taobao.api.response.TbkJuTqgGetResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TqgTest {
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    Configuration configuration;

    @Test
    public void test() throws ApiException, JsonProcessingException {
        TaobaoClient client = new DefaultTaobaoClient(configuration.getAppUrl(), configuration.getAppKey(), configuration.getAppSecret());
        TbkJuTqgGetRequest req = new TbkJuTqgGetRequest();
        req.setAdzoneId(59359250041l);
        req.setFields("click_url,pic_url,reserve_price,zk_final_price,total_amount,sold_num,title,category_name,start_time,end_time");
        req.setStartTime(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        req.setEndTime(Date.from(LocalDate.of(Year.now().getValue(), Month.DECEMBER, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        req.setPageNo(1L);
        req.setPageSize(5L);
        TbkJuTqgGetResponse rsp = client.execute(req);
        Assert.assertTrue(rsp.getTotalResults() >= rsp.getResults().size());
        for (TbkJuTqgGetResponse.Results i : rsp.getResults()) {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(i));
        }

    }
}
