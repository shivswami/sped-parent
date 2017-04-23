package net.jlstechnology.efinanceira.signature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.Exchange;
import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.ElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AssinaturaDigital {
	
	//private static final String CAMINHO_CERTIFICADO = "E:/camel-efinanceira/src/main/resources/s106-2016.pfx";
	//private static final String CAMINHO_CERTIFICADO = "E:/Arquitetura/efinanceira-cxf-rs/src/main/resources/Associacao.pfx";
	private static final String CERTIFICADO = "s106-2016.pfx";
	
	private static PrivateKey privateKey;
	
	public String assinar(Exchange exchange) throws Exception {
		
		InputStream is = exchange.getIn().getBody(InputStream.class);
		String nomeElemento = exchange.getIn().getHeader("nomeElemento", String.class);
		
		Document document = getDocumentFactory(is);
		int qtdElemento = document.getElementsByTagName(nomeElemento).getLength();
		
		for (int i = 0; i < qtdElemento; i++) {

			Element vElement = (Element) document.getElementsByTagName(nomeElemento).item(i);
			Node vParentNodeAssinar = vElement.getParentNode();

			vElement.setIdAttribute("id", true);
			String uri = vElement.getAttribute("id");

			Init.init();

			ElementProxy.setDefaultPrefix(Constants.SignatureSpecNS, "");
			XMLSignature sig = new XMLSignature(document, "", XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256);

			// vElement.getParentNode().appendChild(sig.getElement());
			vParentNodeAssinar.appendChild(sig.getElement());

			{
				Transforms transforms = new Transforms(document);

				transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
				transforms.addTransform(Transforms.TRANSFORM_C14N_OMIT_COMMENTS);
				sig.addDocument("#" + uri, transforms, "http://www.w3.org/2001/04/xmlenc#sha256");
			}

			trimWhitespace(document);

			{
				X509Certificate cert = getCertificate(ClassLoader.getSystemResource(CERTIFICADO).toString()/*CAMINHO_CERTIFICADO*/, "");
				sig.addKeyInfo(cert);
				sig.sign(privateKey);
				System.out.println(i + ") Assinando evento " + uri);
			}
		}
		
		return outputXML(document);
		
	}
	
	private static Document getDocumentFactory(InputStream is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document document = factory.newDocumentBuilder().parse(is);
		return document;
	}
	
	private static void trimWhitespace(Node node) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				child.setTextContent(child.getTextContent().trim());
			}
			trimWhitespace(child);
		}
	}
	
	private static X509Certificate getCertificate(String certificado, String senha) throws Exception {
		//InputStream entrada = new FileInputStream(certificado);
		//KeyStore ks = KeyStore.getInstance("pkcs12");
		KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
		try {
			//ks.load(entrada, senha.toCharArray());
			ks.load(null, null);
		} catch (IOException e) {
			throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");
		}

		KeyStore.PrivateKeyEntry pkEntry = null;
		Enumeration<String> aliasesEnum = ks.aliases();
		while (aliasesEnum.hasMoreElements()) {
			String alias = (String) aliasesEnum.nextElement();
			if (ks.isKeyEntry(alias)) {
				pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
						new KeyStore.PasswordProtection(senha.toCharArray()));
				privateKey = pkEntry.getPrivateKey();
				break;
			}
		}

		X509Certificate cert = (X509Certificate) pkEntry.getCertificate();
		return cert;
	}
	
//	private static X509Certificate getCertificate(String certificado, String senha) throws Exception {
//		//InputStream entrada = new FileInputStream(certificado);
//		//KeyStore ks = KeyStore.getInstance("pkcs12");
//		KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
//		try {
//			//ks.load(entrada, senha.toCharArray());
//			ks.load(null, null);
//		} catch (IOException e) {
//			throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");
//		}
//
//		KeyStore.PrivateKeyEntry pkEntry = null;
//		Enumeration<String> aliasesEnum = ks.aliases();
//		while (aliasesEnum.hasMoreElements()) {
//			String alias = (String) aliasesEnum.nextElement();
//			if (ks.isKeyEntry(alias)) {
//				pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
//						new KeyStore.PasswordProtection(senha.toCharArray()));
//				privateKey = pkEntry.getPrivateKey();
//				break;
//			}
//		}
//
//		X509Certificate cert = (X509Certificate) pkEntry.getCertificate();
//
//		return cert;
//	}
	
	private static String outputXML(Node doc) throws TransformerException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));

		String xml = os.toString();

		if ((xml != null) && (!"".equals(xml))) {
			xml = xml.replaceAll("\\r\\n", "");
			xml = xml.replaceAll(" standalone=\"no\"", "");
		}

		return xml;
	}

}
