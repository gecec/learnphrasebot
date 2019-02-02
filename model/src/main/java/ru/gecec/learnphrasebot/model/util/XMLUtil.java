package ru.gecec.learnphrasebot.model.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;


public class XMLUtil {
    private static final Pattern XML_ESCAPE_RE = Pattern.compile("&(?!(amp|lt|gt|quot|apos|#\\d+|#x[0-9A-F]+);)", 10);

    public XMLUtil() {
    }

    public static String serializeNode(Node node) {
        return serializeNode(node, true);
    }

    public static String serializeNode(Node node, boolean omitXmlDeclaration) {
        return serializeNode(node, true, omitXmlDeclaration);
    }

    public static String serializeNode(Node node, boolean insertIndent, boolean omitXmlDeclaration) {
        if (node == null) {
            return "";
        } else {
            Transformer serializer;
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                serializer = transformerFactory.newTransformer();
            } catch (TransformerConfigurationException var7) {
                throw new FaultException(var7);
            }

            serializer.setOutputProperty("method", "xml");
            serializer.setOutputProperty("indent", insertIndent ? "yes" : "no");
            if (insertIndent) {
                serializer.setOutputProperty("doctype-public", "yes");
            }

            serializer.setOutputProperty("omit-xml-declaration", omitXmlDeclaration ? "yes" : "no");
            StringWriter writer = new StringWriter();

            try {
                serializer.transform(new DOMSource(node), new StreamResult(writer));
            } catch (TransformerException var6) {
                throw new FaultException(var6);
            }

            return writer.toString();
        }
    }

    public static Document load(InputStream inputStream) throws IOException, SAXException, ParserConfigurationException {
        Document var2;
        try {
            DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
            newInstance.setNamespaceAware(true);
            var2 = newInstance.newDocumentBuilder().parse(inputStream);
        } finally {
            inputStream.close();
        }

        return var2;
    }

    public static Document load(InputSource inputSource) throws IOException, SAXException, ParserConfigurationException {
        Document var2;
        try {
            DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
            newInstance.setNamespaceAware(true);
            var2 = newInstance.newDocumentBuilder().parse(inputSource);
        } finally {
            if (inputSource.getByteStream() != null) {
                inputSource.getByteStream().close();
            }

        }

        return var2;
    }

    public static Document load(String value) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        newInstance.setNamespaceAware(true);
        return newInstance.newDocumentBuilder().parse(new InputSource(new StringReader(value)));
    }

    public static String getTextValue(Node element) {
        NodeList childs = element.getChildNodes();

        for(int k = 0; k < childs.getLength(); ++k) {
            Node n = childs.item(k);
            if (n.getNodeType() == 3 || n.getNodeType() == 4) {
                return n.getNodeValue();
            }
        }

        return null;
    }

    public static String getTextValue(Node value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            String strValue = getTextValue(value);
            return StringUtils.isEmpty(strValue) ? defaultValue : strValue;
        }
    }

    public static Iterator<Node> iterator(final NodeList list) {
        return new Iterator<Node>() {
            private int i = 0;
            private final int len = list.getLength();

            public boolean hasNext() {
                return this.i < this.len;
            }

            public Node next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    Node result = list.item(this.i);
                    ++this.i;
                    return result;
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static Element getFirstChildElement(Element elem) {
        for(Node n = elem.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == 1) {
                return (Element)n;
            }
        }

        return null;
    }

    public static Element getNextChildElement(Element parentElem, Element currentElem) {
        boolean currElemFound = false;

        for(Node n = parentElem.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == 1) {
                if (currElemFound) {
                    return (Element)n;
                }

                currElemFound = currentElem == n;
            }
        }

        return null;
    }

    public static Node findNode(Node node, String nodeName) {
        if (node == null) {
            return null;
        } else {
            String regexp = "^(.+:)?" + nodeName + "$";
            if (Pattern.compile(regexp).matcher(node.getNodeName()).matches()) {
                return node;
            } else {
                NodeList nodeList = node.getChildNodes();
                int i = 0;

                for(int cnt = nodeList.getLength(); i < cnt; ++i) {
                    Node child = findNode(nodeList.item(i), nodeName);
                    if (child != null) {
                        return child;
                    }
                }

                return null;
            }
        }
    }

    public static Document createDocument() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException var3) {
            throw new FaultException(var3);
        }

        return docBuilder.newDocument();
    }

    public static Node addTextNode(Document document, Node root, String name, String value) {
        Element tag = document.createElement(name);
        tag.appendChild(document.createTextNode(value));
        root.appendChild(tag);
        return tag;
    }

    public static Node addDateTimeNode(Document document, Node root, String name, Date value) {
        Element tag = document.createElement(name);
        tag.appendChild(document.createTextNode((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S")).format(value)));
        root.appendChild(tag);
        return tag;
    }

    public static Node addNode(Document document, Node root, String name) {
        Element tag = document.createElement(name);
        root.appendChild(tag);
        return tag;
    }

    public static String findTextValue(Node context, String nodeName) {
        return findTextValue(context, nodeName, (String)null);
    }

    public static String findTextValue(Node context, String nodeName, String defaultValue) {
        return getTextValue(findNode(context, nodeName), defaultValue);
    }

    public static String getAttributeValue(Node e, String attributeName) {
        NamedNodeMap map = e.getAttributes();
        Node n = map.getNamedItem(attributeName);
        return n != null ? n.getNodeValue() : null;
    }

    public static String escapeAmpersands(String xml) {
        if (xml == null) {
            xml = "";
        }

        return XML_ESCAPE_RE.matcher(xml).replaceAll("&amp;");
    }

    public static <T> T unmarshal(String element, Class<T> type) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(type);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(element);
            JAXBElement<T> result = unmarshaller.unmarshal(new StreamSource(reader), type);
            return result.getValue();
        } catch (JAXBException var6) {
            throw new FaultException("Couldn't unmarshal data to " + type.getName() + ". data: " + element, var6);
        }
    }

    public static Node marshal(JAXBElement object) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getValue().getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            marshaller.marshal(object, doc);
            return doc.getDocumentElement();
        } catch (JAXBException var6) {
            throw new FaultException("Couldn't marshal object to " + object.getValue(), var6);
        } catch (ParserConfigurationException var7) {
            throw new FaultException("Couldn't marshal object to " + object.getValue(), var7);
        }
    }

    public static String removeAllHtmlTags(String s) {
        return Jsoup.parse(s).text();
    }

    public static String removeAllHtmlTagsV2(String text) {
        return JsoupUtil.removeAllHtmlTagsV2(text);
    }

    public static String parseAccordingWhitelist(String htmlString, Map<String, List<String>> allowedElementsAndAttributes) {
        Whitelist whitelist;
        if (allowedElementsAndAttributes != null) {
            whitelist = Whitelist.none();
            Iterator var3 = allowedElementsAndAttributes.entrySet().iterator();

            while(true) {
                Entry entry;
                do {
                    if (!var3.hasNext()) {
                        return Jsoup.clean(htmlString, "", whitelist, (new OutputSettings()).prettyPrint(false));
                    }

                    entry = (Entry)var3.next();
                    whitelist.addTags(new String[]{(String)entry.getKey()});
                } while(entry.getValue() == null);

                Iterator var5 = ((List)entry.getValue()).iterator();

                while(var5.hasNext()) {
                    String attribute = (String)var5.next();
                    whitelist.addAttributes((String)entry.getKey(), new String[]{attribute});
                }
            }
        } else {
            whitelist = Whitelist.basic();
            return Jsoup.clean(htmlString, "", whitelist, (new OutputSettings()).prettyPrint(false));
        }
    }
}




