package ru.gecec.learnphrasebot.model.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class SqlResolver {

    @Value("#{'${sqlresolver.files}'.split(';')}")
    private List<String> files;

    private Properties properties;
    private Map<String, String> sqls = new HashMap();

    public SqlResolver() {
    }

    @PostConstruct
    public void init() {
        Iterator var1 = this.files.iterator();

        while(var1.hasNext()) {
            String filename = (String)var1.next();

            try {
                Object stream;
                if (filename.startsWith("classpath:")) {
                    stream = this.getClass().getClassLoader().getResourceAsStream(filename.substring("classpath:".length()));
                } else {
                    stream = new FileInputStream(filename);
                }

                this.parseXml((InputStream)stream);
            } catch (Exception var4) {
                throw new IllegalStateException("Couldn't load sqls from " + filename, var4);
            }
        }

    }

    private void parseXml(InputStream stream) throws IOException, SAXException, ParserConfigurationException {
        Document document = XMLUtil.load(stream);
        NodeList list = document.getDocumentElement().getChildNodes();

        for(int i = 0; i < list.getLength() - 1; ++i) {
            Node sqlNode = list.item(i);
            if ("sql".equals(sqlNode.getNodeName())) {
                String id = ((Element)sqlNode).getAttribute("name");
                String value = XMLUtil.getTextValue((Element)sqlNode);
                if (!StringUtils.isEmpty(value)) {
                    this.sqls.put(id, this.processSql(value));
                }
            }
        }
    }

    private String processSql(String sql) {
        String result = sql;
        String key;
        String value;
        if (this.properties != null) {
            for(Enumeration kyes = this.properties.propertyNames(); kyes.hasMoreElements(); result = result.replace("${" + key + "}", value)) {
                key = (String)kyes.nextElement();
                value = this.properties.getProperty(key);
            }
        }

        return result;
    }

    public String getSql(String queryName) {
        if (!this.sqls.containsKey(queryName)) {
            throw new IllegalArgumentException(queryName + " not found");
        } else {
            return (String)this.sqls.get(queryName);
        }
    }

    public Set<String> getSqlNames() {
        return Collections.unmodifiableSet(this.sqls.keySet());
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}


