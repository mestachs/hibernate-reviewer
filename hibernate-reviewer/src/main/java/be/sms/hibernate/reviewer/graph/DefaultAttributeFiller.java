package be.sms.hibernate.reviewer.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.mapping.PersistentClass;
import org.kohsuke.graphviz.Attribute;
import org.kohsuke.graphviz.Node;
import org.kohsuke.graphviz.Shape;

public class DefaultAttributeFiller implements AttributeFiller {
	private Map<String, String> packageColor = new HashMap<String, String>();

	private List<String> packages = new ArrayList<String>();

	public Map<String, String> getPackageColor() {
		return packageColor;
	}

	public void setPackageColor(Map<String, String> packageColor) {
		this.packageColor = packageColor;
		packages = new ArrayList<String>();
		packages.addAll(packageColor.keySet());
		Collections.sort(packages, new Comparator<String>() {
			public int compare(String o1, String o2) {
				int c1 = StringUtils.split(o1, '.').length;
				int c2 = StringUtils.split(o2, '.').length;
				if (c1 == c2) {
					return o1.compareTo(o2);
				}
				return c2 - c1;
			}
		});
	}

	public void fillAttributes(Node node, PersistentClass clazz) {

		for (String packageStart : packages) {
			node.attr("URL", "../current.html?filter=" + clazz.getClassName());
			node.attr("tooltip",clazz.getClassName());
			if (StringUtils.isNotEmpty(clazz.getCacheConcurrencyStrategy())) {
				node.attr(Attribute.SHAPE, Shape.BOX);
			} else if (clazz.getBatchSize() != -1) {
				node.attr(Attribute.SHAPE, Shape.OCTAGON);
			}

			if (clazz.getEntityName().startsWith(packageStart)) {
				node.attr("style", "filled");
				node.attr("color", packageColor.get(packageStart));
			}
		}

	}
}
