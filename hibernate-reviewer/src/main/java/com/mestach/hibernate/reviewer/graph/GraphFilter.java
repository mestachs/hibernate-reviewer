package com.mestach.hibernate.reviewer.graph;

import org.hibernate.mapping.PersistentClass;

public interface GraphFilter {
	boolean isDisplayable(PersistentClass mapping);
	public String getKey() ;
}
