package com.mestach.hibernate.reviewer.navigation;

import org.apache.commons.lang.StringUtils;

public class NavigationUtil {

	private static final char PACKAGE_SEP = '.';

	public String generateLinks(String className, String urlPattern) {
		String[] splitByPackage = StringUtils.split(className, PACKAGE_SEP);
		StringBuilder fullPackage = new StringBuilder();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < splitByPackage.length; i++) {
			String curpackage = splitByPackage[i];
			if (i > 0) {
				fullPackage.append(PACKAGE_SEP);
			}
			fullPackage.append(curpackage);
			result.append("&nbsp;<a href=\"");
			result.append(StringUtils.replace(urlPattern, "${package}", fullPackage.toString()));
			result.append("\">");
			result.append(curpackage);
			if (i + 1 < splitByPackage.length) {
				result.append(PACKAGE_SEP);
			}
			result.append("</a>");
		}
		return result.toString();
	}

	public String[] packages(String className) {
		String[] splitByPackage = StringUtils.split(className, PACKAGE_SEP);
		StringBuilder fullPackage = new StringBuilder();
		String[] packages = new String[splitByPackage.length - 1];
		for (int i = 0; i < splitByPackage.length-1; i++) {
			String curpackage = splitByPackage[i];
			fullPackage.append(curpackage);
			packages[i] = fullPackage.toString();
			if (i + 1 < splitByPackage.length) {
				fullPackage.append(PACKAGE_SEP);
			}
		}
		return packages;
	}
}
