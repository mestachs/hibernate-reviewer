package be.sms.hibernate.reviewer.rules;

import java.util.Iterator;

import org.hibernate.FetchMode;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import be.sms.hibernate.reviewer.HibernateMappingReviewRule;
import be.sms.hibernate.reviewer.MappingReviewReport;
import be.sms.hibernate.reviewer.model.ReviewReportViolation;
import be.sms.hibernate.reviewer.model.ViolationSeverity;



public class AvoidJoinMappingReviewRule implements HibernateMappingReviewRule {


	public void review(Configuration configuration, PersistentClass mapping, MappingReviewReport report) {
		for (Iterator iter = mapping.getReferenceablePropertyIterator(); iter.hasNext();) {
			Property element = (Property) iter.next();
			if (element.getValue() instanceof ManyToOne) {
				ManyToOne many = (ManyToOne) element.getValue();

				boolean isSelect = FetchMode.SELECT.equals(many.getFetchMode());
				if (!isSelect) {
					report.addViolation(new ReviewReportViolation(this, mapping, ViolationSeverity.MINOR, many
							+ "for many to one prefer fetch mode select other fetchmode can introduce join's :" + " :"
							+ many.getFetchMode()));
				}
			}
		}
	}

	public void review(Configuration configuration, Collection colmapping, MappingReviewReport report) {

	}

}
