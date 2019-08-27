package com.productiveedge.content_mgmt_automation.service;

import com.productiveedge.content_mgmt_automation.flow.exception.HttpRedirectException;
import com.productiveedge.content_mgmt_automation.service.exception.ApacheHttpClientException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static com.productiveedge.content_mgmt_automation.service.ApacheHttpClient.HttpHeader.ALLOW_REDIRECT;

public class ApacheHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(ApacheHttpClient.class);

    private static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    public enum HttpHeader {
        ALLOW_REDIRECT
    }


    public static String sendGet(String url, Map<String, String> headers) throws ApacheHttpClientException {
        try {
            HttpGet request = new HttpGet(url);
            HttpClientBuilder builder = HttpClientBuilder.create();
            if (headers != null) {
                String allowRedirect = headers.get(ALLOW_REDIRECT.name().toLowerCase());
                if (allowRedirect != null && !Boolean.valueOf(allowRedirect)) {
                    builder.disableRedirectHandling();
                }
                // add request headerHttpRequestBase
                addRequestHeaders(request, headers);

            }

            HttpClient client = builder.build();
            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new HttpRedirectException("The url=" + url + " was moved or doesn't exist.");
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (HttpRedirectException | IOException e) {
            throw new ApacheHttpClientException(e.getMessage());
        }

    }

    private static void addRequestHeaders(HttpRequestBase requestBase, Map<String, String> headers) {
        requestBase.addHeader(org.apache.http.HttpHeaders.USER_AGENT, USER_AGENT_VALUE);
        headers.forEach(requestBase::addHeader);
    }
}
