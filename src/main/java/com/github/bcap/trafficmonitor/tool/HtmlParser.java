package com.github.bcap.trafficmonitor.tool;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HtmlParser {

	public Document parseHtml(final URL url) throws URISyntaxException, SAXException, IOException {
		final UserAgentContext uaContext = new ExternalContentControlledUserAgent(false, false);
		final DocumentBuilderImpl builder = new DocumentBuilderImpl(uaContext);
		final InputSourceImpl inputSource = new InputSourceImpl(url.toURI().toString());
		return builder.parse(inputSource);
	}

	public String toString(final Document doc) {
		final StringBuilder builder = new StringBuilder();
		final NodeList childNodes = doc.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			this.appendNodeString(childNodes.item(i), builder, 0);
		}
		return builder.toString();
	}

	public String toString(final Node node) {
		final StringBuilder builder = new StringBuilder();
		this.appendNodeString(node, builder, 0);
		return builder.toString();
	}

	protected void appendNodeString(final Node node, final StringBuilder builder, final int ident) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			this.appendIdent(builder, ident).append(node.getTextContent()).append("\n");
		} else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
			this.appendAttribute((Attr) node, builder);
		} else {
			this.appendIdent(builder, ident).append("<");
			builder.append(node.getNodeName());
			this.appendAttributes(node, builder);
			builder.append(">\n");
			final NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				this.appendNodeString(childNodes.item(i), builder, ident + 1);
			}
			this.appendIdent(builder, ident).append("</").append(node.getNodeName()).append(">\n");
		}
	}

	protected void appendAttributes(final Node node, final StringBuilder builder) {
		final NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				this.appendAttribute((Attr) attributes.item(i), builder);
			}
		}
	}

	protected void appendAttribute(final Attr attribute, final StringBuilder builder) {
		builder.append(" ");
		builder.append(attribute.getName());
		builder.append("=\"");
		builder.append(attribute.getValue());
		builder.append("\"");
	}

	protected StringBuilder appendIdent(final StringBuilder builder, final int ident) {
		for (int i = 0; i < ident; i++) {
			builder.append("   ");
		}
		return builder;
	}
}

class ExternalContentControlledUserAgent extends SimpleUserAgentContext {

	private boolean cssEnabled;
	private boolean scriptEnabled;

	public ExternalContentControlledUserAgent() {
	}

	public ExternalContentControlledUserAgent(final boolean cssEnabled, final boolean scriptEnabled) {
		this.cssEnabled = cssEnabled;
		this.scriptEnabled = scriptEnabled;
	}

	public boolean isCssEnabled() {
		return this.cssEnabled;
	}

	public boolean isScriptEnabled() {
		return this.scriptEnabled;
	}

	public void setCssEnabled(final boolean cssEnabled) {
		this.cssEnabled = cssEnabled;
	}

	public void setScriptEnabled(final boolean scriptEnabled) {
		this.scriptEnabled = scriptEnabled;
	}

	public boolean isExternalCSSEnabled() {
		return this.cssEnabled;
	}

	public boolean isScriptingEnabled() {
		return this.scriptEnabled;
	}
}
