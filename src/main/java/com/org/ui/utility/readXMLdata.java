package com.org.ui.utility;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public class readXMLdata {

    private static final Logger logger = LoggerFactory.getLogger(readXMLdata.class);

    public static String getTestData(String SectionName, String NodeName) {
        String env = System.getProperty("ExecutionEnv");
        System.out.println("Test Environment is :: "+env);
        SectionName = "Project/" + env + "/" + SectionName + "/" + NodeName;
        System.out.println("SectionName is :: "+SectionName);
        File fXmlFile = new File("TestData.xml");
        String foundData = null;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        NodeList nodeList;
        Document doc;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
            nodeList = (NodeList) xPath.compile(SectionName).evaluate(doc, XPathConstants.NODESET);
            System.out.println("nodeList is :: "+nodeList.toString());
        } catch (Exception e) {
            Assert.fail("Exception reading TestData.xml [" + e.getMessage() + "]");
            return null;
        }
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            org.w3c.dom.Node nNode = nodeList.item(temp);
            if (nNode.getNodeName().equals(NodeName)) {
                foundData = nNode.getTextContent().trim();
            }
        }
        System.out.println("foundData is :: "+foundData);
        Assert.assertNotNull("Failed to find data in TestData.xml for [" + SectionName + "]", foundData);
        return foundData;
    }

    public static  void main(String[] args) {
        getTestData("DB/MySql", "MySqlDriver");
    }

}
