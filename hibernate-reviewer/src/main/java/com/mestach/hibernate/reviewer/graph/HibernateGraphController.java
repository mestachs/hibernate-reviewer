package com.mestach.hibernate.reviewer.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.type.EmbeddedComponentType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;

import com.mestach.hibernate.reviewer.navigation.NavigationUtil;


public class HibernateGraphController {
	private Configuration configuration;
	private DefaultAttributeFiller attributeFiller;

	private static final String FORMAT = "png";

	private String outputDirectory= "./target";
	private String dotexe = System.getProperty("dotexe","C:\\prog\\Graphviz\\bin\\dot.exe");

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public DefaultAttributeFiller getAttributeFiller() {
		return attributeFiller;
	}

	public void setAttributeFiller(DefaultAttributeFiller attributeFiller) {
		this.attributeFiller = attributeFiller;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Map<String, Object> generate(GraphFilter filter) throws Exception {

		PersistenceGraphBuilder builder = new PersistenceGraphBuilder(
				configuration, filter, attributeFiller);
		putNodesAndEdges(builder);
		boolean showText = false;
		if (showText) {
			builder.toGraph().writeTo(System.out);
		} else {
			String fileName = "generated-graph-" + filter.getKey();
			generate(builder, fileName, FORMAT);
			generate(builder, fileName, "cmapx");

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("fileGraph", fileName + "." + FORMAT);
			model.put(
					"mapData",
					IOUtils.toString(new FileInputStream(outputDirectory
							+ File.separator + fileName + "." + "cmapx")));
			model.put("filter", filter);
			model.put("omittedRelationShips", builder.getOmittedRelationShips());
			model.put("orphans", builder.getOrphans());
			model.put("navigationUtil", new NavigationUtil());
			return model;
		}
		return null;
	}

	private void generate(PersistenceGraphBuilder builder, String fileName,
			String format) throws InterruptedException, IOException {
		File generated = new File(outputDirectory + File.separator + fileName
				+ "." + format);
		builder.toGraph()
				.generateTo(
						Arrays.asList(new String[] { dotexe, "-T" + format }),
						generated);
	}

	private void putNodesAndEdges(PersistenceGraphBuilder builder) {
		for (Iterator<PersistentClass> iter = configuration.getClassMappings(); iter
				.hasNext();) {
			PersistentClass mapping = iter.next();

			builder.addNode(mapping);
		}

		for (Iterator<PersistentClass> iter = configuration.getClassMappings(); iter
				.hasNext();) {
			PersistentClass mapping = iter.next();
			for (Iterator<Property> iterator = mapping.getPropertyIterator(); iterator
					.hasNext();) {
				Property prop = iterator.next();
				builder.addProperty(mapping, prop);
			}
		}
		for (Iterator<PersistentClass> iter = configuration.getClassMappings(); iter
				.hasNext();) {
			PersistentClass mapping = iter.next();

			if (mapping.getIdentifier().getType() instanceof EmbeddedComponentType) {
				EmbeddedComponentType ect = (EmbeddedComponentType) mapping
						.getIdentifier().getType();
				Type[] types = ect.getSubtypes();
				for (int i = 0; i < types.length; i++) {
					Type type = types[i];
					if (type instanceof ManyToOneType) {
						builder.addKeyManyToOne(mapping, (ManyToOneType) type);
					}
				}

			}
		}

		for (Iterator<PersistentClass> iter = configuration.getClassMappings(); iter
				.hasNext();) {
			PersistentClass mapping = iter.next();
			PersistentClass mappingparent = mapping.getSuperclass();
			if (mapping == mappingparent) {
				continue;
			}
			builder.addInheritence(mapping, mappingparent);
		}

		for (Iterator<Collection> iter = configuration.getCollectionMappings(); iter
				.hasNext();) {
			Collection colmapping = iter.next();
			builder.addCollection(colmapping);
		}
	}

}
