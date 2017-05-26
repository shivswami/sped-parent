package org.apache.ws.security.components.crypto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509CRL;
import java.util.Collections;
import java.util.Properties;

import org.apache.wss4j.common.ext.WSSecurityException;

import org.apache.wss4j.common.crypto.Merlin;
import org.apache.wss4j.common.crypto.PasswordEncryptor;

public class PKCS11Device extends Merlin {
	
	private static final org.slf4j.Logger LOG =
	        org.slf4j.LoggerFactory.getLogger(PKCS11Device.class);
	    private static final boolean DO_DEBUG = LOG.isDebugEnabled();

	    public PKCS11Device() {
	        super();
	    }

	    public PKCS11Device(Properties properties, ClassLoader loader, PasswordEncryptor passwordEncryptor)
	        throws WSSecurityException, IOException {
	        super(properties, loader, passwordEncryptor);
	    }

	    @Override
	    public void loadProperties(Properties properties, ClassLoader loader, PasswordEncryptor passwordEncryptor)
	        throws WSSecurityException, IOException {
	        if (properties == null) {
	            return;
	        }
	        this.properties = properties;
	        this.passwordEncryptor = passwordEncryptor;

	        String prefix = PREFIX;
	        for (Object key : properties.keySet()) {
	            if (key instanceof String) {
	                String propKey = (String)key;
	                if (propKey.startsWith(PREFIX)) {
	                    break;
	                } else if (propKey.startsWith(OLD_PREFIX)) {
	                    prefix = OLD_PREFIX;
	                    break;
	                }
	            }
	        }

	        //
	        // Load the provider(s)
	        //
	        String provider = properties.getProperty(prefix + CRYPTO_KEYSTORE_PROVIDER);
	        if (provider != null) {
	            provider = provider.trim();
	        }
	        String certProvider = properties.getProperty(prefix + CRYPTO_CERT_PROVIDER);
	        if (certProvider != null) {
	            setCryptoProvider(certProvider);
	        }
	        //
	        // Load the KeyStore
	        //
	        String alias = properties.getProperty(prefix + KEYSTORE_ALIAS);
	        if (alias != null) {
	            alias = alias.trim();
	            setDefaultX509Identifier(alias);
	        }
	        String keyStoreLocation = properties.getProperty(prefix + KEYSTORE_FILE);
	        if (keyStoreLocation == null) {
	            keyStoreLocation = properties.getProperty(prefix + OLD_KEYSTORE_FILE);
	        }
	        String keyStorePassword = properties.getProperty(prefix + KEYSTORE_PASSWORD, "security");
	        if (keyStorePassword != null) {
	            keyStorePassword = keyStorePassword.trim();
	            keyStorePassword = decryptPassword(keyStorePassword, passwordEncryptor);
	        }
	        String keyStoreType = properties.getProperty(prefix + KEYSTORE_TYPE, KeyStore.getDefaultType());
	        if (keyStoreType != null) {
	            keyStoreType = keyStoreType.trim();
	        }
	        if (keyStoreLocation != null) {
	            keyStoreLocation = keyStoreLocation.trim();

	            try (InputStream is = loadInputStream(loader, keyStoreLocation)) {
	                keystore = load(is, keyStorePassword, provider, keyStoreType);
	                if (DO_DEBUG) {
	                    LOG.debug(
	                        "The KeyStore " + keyStoreLocation + " of type " + keyStoreType
	                        + " has been loaded"
	                    );
	                }
	            }
	        } else {
	            keystore = load(null, keyStorePassword, provider, keyStoreType);
	        }

	        //
	        // Load the TrustStore
	        //
	        String trustStorePassword = properties.getProperty(prefix + TRUSTSTORE_PASSWORD, "changeit");
	        if (trustStorePassword != null) {
	            trustStorePassword = trustStorePassword.trim();
	            trustStorePassword = decryptPassword(trustStorePassword, passwordEncryptor);
	        }
	        String trustStoreType = properties.getProperty(prefix + TRUSTSTORE_TYPE, KeyStore.getDefaultType());
	        if (trustStoreType != null) {
	            trustStoreType = trustStoreType.trim();
	        }
	        String loadCacerts = properties.getProperty(prefix + LOAD_CA_CERTS, "false");
	        if (loadCacerts != null) {
	            loadCacerts = loadCacerts.trim();
	        }
	        String trustStoreLocation = properties.getProperty(prefix + TRUSTSTORE_FILE);
	        if (trustStoreLocation != null) {
	            trustStoreLocation = trustStoreLocation.trim();

	            try (InputStream is = loadInputStream(loader, trustStoreLocation)) {
	                truststore = load(is, trustStorePassword, provider, trustStoreType);
	                if (DO_DEBUG) {
	                    LOG.debug(
	                        "The TrustStore " + trustStoreLocation + " of type " + trustStoreType
	                        + " has been loaded"
	                    );
	                }
	                loadCACerts = false;
	            }
	        } else if (Boolean.valueOf(loadCacerts)) {
	            String cacertsPath = System.getProperty("java.home") + "/lib/security/cacerts";
	            if (cacertsPath != null) {
	                cacertsPath = cacertsPath.trim();
	            }
	            try (InputStream is = new FileInputStream(cacertsPath)) {
	                String cacertsPasswd = properties.getProperty(prefix + TRUSTSTORE_PASSWORD, "changeit");
	                if (cacertsPasswd != null) {
	                    cacertsPasswd = cacertsPasswd.trim();
	                    cacertsPasswd = decryptPassword(cacertsPasswd, passwordEncryptor);
	                }
	                truststore = load(is, cacertsPasswd, null, KeyStore.getDefaultType());
	                if (DO_DEBUG) {
	                    LOG.debug("CA certs have been loaded");
	                }
	                loadCACerts = true;
	            }
	        } else {
	            truststore = load(null, trustStorePassword, provider, trustStoreType);
	        }
	        //
	        // Load the CRL file
	        //
	        String crlLocation = properties.getProperty(prefix + X509_CRL_FILE);
	        if (crlLocation != null) {
	            crlLocation = crlLocation.trim();

	            try (InputStream is = loadInputStream(loader, crlLocation)) {
	                CertificateFactory cf = getCertificateFactory();
	                X509CRL crl = (X509CRL)cf.generateCRL(is);

	                if (provider == null || provider.length() == 0) {
	                    crlCertStore =
	                        CertStore.getInstance(
	                            "Collection",
	                            new CollectionCertStoreParameters(Collections.singletonList(crl))
	                        );
	                } else {
	                    crlCertStore =
	                        CertStore.getInstance(
	                            "Collection",
	                            new CollectionCertStoreParameters(Collections.singletonList(crl)),
	                            provider
	                        );
	                }
	                if (DO_DEBUG) {
	                    LOG.debug(
	                        "The CRL " + crlLocation + " has been loaded"
	                    );
	                }
	            } catch (Exception e) {
	                if (DO_DEBUG) {
	                    LOG.debug(e.getMessage(), e);
	                }
	                throw new WSSecurityException(WSSecurityException.ErrorCode.FAILURE, e, "failedCredentialLoad");
	            }
	        }
	    }
	
	@SuppressWarnings("restriction")
	@Override
	protected KeyStore load(InputStream input, String storepass, String provider, String type) throws WSSecurityException {
		KeyStore ks = null;
		
		sun.security.pkcs11.SunPKCS11 akisProvider = new sun.security.pkcs11.SunPKCS11(ClassLoader.getSystemResource("SmartCard.properties").getPath());
		Security.addProvider(akisProvider);

        try {
            if (provider == null || provider.length() == 0) {
                ks = KeyStore.getInstance(type);
            } else {
                ks = KeyStore.getInstance(type, akisProvider);
            }

            ks.load(input, storepass == null || storepass.length() == 0
                ? new char[0] : storepass.toCharArray());
        } catch (IOException | GeneralSecurityException e) {
            if (DO_DEBUG) {
                LOG.debug(e.getMessage(), e);
            }
            throw new WSSecurityException(WSSecurityException.ErrorCode.FAILURE, e, "failedCredentialLoad");
        }
        return ks;
	}

}
