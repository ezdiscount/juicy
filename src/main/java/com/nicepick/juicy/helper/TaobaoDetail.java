package com.nicepick.juicy.helper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaobaoDetail {
    private final static Logger LOGGER = LoggerFactory.getLogger(TaobaoDetail.class);
    private final static String BASE = "http://item.taobao.com/item.htm?id=";
    private final static int FETCH_TIMEOUT = 10000;
    private final static int JS_TIMEOUT = 10000;

    public static List<String> get(String id) {
        List<String> details = new ArrayList<>();
        //HttpConnection connection = (HttpConnection) Jsoup.connect(BASE + id);
        //Document document = connection.validateTLSCertificates(false).userAgent(BrowserVersion.CHROME.getUserAgent()).get();
        Document document = Jsoup.parse(getRawDetail(id));
        for (Element e : document.select("img")) {
             //_640x0q85s150_.webp
             //_640x0_.webp
             //_1080x0_.webp
             //_1280x0_.webp
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
            HtmlPage htmlPage = wc.getPage(request);
            DomNodeList<HtmlElement> script = htmlPage.getHead().getElementsByTagName("script");
            String detailUrl = "";
            for(HtmlElement e : script) {
                String textContent = e.getTextContent();
                if(textContent.contains("var g_config = {")) {
                    for(String line : textContent.split("\n")) {
                        if(line.startsWith("        descUrl")) {
                            detailUrl = "http:" + getFirstMatch(line)
                                    .replaceAll("\\s+:","")
                                    .replace("'","");
                            break;
                        }
                    }
                    break;
                }
            }
            if(StringUtils.isNotEmpty(detailUrl)) {
                raw = wc.getPage(detailUrl)
                        .getWebResponse()
                        .getContentAsString()
                        .replace("var desc='","")
                        .replace("';","");
            }
        } catch (Throwable t) {
            LOGGER.error("Taobao raw detail error {} {}", t.getClass().getName(), t.getMessage(), t);
        } finally {
            wc.close();
        }
        return raw;
    }

    private static String getFirstMatch(String str) {
        Pattern pattern = Pattern.compile("'//dsc.taobaocdn.com/i[0-9]+/[0-9]+/[0-9]+/[0-9]+/.+[0-9]+'\\s+:");
        Matcher matcher = pattern.matcher(str);
        String result = null;
        if(matcher.find()) {
            result = matcher.group();
        }
        return result;
    }
}
