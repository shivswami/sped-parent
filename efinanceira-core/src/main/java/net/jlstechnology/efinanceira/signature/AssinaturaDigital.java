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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AssinaturaDigital {
	
	private static Logger log = LoggerFactory.getLogger(AssinaturaDigital.class);
	
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
				X509Certificate cert = getCertificate();
				sig.addKeyInfo(cert);
				sig.sign(privateKey);
				log.debug("Assinando evento {}", uri);
			}
		}
		
		return outputXML(document);		
	}
	
	private static Document getDocumentFactory(InputStream is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		return factory.newDocumentBuilder().parse(is);
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
	
	private static X509Certificate getCertificate() throws Exception {
		KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
		try {
			ks.load(null, null);
		} catch (IOException e) {
			throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.", e);
		}

		KeyStore.PrivateKeyEntry pkEntry = null;
		Enumeration<String> aliasesEnum = ks.aliases();
		while (aliasesEnum.hasMoreElements()) {
			String alias = aliasesEnum.nextElement();
			if (ks.isKeyEntry(alias)) {
				pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
						new KeyStore.PasswordProtection("".toCharArray()));
				privateKey = pkEntry.getPrivateKey();
				break;
			}
		}
		return (X509Certificate) pkEntry.getCertificate();
	}
	
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
