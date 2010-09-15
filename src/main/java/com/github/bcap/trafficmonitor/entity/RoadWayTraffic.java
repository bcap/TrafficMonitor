package com.github.bcap.trafficmonitor.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class RoadWayTraffic implements Serializable {

	private static final long serialVersionUID = -7780183151274529802L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private RoadWayMark roadWayMark;

	@Enumerated(EnumType.ORDINAL)
	private TrafficCondition trafficCondition;

	@Temporal(TemporalType.TIMESTAMP)
	private Date measuredTime;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public RoadWayMark getRoadWayMark() {
		return this.roadWayMark;
	}

	public void setRoadWayMark(final RoadWayMark roadWayMark) {
		this.roadWayMark = roadWayMark;
	}

	public TrafficCondition getTrafficCondition() {
		return this.trafficCondition;
	}

	public void setTrafficCondition(final TrafficCondition trafficCondition) {
		this.trafficCondition = trafficCondition;
	}

	public Date getMeasuredTime() {
		return this.measuredTime;
	}

	public void setMeasuredTime(final Date measuredTime) {
		this.measuredTime = measuredTime;
	}

}