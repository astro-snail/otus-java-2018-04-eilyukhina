package ru.otus.l131.cache;

import java.util.Properties;

public interface Cache {
	
	void put(Element element);
	
	Element get(Object key);
	
	boolean remove(Object key);
	
	int getHitCount();

    int getMissCount();
    
    int getSize();
    
    Properties getProperties();
	
	void dispose();

}
