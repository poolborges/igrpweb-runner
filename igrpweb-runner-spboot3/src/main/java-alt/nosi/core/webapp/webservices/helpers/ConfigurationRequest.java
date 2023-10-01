package nosi.core.webapp.webservices.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
//import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import nosi.core.webapp.helpers.UrlHelper;

/**
 * * @author: Emanuel Pereira
 * 22 Jan 2018
 */
public class ConfigurationRequest {

	private RestRequest request;
	
	public ConfigurationRequest(RestRequest request) {
		this.request = request;
	}
	public Client bluidClient() {
		return  ClientBuilder.newBuilder()
				.sslContext(this.createSslContext())
				.hostnameVerifier(this.getHostNameVerifier())
				//.register(this.getHttpAuthenticationFeature())
                                .register(new Authenticator(this.request.getUsername(),this.request.getPassword()))
				.build();
	}

	public String getUrl() {
		return UrlHelper.urlEncoding(this.request.getBase_url());
	}

/*	public HttpAuthenticationFeature getHttpAuthenticationFeature() {
		return HttpAuthenticationFeature.basic(this.request.getUsername(),this.request.getPassword());
	}*/
	
	public HostnameVerifier getHostNameVerifier() {
		return new HostnameVerifier() {
			@Override
			public boolean verify(String s, SSLSession sslSession) {
				return true;
			}
		};
	}
	
	public SSLContext createSslContext() {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, this.createTrustManager(), new java.security.SecureRandom());// new java.security.SecureRandom()
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
		}
		return sslContext;
	}
	
	public TrustManager[] createTrustManager() {
		return new TrustManager[ ] {
			    new X509TrustManager() {			    	
			        @Override
			        public X509Certificate[] getAcceptedIssuers() {
			            return null;
			        }
			        @Override
			        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			        }
			        @Override
			        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			        }
			    }
        };
	}
        
        //@Provider
class Authenticator implements ClientRequestFilter {

    private String user;
    private String password;

    public Authenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add(
            HttpHeaders.AUTHORIZATION, getBasicAuthentication());
    }

    private String getBasicAuthentication() {
        String userAndPassword = this.user + ":" + this.password;
        byte[] userAndPasswordBytes = new byte[0];
        try {
            userAndPasswordBytes = userAndPassword.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConfigurationRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Basic " + Base64.getEncoder().encodeToString(userAndPasswordBytes);
    }
}
}
