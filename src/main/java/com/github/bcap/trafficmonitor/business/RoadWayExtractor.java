package com.github.bcap.trafficmonitor.business;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.github.bcap.trafficmonitor.entity.Road;
import com.github.bcap.trafficmonitor.entity.RoadWay;
import com.github.bcap.trafficmonitor.entity.RoadWayMark;
import com.github.bcap.trafficmonitor.entity.RoadWayTraffic;
import com.github.bcap.trafficmonitor.entity.TrafficCondition;
import com.github.bcap.trafficmonitor.exception.ExtractorException;

public class RoadWayExtractor extends Extractor {

	private static final String ROADWAY_NAME_XPATH = "/html/body//div[@id='principal']/h3[${id}]/text()";

	private static final String ROADMARK_NAME_XPATH = "/html/body//div[${id}]/table[@class='corredores']//td[1]/a/text()";
	private static final String ROADMARK_CONDITION_XPATH = "/html/body//div[${id}]/table[@class='corredores']//td[2]/span/text()";

	public void extractRoadWays(final Road road) throws ExtractorException {
		try {
			final Document htmlDoc = this.htmlParser.parseHtml(new URL(road.getLink()));

			final RoadWay outgoingRoadWay = this.extractFullRoadWay(htmlDoc, 1, 3);
			final RoadWay incomingRoadWay = this.extractFullRoadWay(htmlDoc, 2, 5);

			if (outgoingRoadWay != null) {
				outgoingRoadWay.setRoad(road);
			}

			if (incomingRoadWay != null) {
				incomingRoadWay.setRoad(road);
			}

			road.setOutgoingRoadWay(outgoingRoadWay);
			road.setIncomingRoadWay(incomingRoadWay);

		} catch (final MalformedURLException e) {
			throw new ExtractorException("configured road url is wrong: " + road.getLink(), e);
		} catch (final URISyntaxException e) {
			throw new ExtractorException("configured road url is wrong: " + road.getLink(), e);
		} catch (final SAXException e) {
			throw new ExtractorException("Could not parse html on this url: " + road.getLink(), e);
		} catch (final IOException e) {
			throw new ExtractorException("IOException while trying to parse the html on this url: " + road.getLink(), e);
		}
	}

	private RoadWay extractFullRoadWay(final Document htmlDoc, final Integer roadWayNameId, final Integer roadWayMarksId) throws ExtractorException {
		final RoadWay roadWay = this.extractRoadWay(htmlDoc, roadWayNameId);
		if (roadWay != null) {
			roadWay.setRoadWayMarks(this.extractRoadWayMarks(htmlDoc, roadWay, roadWayMarksId));
		}
		return roadWay;
	}

	protected RoadWay extractRoadWay(final Document htmlDoc, final Integer roadWayNameId) throws ExtractorException {
		try {
			final Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("id", roadWayNameId);

			RoadWay roadWay = null;

			final NodeList nodes = this.selectNodes(htmlDoc, ROADWAY_NAME_XPATH, variables);
			if (nodes.getLength() > 0) {
				final String roadWayName = ((Text) nodes.item(0)).getWholeText().trim();
				roadWay = new RoadWay();
				roadWay.setName(roadWayName);
			}
			return roadWay;
		} catch (final XPathExpressionException e) {
			throw new ExtractorException("Road way xpath expression " + ROADWAY_NAME_XPATH + " is wrong!", e);
		}
	}

	protected List<RoadWayMark> extractRoadWayMarks(final Document htmlDoc, final RoadWay roadWay, final Integer roadWayMarksId) throws ExtractorException {
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("id", roadWayMarksId);

		NodeList roadWayMarkNames;
		NodeList roadWayMarkConditions;

		try {
			roadWayMarkNames = this.selectNodes(htmlDoc, ROADMARK_NAME_XPATH, variables);
		} catch (final XPathExpressionException e) {
			throw new ExtractorException("RoadWayMark xpath expression " + ROADWAY_NAME_XPATH + " is wrong!", e);
		}

		try {
			roadWayMarkConditions = this.selectNodes(htmlDoc, ROADMARK_CONDITION_XPATH, variables);
		} catch (final XPathExpressionException e) {
			throw new ExtractorException("RoadWayMark xpath expression " + ROADMARK_CONDITION_XPATH + " is wrong!", e);
		}

		final List<RoadWayMark> result = new ArrayList<RoadWayMark>();
		for (int i = 0; i < roadWayMarkNames.getLength(); i++) {
			final String roadWayName = ((Text) roadWayMarkNames.item(i)).getWholeText().trim();
			final String roadWayCondition = ((Text) roadWayMarkConditions.item(i)).getWholeText().trim();

			final RoadWayMark mark = new RoadWayMark();
			mark.setName(roadWayName);
			mark.setRoadWay(roadWay);

			final RoadWayTraffic traffic = new RoadWayTraffic();
			traffic.setTrafficCondition(TrafficCondition.getBySiteString(roadWayCondition));
			traffic.setMeasuredTime(Calendar.getInstance().getTime());
			traffic.setRoadWayMark(mark);

			final List<RoadWayTraffic> measuredTraffics = new ArrayList<RoadWayTraffic>();
			measuredTraffics.add(traffic);
			mark.setMeasuredTraffics(measuredTraffics);

			result.add(mark);
		}
		return result;
	}
}