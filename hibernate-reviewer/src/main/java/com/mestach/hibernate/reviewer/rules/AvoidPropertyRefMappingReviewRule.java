package com.mestach.hibernate.reviewer.rules;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.OneToOne;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import com.mestach.hibernate.reviewer.HibernateMappingReviewRule;
import com.mestach.hibernate.reviewer.MappingReviewReport;
import com.mestach.hibernate.reviewer.model.ReviewReportViolation;
import com.mestach.hibernate.reviewer.model.ViolationSeverity;


public class AvoidPropertyRefMappingReviewRule implements HibernateMappingReviewRule {

	public void review(Configuration configuration, PersistentClass mapping, MappingReviewReport report) {
		for (Iterator iterator = mapping.getPropertyIterator(); iterator.hasNext();) {
			Property prop = (Property) iterator.next();
			if (isPropertyRef(prop)) {
				report.addViolation(new ReviewReportViolation(this, mapping, ViolationSeverity.CRITICAL, "consider the use of property-ref only in legacy mapping : " + prop + " "
						+ prop.getValue()));
			}
		}

	}

	public boolean isPropertyRef(Property prop) {
		Object val = prop.getValue();
		if (val instanceof ManyToOne) {
			ManyToOne manytoone = (ManyToOne) val;

			if (StringUtils.isNotEmpty(manytoone.getReferencedPropertyName())) {
				return true;
			}
		} else if (val instanceof OneToOne) {
			OneToOne onetoone = (OneToOne) val;
			if (StringUtils.isNotEmpty(onetoone.getReferencedPropertyName())) {
				return true;
			}
		}
		return false;
	}

	public void review(Configuration configuration, Collection colmapping, MappingReviewReport report) {
		// TODO Auto-generated method stub

	}

}
