package com.productiveedge.content_mgmt_automation.service;

import com.productiveedge.content_mgmt_automation.service.exception.ApacheHttpClientException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApacheHttpClient {
    private static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    public static String sendGet(String url, Map<String, String> headers, Map<String, String> paramaters) throws ApacheHttpClientException {
        try {
            HttpGet request = new HttpGet(url);
            HttpClientBuilder builder = HttpClientBuilder.create();
            if (headers != null) {
                String allowRedirect = headers.get("allow_redirect");
                if (allowRedirect != null && !Boolean.valueOf(allowRedirect)) {
                    builder.disableRedirectHandling();
                }
                // add request header
                addRequestHeaders(request, headers);

            }

            HttpClient client = builder.build();
            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RedirectException("The url=" + url + " was moved or doesn't exist.");
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (RedirectException | IOException e) {
            throw new ApacheHttpClientException(e.getMessage());
        }

    }

    private static void addRequestHeaders(HttpRequestBase requestBase, Map<String, String> headers) {
        requestBase.addHeader(org.apache.http.HttpHeaders.USER_AGENT, USER_AGENT_VALUE);
        headers.forEach((key, value) -> requestBase.addHeader(key, value));
    }

    private static void addRequestParameters(HttpPost post, Map<String, String> paramaters) throws UnsupportedEncodingException {
        List<NameValuePair> urlParameters = new ArrayList<>();
        paramaters.forEach((key, value) -> urlParameters.add(new BasicNameValuePair(key, value)));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
    }


    // HTTP POST request
    public static StringBuilder sendPost(String url, Map<String, String> headers, Map<String, String> paramaters) throws Exception {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        // add headers
        addRequestHeaders(post, headers);

        //add parameters
        addRequestParameters(post, paramaters);

        HttpResponse response = client.execute(post);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }
}
