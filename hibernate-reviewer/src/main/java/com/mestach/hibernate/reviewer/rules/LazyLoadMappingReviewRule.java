package com.mestach.hibernate.reviewer.rules;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;

import com.mestach.hibernate.reviewer.HibernateMappingReviewRule;
import com.mestach.hibernate.reviewer.MappingReviewReport;
import com.mestach.hibernate.reviewer.model.ReviewReportViolation;
import com.mestach.hibernate.reviewer.model.ViolationSeverity;


public class LazyLoadMappingReviewRule implements HibernateMappingReviewRule {


	public void review(Configuration configuration, PersistentClass mapping, MappingReviewReport report) {

	}

	public void review(Configuration configuration, Collection colmapping, MappingReviewReport report) {
		ViolationSeverity severity = ViolationSeverity.BLOCKER;
		if (StringUtils.isEmpty(colmapping.getCacheConcurrencyStrategy())) {
			severity = ViolationSeverity.MINOR;
		}

		if (!colmapping.isLazy()) {
			report.addViolation(new ReviewReportViolation(this, colmapping.getOwner(), ViolationSeverity.BLOCKER,
					"collection is not lazy by default for " + colmapping.getRole()));
		}

	}
}
