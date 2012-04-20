package com.mestach.hibernate.reviewer.rules;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;

import com.mestach.hibernate.reviewer.HibernateMappingReviewRule;
import com.mestach.hibernate.reviewer.MappingReviewReport;
import com.mestach.hibernate.reviewer.model.ReviewReportViolation;
import com.mestach.hibernate.reviewer.model.ViolationSeverity;


public class BatchSizeMappingReviewRule implements HibernateMappingReviewRule {

	public void review(Configuration configuration, PersistentClass mapping,
			MappingReviewReport report) {
		if (noBatchSize(mapping) && !hasCompositeKey(mapping)) {
			report.addViolation(new ReviewReportViolation(this, mapping,
					ViolationSeverity.BLOCKER, "no batch size at class level"));
		}

		if (hasCompositeKey(mapping)) {
			report.addViolation(new ReviewReportViolation(this, mapping,
					ViolationSeverity.CRITICAL,
					"compositeKey are not recommanded, only for legacy mapping"));
		}
	}

	/**
	 * @param mapping
	 * @return
	 */
	private static boolean hasCompositeKey(PersistentClass mapping) {
		return mapping.getIdentifier() instanceof org.hibernate.mapping.Component;
	}

	/**
	 * @param mapping
	 * @return
	 */
	private boolean noBatchSize(PersistentClass mapping) {

		return mapping.getBatchSize() <= 0;
	}


	public void review(Configuration configuration, Collection colmapping,
			MappingReviewReport report) {
		if (colmapping.getBatchSize() <= 0) {
			report.addViolation(new ReviewReportViolation(this, colmapping
					.getOwner(), ViolationSeverity.BLOCKER,
					"no batch size at collection level :"
							+ colmapping.getRole()));
		}

	}
}
