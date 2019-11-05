package com.insidecoding.opium.agent.service;

import com.google.gson.Gson;
import com.insidecoding.opium.agent.entity.Device;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class NetworkService {
    private static final Pattern IPv4RegexPattern = Pattern
            .compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static final Logger LOG = LoggerFactory.getLogger(NetworkService.class);
    private static final String DEFAULT_MASTER = "http://localhost:2020";

    private static final String master;
    private static final HttpClient httpClient;

    static {
        int connTimeout = Integer.parseInt(System.getProperty("connTimeout", "30"));

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connTimeout * 1000)
                .build();

        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(new PoolingHttpClientConnectionManager()).build();
        master = System.getProperty("master", DEFAULT_MASTER);

    }

    private static boolean validate(final String ip) {
        return IPv4RegexPattern.matcher(ip).matches();
    }

    public void sendToMaster(List<Device> devices) {
        try {
            HttpPut post = new HttpPut(master + "/device");
            post.addHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(new Gson().toJson(devices)));

            httpClient.execute(post).getEntity().getContent().close();
        } catch (Exception e) {
            LOG.warn("Error while sending test data: ", e);
        }
    }

    public String getLocalhostIpv4() throws SocketException {
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        String ipToReturn = null;

        while (e.hasMoreElements()) {
            NetworkInterface n = e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();

            while (ee.hasMoreElements()) {
                InetAddress i = ee.nextElement();
                String currentAddress = i.getHostAddress();

                if (i.isSiteLocalAddress() && !i.isLoopbackAddress() && validate(currentAddress)) {
                    ipToReturn = currentAddress;
                }
            }
        }

        return ipToReturn;
    }
}
