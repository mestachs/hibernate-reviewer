package be.sms.hibernate.reviewer.rules;

import java.util.Iterator;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import be.sms.hibernate.reviewer.HibernateMappingReviewRule;
import be.sms.hibernate.reviewer.MappingReviewReport;
import be.sms.hibernate.reviewer.model.ReviewReportViolation;
import be.sms.hibernate.reviewer.model.ViolationSeverity;

public class AvoidIgnoreNotFoundMappingReviewRule implements HibernateMappingReviewRule {

	public void review(Configuration configuration, PersistentClass mapping, MappingReviewReport report) {
		for (Iterator iter = mapping.getReferenceablePropertyIterator(); iter.hasNext();) {
			Property element = (Property) iter.next();
			if (element.getValue() instanceof ManyToOne) {
				ManyToOne many = (ManyToOne) element.getValue();

				if (many.isIgnoreNotFound()) {
					report.addViolation(new ReviewReportViolation(
							this,
							mapping,
							ViolationSeverity.MAJOR,
							many
									+ "If not-found property of many-to-one mapping is set to 'ignore', "
									+ "the association always gets eagerly initialized regardless of the value of the 'lazy' property."
									+ " :" + many.getReferencedEntityName()+" - "+many.getReferencedPropertyName()));
				}

			}
		}

	}

	public void review(Configuration configuration, Collection colmapping, MappingReviewReport report) {
		// TODO Auto-generated method stub

	}

}
