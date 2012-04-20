package com.mestach.hibernate.reviewer.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.OneToOne;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Value;
import org.hibernate.type.ManyToOneType;
import org.kohsuke.graphviz.Attribute;
import org.kohsuke.graphviz.Edge;
import org.kohsuke.graphviz.Graph;
import org.kohsuke.graphviz.Node;
import org.kohsuke.graphviz.RankDir;

public class PersistenceGraphBuilder {

	private final Map<PersistentClass, Node> classes = new HashMap<PersistentClass, Node>();
	private final Set<PersistentClass> nonorphans = new HashSet<PersistentClass>();
	private final Set<PersistentClass> orphans = new HashSet<PersistentClass>();

	private List<OmittedRelationShip> omittedRelationShips = new ArrayList<OmittedRelationShip>();

	private final Configuration cfg;
	private final Graph graph = new Graph();
	private final GraphFilter graphFilter;

	private AttributeFiller filler = new DefaultAttributeFiller();

	public PersistenceGraphBuilder(Configuration cfg, GraphFilter filter, DefaultAttributeFiller filler) {
		super();
		this.cfg = cfg;
		this.graphFilter = filter;
		this.filler = filler;
		graph.attr(Attribute.RANKDIR, RankDir.TB);

	}

	public Node addNode(PersistentClass mapping) {
		if (!graphFilter.isDisplayable(mapping)) {
			return null;
		}

		return getClassNode(mapping);
	}

	private Node getClassNode(PersistentClass mapping) {
		// || !graphFilter.isDisplayable(mapping)
		if (mapping == null) {
			return null;
		}
		Node node = classes.get(mapping);
		if (node == null) {
			node = new Node().id(StringUtils.replace(mapping.getEntityName(), ".", "_")).attr("label", getLabel(mapping.getEntityName()));
			filler.fillAttributes(node, mapping);
			classes.put(mapping, node);
		}
		return node;
	}

	private String getLabel(String entityName) {
		if (entityName.indexOf(".") != -1) {
			return StringUtils.substringAfterLast(entityName, ".");
		}
		return entityName;
	}

	Graph toGraph() {
		for (PersistentClass clas : new HashSet<PersistentClass>(classes.keySet())) {
			if (!nonorphans.contains(clas)) {
				orphans.add(clas);
				classes.remove(clas);
			}
		}
		for (Node node : classes.values()) {
			graph.node(node);
		}
		return graph;
	}

	private Node getClassNode(String referencedClass) {
		return getClassNode(cfg.getClassMapping(referencedClass));
	}

	private String getReferencedClass(Value element) {
		if (element instanceof ManyToOne) {
			ManyToOne manyToOne = (ManyToOne) element;
			return manyToOne.getReferencedEntityName();
		} else if (element instanceof OneToMany) {
			OneToMany oneToMany = (OneToMany) element;
			return oneToMany.getReferencedEntityName();

		} else if (element instanceof OneToOne) {
			OneToOne onetoone = (OneToOne) element;
			return onetoone.getReferencedEntityName();
		}
		return "" + element;
	}

	public void addCollection(Collection colmapping) {
		PersistentClass owner = colmapping.getOwner();
		PersistentClass referenced = cfg.getClassMapping(getReferencedClass(colmapping.getElement()));
		if (graphFilter.isDisplayable(owner) && graphFilter.isDisplayable(referenced)) {
			Node ownerNode = getClassNode(owner);
			Node referencedNode = getClassNode(referenced);
			if (referenced != null && ownerNode != null) {
				graph.edge(new Edge(ownerNode, referencedNode).attr("label", getLabel(colmapping.getRole())).attr("color",
						getColor(colmapping)));
				nonorphans.add(owner);
				nonorphans.add(referenced);
			}
		} else {
			if (graphFilter.isDisplayable(owner) || graphFilter.isDisplayable(referenced)) {
				omittedRelationShips.add(new OmittedRelationShip(owner, referenced, "collection"));
			}
		}
	}

	private String getColor(Collection colmapping) {
		return getColorLazy(colmapping.isLazy() && colmapping.getBatchSize() > 0);
	}

	private String getColor(Property prop) {
		return getColorLazy(prop.isLazy());
	}

	private String getColorLazy(boolean b) {
		if (b) {
			return "blue";
		} else {
			return "red";
		}
	}

	public void addInheritence(PersistentClass mapping, PersistentClass mappingparent) {
		if (!graphFilter.isDisplayable(mapping) && !graphFilter.isDisplayable(mappingparent)) {
			if (graphFilter.isDisplayable(mapping) || graphFilter.isDisplayable(mappingparent)) {
				omittedRelationShips.add(new OmittedRelationShip(mapping, mappingparent, "Inheritence"));
			}
			return;
		}
		Node child = getClassNode(mapping);
		Node parent = getClassNode(mappingparent);
		if (child == null || parent == null) {
			return;
		}
		graph.edge(new Edge(child, parent).attr("arrowhead", "empty"));
		nonorphans.add(mapping);
		nonorphans.add(mappingparent);

	}

	public void addProperty(PersistentClass mapping, Property prop) {
		if (prop.getValue() instanceof Component) {
			return;
		}

		Value element = prop.getValue();
		PersistentClass referencedMapping = cfg.getClassMapping(getReferencedClass(element));
		if (referencedMapping == null || !graphFilter.isDisplayable(referencedMapping)) {
			if (graphFilter.isDisplayable(mapping) || graphFilter.isDisplayable(referencedMapping)) {
				if (mapping != null && referencedMapping != null) {
					omittedRelationShips.add(new OmittedRelationShip(mapping, referencedMapping, ""
							+ element.getType().getClass().getSimpleName() + " " + prop.isLazy()));
				}
			}
			return;
		}
		if (!graphFilter.isDisplayable(mapping)) {
			if (mapping != null && referencedMapping != null) {
				omittedRelationShips.add(new OmittedRelationShip(mapping, referencedMapping, ""
						+ element.getType().getClass().getSimpleName() + " " + prop.isLazy()));
			}
			return;
		}
		Node child = getClassNode(mapping);
		Node parent = getClassNode(getReferencedClass(element));
		if (child == null || parent == null) {
			return;
		}
		graph.edge(new Edge(child, parent).attr("label", prop.getName()).attr("color", getColor(prop)));
		nonorphans.add(mapping);
		nonorphans.add(referencedMapping);

	}

	public List<OmittedRelationShip> getOmittedRelationShips() {
		return omittedRelationShips;
	}

	public void setOmittedRelationShips(List<OmittedRelationShip> omittedRelationShips) {
		this.omittedRelationShips = omittedRelationShips;
	}

	public void addKeyManyToOne(PersistentClass mapping, ManyToOneType type) {
		if (!graphFilter.isDisplayable(mapping) && !graphFilter.isDisplayable(cfg.getClassMapping(type.getAssociatedEntityName()))) {
			return;
		}
		Node child = getClassNode(mapping);
		Node parent = getClassNode(type.getAssociatedEntityName());

		graph.edge(new Edge(child, parent).attr("label", "id"));
		nonorphans.add(mapping);
		nonorphans.add(cfg.getClassMapping(type.getAssociatedEntityName()));
	}

	public Set<PersistentClass> getOrphans() {
		return orphans;
	}

}
