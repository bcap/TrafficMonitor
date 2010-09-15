package com.github.bcap.trafficmonitor.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class RoadWayMark implements Serializable {

	private static final long serialVersionUID = -4628552188321632136L;

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@ManyToOne
	private RoadWay roadWay;

	@OneToMany
	private List<RoadWayTraffic> measuredTraffics;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public RoadWay getRoadWay() {
		return this.roadWay;
	}

	public void setRoadWay(final RoadWay roadWay) {
		this.roadWay = roadWay;
	}

	public List<RoadWayTraffic> getMeasuredTraffics() {
		return this.measuredTraffics;
	}

	public void setMeasuredTraffics(final List<RoadWayTraffic> measuredTraffics) {
		this.measuredTraffics = measuredTraffics;
	}
}
