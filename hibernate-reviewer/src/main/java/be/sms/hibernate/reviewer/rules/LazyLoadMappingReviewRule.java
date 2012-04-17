package be.sms.hibernate.reviewer.rules;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;

import be.sms.hibernate.reviewer.HibernateMappingReviewRule;
import be.sms.hibernate.reviewer.MappingReviewReport;
import be.sms.hibernate.reviewer.model.ReviewReportViolation;
import be.sms.hibernate.reviewer.model.ViolationSeverity;

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
