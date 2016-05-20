package com.github.hdesale.domain.location;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configuration;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * SSL client helper to build Jersey client.
 *
 * @author Hemant
 */
public class SSLClientHelper {

    private SSLClientHelper() {
        // private constructor to enforce static utilisation
    }

    public static Client getSSLClient(Configuration config) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null, new TrustManager[]{getTrustManager()}, new SecureRandom());
        return ClientBuilder.newBuilder()
                .hostnameVerifier(new TrustAllHostNameVerifier())
                .withConfig(config)
                .sslContext(ctx)
                .build();
    }

    private static TrustManager getTrustManager() {
        return new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
        };
    }

    private static class TrustAllHostNameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
