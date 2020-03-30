package cn.bytecloud.smartCommunity.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XmlUtil {

    public static Map<String, String> getMapFromXml(String xmlStr) throws DocumentException {
        Map<String, String> m = new HashMap<String, String>();
        // 将字符串转化为XML文档对象
        Document document = DocumentHelper.parseText(xmlStr);
        // 获得文档的根节点
        Element root = document.getRootElement();
        // 遍历根节点下所有子节点
        Iterator<?> iter = root.elementIterator();
        while (iter.hasNext()) {
            Element e = (Element) iter.next();
            String k = e.getName();
            String v = (String) e.getData();
            m.put(k, v);
        }
        return m;
    }
}
