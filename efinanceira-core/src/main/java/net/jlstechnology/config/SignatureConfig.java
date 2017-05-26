package net.jlstechnology.config;

//import java.io.IOException;
//import java.security.KeyStore;
//import java.security.PrivateKey;
//import java.security.cert.X509Certificate;
//import java.util.Enumeration;
//
//import org.apache.camel.CamelContext;
//import org.apache.camel.component.xmlsecurity.api.DefaultKeyAccessor;
//import org.apache.camel.component.xmlsecurity.api.DefaultKeySelector;
//import org.apache.camel.util.jsse.KeyStoreParameters;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

//@Configuration
public class SignatureConfig {
	
//	private final CamelContext camelContext;
//	
//	public SignatureConfig(CamelContext camelContext) {
//		this.camelContext = camelContext;
//	}
//
//	@Bean
//	public DefaultKeyAccessor defaultKeyAccessor() throws Exception {
//		DefaultKeyAccessor defaultKeyAccessor = new DefaultKeyAccessor();
//		defaultKeyAccessor.setCamelContext(camelContext);
//		defaultKeyAccessor.setKeyStore(getKeyStore());
//		defaultKeyAccessor.setAlias(getAlias());
//		return defaultKeyAccessor;
//	}
//	
//	@Bean
//	public DefaultKeySelector defaultKeySelector() throws Exception {
//		DefaultKeySelector defaultKeySelector = new DefaultKeySelector();
//		defaultKeySelector.setCamelContext(camelContext);
//		//defaultKeySelector.setKeyStore(getKeyStore());
//		defaultKeySelector.setKeyStoreParameters(keyStoreParameters());
//		defaultKeySelector.setAlias(getAlias());
//		defaultKeySelector.singletonKeySelector(getCertificate().getPublicKey());
//		return defaultKeySelector;
//	}
//	
//	@Bean
//	public KeyStoreParameters keyStoreParameters() throws Exception {
//		KeyStoreParameters parameters = new KeyStoreParameters();
//		parameters.setCamelContext(camelContext);
//		parameters.setType("Windows-MY");
//		parameters.setProvider("SunMSCAPI");
//		parameters.setPassword("");
//		return parameters;
//	}
//
//	private static KeyStore getKeyStore() throws Exception {
//		KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
//		ks.load(null, null);
//		return ks;
//	}
//
//	private static String getAlias() throws Exception {
//		KeyStore ks = getKeyStore();
//		Enumeration<String> aliasesEnum = ks.aliases();
//		String alias = null;
//		while (aliasesEnum.hasMoreElements()) {
//			alias = aliasesEnum.nextElement();
//			if (ks.isKeyEntry(alias)) {
//				return alias;
//			}
//		}
//		return "";
//	}
//	
//	private X509Certificate getCertificate() throws Exception {
//		KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
//		//PrivateKey privateKey = null;
//		try {
//			ks.load(null, null);
//		} catch (IOException e) {
//			throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.", e);
//		}
//
//		KeyStore.PrivateKeyEntry pkEntry = null;
//		Enumeration<String> aliasesEnum = ks.aliases();
//		while (aliasesEnum.hasMoreElements()) {
//			String alias = aliasesEnum.nextElement();
//			if (ks.isKeyEntry(alias)) {
//				pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
//						new KeyStore.PasswordProtection("".toCharArray()));
//				//privateKey = pkEntry.getPrivateKey();
//				break;
//			}
//		}
//		return (X509Certificate) pkEntry.getCertificate();
//	}

	// public class DefaultKeyAccessor extends DefaultKeySelector implements
	// KeyAccessor {
	//
	// private String provider;
	//
	// public DefaultKeyAccessor() {
	// }
	//
	// public String getProvider() {
	// return provider;
	// }
	//
	// public void setProvider(String provider) {
	// this.provider = provider;
	// }
	//
	// @Override
	// public KeySelector getKeySelector(Message message) throws Exception {
	// return this;
	// }
	//
	// @Override
	// public KeyInfo getKeyInfo(Message message, Node messageBody,
	// KeyInfoFactory factory) throws Exception {
	// return createKeyInfo(factory);
	// }
	//
	// private KeyInfo createKeyInfo(KeyInfoFactory kif) throws Exception {
	//
	// X509Certificate[] chain = getCertificateChain();
	// if (chain == null) {
	// return null;
	// }
	// X509Data x509D = kif.newX509Data(Arrays.asList(chain));
	// return kif.newKeyInfo(Collections.singletonList(x509D), "_" +
	// UUID.randomUUID().toString());
	// }
	//
	// private X509Certificate[] getCertificateChain() throws Exception {
	// KeyStore keystore = getKeyStore();
	// if (keystore == null) {
	// return null;
	// }
	// String alias = this.getAlias();
	// if (alias == null) {
	// return null;
	// }
	// Certificate[] certs = keystore.getCertificateChain(alias);
	// if (certs == null) {
	// return null;
	// }
	// ArrayList<X509Certificate> certList = new
	// ArrayList<X509Certificate>(certs.length);
	// for (Certificate cert : certs) {
	// if (cert instanceof X509Certificate) {
	// certList.add((X509Certificate) cert);
	// }
	// }
	// return certList.toArray(new X509Certificate[certList.size()]);
	// }
	//
	// public KeyStore getKeyStore() throws Exception {
	// KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
	// try {
	// ks.load(null, null);
	// } catch (IOException | NoSuchAlgorithmException | CertificateException e)
	// {
	// throw new Exception("Senha do Certificado Digital incorreta ou
	// Certificado inválido.", e);
	// }
	// return ks;
	// }
	//
	// public String getAlias() throws Exception {
	// KeyStore ks = getKeyStore();
	// Enumeration<String> aliasesEnum = ks.aliases();
	// String alias = null;
	// while (aliasesEnum.hasMoreElements()) {
	// alias = aliasesEnum.nextElement();
	// if (ks.isKeyEntry(alias)) {
	// return alias;
	// }
	// }
	// return "";
	// }
	//
	// }

}
