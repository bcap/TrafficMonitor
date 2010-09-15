package com.github.bcap.trafficmonitor.business;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.github.bcap.trafficmonitor.entity.Road;
import com.github.bcap.trafficmonitor.entity.TrafficCondition;
import com.github.bcap.trafficmonitor.exception.ExtractorException;

public class RoadExtractor extends Extractor {

	private static final String ROADS_DOMAIN = "http://maplink.uol.com.br";
	private static final String ROADS_URL_STRING = ROADS_DOMAIN + "/v2/corredores/Sao-Paulo-SP.htm";

	private static final String ROAD_NAME_XPATH = "/html/body//table[@class='corredores']//td[2]/a/text()";
	private static final String CONDITION_XPATH = "/html/body//table[@class='corredores']//td[4]/span[2]/text()";
	private static final String LINK_XPATH = "/html/body//table[@class='corredores']//td[7]/a/@href";

	public List<Road> extractRoads() throws ExtractorException {
		try {
			final Document htmlDoc = this.htmlParser.parseHtml(new URL(RoadExtractor.ROADS_URL_STRING));

			final List<String> roadNames = this.extractRoadNames(htmlDoc);
			final List<String> conditions = this.extractConditions(htmlDoc);
			final List<String> links = this.extractLinks(htmlDoc);

			if ((roadNames.size() != conditions.size()) || (conditions.size() != links.size())) {
				throw new ExtractorException("got different amounts os road names (" + roadNames.size() + "), conditions (" + conditions.size() + ") or links (" + links.size() + "), check the xpaths");
			} else {
				final List<Road> result = new ArrayList<Road>();
				for (Iterator<String> roadsIt = roadNames.iterator(), conditionsIt = conditions.iterator(), linksIt = links.iterator(); roadsIt.hasNext();) {
					final Road road = new Road();
					road.setName(roadsIt.next());
					road.setGeneralCondition(TrafficCondition.getBySiteString(conditionsIt.next()));
					road.setLink(linksIt.next());
					result.add(road);
				}
				return result;
			}
		} catch (final MalformedURLException e) {
			throw new ExtractorException("configured road url is wrong: " + RoadExtractor.ROADS_URL_STRING, e);
		} catch (final URISyntaxException e) {
			throw new ExtractorException("configured road url is wrong: " + RoadExtractor.ROADS_URL_STRING, e);
		} catch (final SAXException e) {
			throw new ExtractorException("Could not parse html on this url: " + RoadExtractor.ROADS_URL_STRING, e);
		} catch (final IOException e) {
			throw new ExtractorException("IOException while trying to parse the html on this url: " + RoadExtractor.ROADS_URL_STRING, e);
		}
	}

	protected List<String> extractRoadNames(final Document htmlDoc) throws ExtractorException {
		try {
			final List<String> roads = new ArrayList<String>();
			final NodeList roadNodes = this.selectNodes(htmlDoc, ROAD_NAME_XPATH);
			for (int i = 0; i < roadNodes.getLength(); i++) {
				roads.add(((Text) roadNodes.item(i)).getWholeText().trim());
			}
			return roads;
		} catch (final XPathExpressionException e) {
			throw new ExtractorException("Road names xpath expression " + ROADS_URL_STRING + " is wrong!", e);
		}
	}

	protected List<String> extractConditions(final Document htmlDoc) throws ExtractorException {
		try {
			final List<String> conditions = new ArrayList<String>();
			final NodeList conditionNodes = this.selectNodes(htmlDoc, CONDITION_XPATH);
			for (int i = 0; i < conditionNodes.getLength(); i++) {
				conditions.add(((Text) conditionNodes.item(i)).getWholeText().trim());
			}
			return conditions;
		} catch (final XPathExpressionException e) {
			throw new ExtractorException("Road names xpath expression " + ROADS_URL_STRING + " is wrong!", e);
		}
	}

	protected List<String> extractLinks(final Document htmlDoc) throws ExtractorException {
		try {
			final List<String> links = new ArrayList<String>();
			final NodeList linkNodes = this.selectNodes(htmlDoc, LINK_XPATH);
			for (int i = 0; i < linkNodes.getLength(); i++) {
				links.add(ROADS_DOMAIN + ((Attr) linkNodes.item(i)).getValue().trim());
			}
			return links;
		} catch (final XPathExpressionException e) {
			throw new ExtractorException("Road names xpath expression " + ROADS_URL_STRING + " is wrong!", e);
		}
	}

	public static void main(final String[] args) throws ExtractorException {
		final RoadExtractor extractor = new RoadExtractor();
		final List<Road> roads = extractor.extractRoads();
		for (final Road road : roads) {
			System.out.println(road);
		}
	}
}
