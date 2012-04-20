package com.mestach.hibernate.reviewer.graph;

import org.hibernate.mapping.PersistentClass;

public class DefaultGraphFilter implements GraphFilter {
	private String packageStarts;

	public DefaultGraphFilter(String packageStarts) {
		super();
		if (packageStarts.startsWith("^"))
			this.packageStarts = packageStarts;
		else {
			this.packageStarts = "^" + packageStarts + ".*";

		}
	}

	public boolean isDisplayable(PersistentClass mapping) {
		if (mapping == null)
			return false;
	
		boolean displayable = mapping.getClassName().matches(packageStarts);

		return displayable;
	}

	public String getKey() {
		String key = packageStarts.replace("*", ")");
		key = key.replace("|", ".");
		key = key.replace("^", "");
		key = key.replace("(", "");
		key = key.replace(")", "");
		return key;
	}
}
