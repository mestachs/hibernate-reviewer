package com.mestach.hibernate.reviewer.graph;

import org.hibernate.mapping.PersistentClass;
import org.kohsuke.graphviz.Node;

public interface AttributeFiller {

	void fillAttributes(Node node, PersistentClass class1);
}
