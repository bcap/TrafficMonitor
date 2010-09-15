package com.github.bcap.trafficmonitor.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class RoadWay implements Serializable {

	private static final long serialVersionUID = 6476677794759257405L;

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToOne
	private Road road;

	@OneToMany
	private List<RoadWayMark> roadWayMarks;

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

	public Road getRoad() {
		return this.road;
	}

	public void setRoad(final Road road) {
		this.road = road;
	}

	public List<RoadWayMark> getRoadWayMarks() {
		return this.roadWayMarks;
	}

	public void setRoadWayMarks(final List<RoadWayMark> roadWayMarks) {
		this.roadWayMarks = roadWayMarks;
	}

}
