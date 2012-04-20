package com.mestach.hibernate.reviewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.mestach.hibernate.reviewer.model.ReviewReportViolation;
import com.mestach.hibernate.reviewer.model.ViolationSeverity;
import com.mestach.hibernate.reviewer.navigation.NavigationUtil;


public class MappingReviewReport {

	private List<ReviewReportViolation> violations = new ArrayList<ReviewReportViolation>();;

	private String filter;

	public void addViolation(ReviewReportViolation violation) {
		violations.add(violation);
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder report = new StringBuilder("");
		for (ReviewReportViolation item : violations) {
			report.append(item);
			report.append("\n");
		}
		return report.toString();
	}

	public List<ReviewReportViolation> getItems() {

		List<ReviewReportViolation> currentItems = new ArrayList<ReviewReportViolation>(this.violations);
		Collections.sort(currentItems, new MappingReviewReportComparator());
		if (StringUtils.isNotEmpty(filter)) {
			CollectionUtils.filter(currentItems, new Predicate() {
				public boolean evaluate(Object arg0) {
					return StringUtils.contains(((ReviewReportViolation) arg0).asString().toLowerCase(), filter.toLowerCase());
				}
			});
		}
		return currentItems;
	}

	/**
	 * @param filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;

	}

	private static final char PACKAGE_SEP = '.';

	public String getStats() {
		Map<String, Map<ViolationSeverity, List<ReviewReportViolation>>> statsPerPackagePerSeverity = new LinkedHashMap<String, Map<ViolationSeverity, List<ReviewReportViolation>>>();
		for (ReviewReportViolation violation : getItems()) {
			String packag = violation.getMapping().getClassName();
			String[] packages = new NavigationUtil().packages(packag);
			StringBuilder currentFullPackage = new StringBuilder();
			for (int i = 0; i < packages.length - 1; i++) {
				String currentPackage = packages[i];
				getListFor(statsPerPackagePerSeverity, packag, violation.getSeverity()).add(violation);
			}
		}
		return statsPerPackagePerSeverity.toString();
	}

	private List<ReviewReportViolation> getListFor(
			Map<String, Map<ViolationSeverity, List<ReviewReportViolation>>> statsPerPackagePerSeverity, String packag,
			ViolationSeverity sev) {
		Map<ViolationSeverity, List<ReviewReportViolation>> stats = statsPerPackagePerSeverity.get(packag);
		if (stats == null) {
			stats = new LinkedHashMap<ViolationSeverity, List<ReviewReportViolation>>();
			statsPerPackagePerSeverity.put(packag, stats);
		}

		List<ReviewReportViolation> list = stats.get(sev);
		if (list == null) {
			list = new ArrayList<ReviewReportViolation>();
			stats.put(sev, list);
		}
		return list;

	}
}
