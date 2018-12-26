package ru.otus.l131.cache;

public class CacheFactory {

	public static Cache getCache(CacheConfiguration configuration) {

		int maxCapacity = configuration.getMaxCapacity();
		long lifeTime = configuration.getLifeTime();
		long idleTime = configuration.getIdleTime();
		boolean isEternal = configuration.isEternal();

		return new CacheStore(maxCapacity, lifeTime, idleTime, isEternal);
	}

}
