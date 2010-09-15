package com.github.bcap.trafficmonitor.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUnitManager {
	private final Map<String, EntityManagerFactory> entityManagerMap;

	public PersistenceUnitManager() {
		this.entityManagerMap = new HashMap<String, EntityManagerFactory>();
	}

	public EntityManager getEntityManager(final String persistenceUnit) {
		EntityManagerFactory factory;
		if (this.entityManagerMap.containsKey(persistenceUnit)) {
			factory = this.entityManagerMap.get(persistenceUnit);
		} else {
			factory = Persistence.createEntityManagerFactory(persistenceUnit);
			this.entityManagerMap.put(persistenceUnit, factory);
		}

		return factory.createEntityManager();
	}
}