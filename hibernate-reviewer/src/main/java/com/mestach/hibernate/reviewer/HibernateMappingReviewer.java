package com.mestach.hibernate.reviewer;

import java.util.Iterator;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;

public class HibernateMappingReviewer {

	private HibernateMappingReviewRule[] rules;

	private Configuration configuration;

	public MappingReviewReport reviewAllMappings() {

		MappingReviewReport report = new MappingReviewReport();

		for (Iterator iter = configuration.getClassMappings(); iter.hasNext();) {
			PersistentClass mapping = (PersistentClass) iter.next();

			for (int i = 0; i < rules.length; i++) {
				HibernateMappingReviewRule rule = rules[i];
				rule.review(configuration,mapping, report);
			}
		}

		for (Iterator iter = configuration.getCollectionMappings(); iter.hasNext();) {
			Collection colmapping = (Collection) iter.next();
			for (int i = 0; i < rules.length; i++) {
				HibernateMappingReviewRule rule = rules[i];
				rule.review(configuration,colmapping, report);
			}

		}
		System.out.println(report.toString());
		return report;
	}

	public HibernateMappingReviewRule[] getRules() {
		return this.rules;
	}

	public void setRules(HibernateMappingReviewRule[] rules) {
		this.rules = rules;
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}
