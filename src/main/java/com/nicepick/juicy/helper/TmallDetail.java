package com.nicepick.juicy.helper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TmallDetail {
    private final static Logger LOGGER = LoggerFactory.getLogger(TmallDetail.class);
    private final static String BASE = "https://detail.tmall.com/item.htm?id=";
    private final static int FETCH_TIMEOUT = 10000;
    private final static int JS_TIMEOUT = 10000;

    public static List<String> get(String id) {
        List<String> details = new ArrayList<>();
        Document document = Jsoup.parse(getRawDetail(id));
        for (Element e : document.select("img")) {
            details.add(e.attr("src") + "_640x0q85s150_.webp");
        }
        return details;
    }

    private static String getRawDetail(String id) {
        String raw = "";
        WebClient wc = new WebClient(BrowserVersion.BEST_SUPPORTED);
        wc.getOptions().setUseInsecureSSL(true);
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setCssEnabled(false);
        wc.getOptions().setRedirectEnabled(true);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(FETCH_TIMEOUT);
        wc.setJavaScriptTimeout(JS_TIMEOUT);
        wc.setAjaxController(new NicelyResynchronizingAjaxController());
        try {
            WebRequest request = new WebRequest(UrlUtils.toUrlUnsafe(BASE + id));
            wc.getCurrentWindow().getTopWindow().setOuterHeight(Integer.MAX_VALUE);
            wc.getCurrentWindow().getTopWindow().setInnerHeight(Integer.MAX_VALUE);

            HtmlPage htmlPage = wc.getPage(request);
            htmlPage.getEnclosingWindow().setOuterHeight(Integer.MAX_VALUE);
            htmlPage.getEnclosingWindow().setInnerHeight(Integer.MAX_VALUE);
            htmlPage.executeJavaScript(String.format("javascript:window.scrollBy(0,%d);", Integer.MAX_VALUE));
            wc.waitForBackgroundJavaScript(JS_TIMEOUT);
            raw = htmlPage.getElementById("description")
                    .asXml()
                    .replaceAll("src=\"//.{0,100}.png\" data-ks-lazyload=", "src=");
        } catch (Throwable t) {
            LOGGER.error("Tmall raw detail error {} {}", t.getClass().getName(), t.getMessage(), t);
        } finally {
            wc.close();
        }
        return raw;
    }
}
