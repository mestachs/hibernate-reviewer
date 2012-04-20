package com.mestach.hibernate.reviewer.rules;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mestach.hibernate.reviewer.HibernateMappingReviewRule;
import com.mestach.hibernate.reviewer.MappingReviewReport;
import com.mestach.hibernate.reviewer.model.ReviewReportViolation;
import com.mestach.hibernate.reviewer.model.ViolationSeverity;


public class CachingMappingReviewRule implements HibernateMappingReviewRule {

	/** logging */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CachingMappingReviewRule.class);

	public void review(Configuration configuration, PersistentClass mapping,
			MappingReviewReport report) {

	}

	public void review(Configuration configuration, Collection colmapping,
			MappingReviewReport report) {
		boolean ownerHasCacheEnabled = hasCacheEnabled(colmapping.getOwner());

		boolean colHasCacheEnabled = hasCacheEnabled(colmapping);
		LOGGER.debug("owner " + ownerHasCacheEnabled + " collection "
				+ !colHasCacheEnabled + " "
				+ colmapping.getOwner().getClassName());
		if (ownerHasCacheEnabled && !colHasCacheEnabled) {
			report.addViolation(new ReviewReportViolation(this, colmapping
					.getOwner(), ViolationSeverity.MAJOR, colmapping.getRole()
					+ " collection isn't cached and the main owner is cached :"
					+ colmapping.getOwner().getClassName()));
		}

	}

	private boolean hasCacheEnabled(Collection colmapping) {
		return StringUtils.isNotEmpty(colmapping.getCacheConcurrencyStrategy());
	}

	/**
	 * @param owner
	 * @return
	 */
	private boolean hasCacheEnabled(PersistentClass owner) {
		return StringUtils.isNotEmpty(owner.getCacheConcurrencyStrategy());
	}

}
