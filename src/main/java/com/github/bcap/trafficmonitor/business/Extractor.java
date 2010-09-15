package com.github.bcap.trafficmonitor.business;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.bcap.trafficmonitor.tool.HtmlParser;

public abstract class Extractor {

	private final XPath xPath = XPathFactory.newInstance().newXPath();

	protected final HtmlParser htmlParser = new HtmlParser();

	protected NodeList selectNodes(final Node node, final String xPathString) throws XPathExpressionException {
		return this.selectNodes(node, xPathString, null);
	}

	protected NodeList selectNodes(final Node node, String xPathString, final Map<String, Object> variables) throws XPathExpressionException {
		if (variables != null) {
			for (final Entry<String, Object> entry : variables.entrySet()) {
				xPathString = xPathString.replaceAll("\\$\\{" + entry.getKey() + "\\}", entry.getValue().toString());
			}
		}
		return (NodeList) this.xPath.evaluate(xPathString, node, XPathConstants.NODESET);
	}
}
