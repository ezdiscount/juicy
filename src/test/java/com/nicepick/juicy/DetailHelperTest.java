package com.nicepick.juicy;

import com.nicepick.juicy.helper.TaobaoDetail;
import com.nicepick.juicy.helper.TmallDetail;
import com.nicepick.juicy.tbksdk.DataSet;
import org.junit.Test;

public class DetailHelperTest {
    @Test
    public void getTaobaoDetail() {
        for (String url : TaobaoDetail.get(DataSet.TAOBAO_ITEM)) {
            System.out.println(url);
        }
    }

    @Test
    public void getTmallDetail() {
        for (String url : TmallDetail.get(DataSet.TMALL_ITEM)) {
            System.out.println(url);
        }
    }
}
