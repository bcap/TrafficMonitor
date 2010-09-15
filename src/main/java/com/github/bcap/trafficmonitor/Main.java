package com.github.bcap.trafficmonitor;

import java.util.List;

import javax.persistence.EntityManager;

import com.github.bcap.trafficmonitor.business.RoadExtractor;
import com.github.bcap.trafficmonitor.business.RoadWayExtractor;
import com.github.bcap.trafficmonitor.entity.Road;
import com.github.bcap.trafficmonitor.entity.RoadWay;
import com.github.bcap.trafficmonitor.entity.RoadWayMark;
import com.github.bcap.trafficmonitor.entity.RoadWayTraffic;
import com.github.bcap.trafficmonitor.exception.ExtractorException;
import com.github.bcap.trafficmonitor.jpa.PersistenceUnitManager;

public class Main implements Runnable {

	private PersistenceUnitManager persistenceUnitManager;
	private EntityManager entityManager;

	public void run() {
		this.persistenceUnitManager = new PersistenceUnitManager();
		this.entityManager = this.persistenceUnitManager.getEntityManager("traffic");

		try {

			final RoadExtractor roadExtractor = new RoadExtractor();
			final RoadWayExtractor roadWayExtractor = new RoadWayExtractor();

			System.out.println("Extracting roads");

			final List<Road> roads = roadExtractor.extractRoads();

			for (final Road road : roads) {
				System.out.print("Extracting info for road " + road.getName() + " ... ");
				roadWayExtractor.extractRoadWays(road);
				System.out.println(road);

				this.entityManager.getTransaction().begin();
				this.persistRoad(road);
				this.entityManager.getTransaction().commit();
			}
		} catch (final ExtractorException e) {
			e.printStackTrace();
		} finally {
			this.entityManager.close();
		}
	}

	protected void persistRoad(final Road road) {
		System.out.println("Persisting road " + road.getName());
		this.persistRoadWay(road.getOutgoingRoadWay());
		this.persistRoadWay(road.getIncomingRoadWay());
		this.entityManager.persist(road);
	}

	protected void persistRoadWay(final RoadWay roadWay) {
		if (roadWay != null) {
			System.out.println("Persisting roadWay " + roadWay.getName());
			for (final RoadWayMark roadWayMark : roadWay.getRoadWayMarks()) {
				this.persistRoadWayMark(roadWayMark);
			}
			this.entityManager.persist(roadWay);
		}
	}

	protected void persistRoadWayMark(final RoadWayMark roadWayMark) {
		System.out.println("Persisting roadWayMark " + roadWayMark.getName());
		for (final RoadWayTraffic roadWayTraffic : roadWayMark.getMeasuredTraffics()) {
			this.entityManager.persist(roadWayTraffic);
		}
		this.entityManager.persist(roadWayMark);
	}

	public static void main(final String[] args) throws ExtractorException {
		final Main main = new Main();
		main.run();
	}
}
