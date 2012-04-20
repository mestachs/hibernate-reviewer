package com.mestach.hibernate.reviewer;

import java.util.Comparator;

import com.mestach.hibernate.reviewer.model.ReviewReportViolation;


public class MappingReviewReportComparator implements Comparator<ReviewReportViolation> {

	public int compare(ReviewReportViolation o1, ReviewReportViolation o2) {
		String c1 = o1.getMapping().getClassName();
		String c2 = o1.getMapping().getClassName();
		int cc = c1.compareTo(c2);
		if (cc == 0)
			return cc;
		else
			return o1.getSeverity().compareTo(o2.getSeverity());
	}

}
