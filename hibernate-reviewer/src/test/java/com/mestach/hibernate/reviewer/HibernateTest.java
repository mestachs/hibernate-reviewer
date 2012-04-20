package com.mestach.hibernate.reviewer;

import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.cfg.Configuration;

import com.mestach.hibernate.reviewer.HibernateMappingReviewRule;
import com.mestach.hibernate.reviewer.HibernateMappingReviewer;
import com.mestach.hibernate.reviewer.MappingReviewReport;
import com.mestach.hibernate.reviewer.rules.AvoidIgnoreNotFoundMappingReviewRule;
import com.mestach.hibernate.reviewer.rules.AvoidJoinMappingReviewRule;
import com.mestach.hibernate.reviewer.rules.AvoidPropertyRefMappingReviewRule;
import com.mestach.hibernate.reviewer.rules.BatchSizeMappingReviewRule;
import com.mestach.hibernate.reviewer.rules.CachingMappingReviewRule;
import com.mestach.hibernate.reviewer.rules.LazyLoadMappingReviewRule;
import com.mestach.model.BadSoldier;
import com.mestach.model.BadTroop;
import com.mestach.model.Child;
import com.mestach.model.Parent;
import com.mestach.model.bank.BadAccount;
import com.mestach.model.books.BadAuthor;
import com.mestach.model.books.BadBook;
import com.mestach.model.books.Category;


public class HibernateTest extends TestCase {

	public void testSampleRules() throws Exception {
		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass(BadBook.class);
		cfg.addAnnotatedClass(BadAuthor.class);
		cfg.addAnnotatedClass(Category.class);
		cfg.addAnnotatedClass(BadAccount.class);
		cfg.addAnnotatedClass(Parent.class);
		cfg.addAnnotatedClass(Child.class);
		cfg.addAnnotatedClass(BadTroop.class);
		cfg.addAnnotatedClass(BadSoldier.class);

		Properties props = new Properties();
		props.load(this.getClass().getClassLoader()
				.getResourceAsStream("hibernate.properties"));
		cfg.setProperties(props);
		cfg.buildMappings();// SessionFactory();
		HibernateMappingReviewer reviewer = new HibernateMappingReviewer();
		reviewer.setRules(new HibernateMappingReviewRule[] {
				new AvoidIgnoreNotFoundMappingReviewRule(),
				new AvoidPropertyRefMappingReviewRule(),
				new CachingMappingReviewRule(),
				new BatchSizeMappingReviewRule(),
				new AvoidJoinMappingReviewRule(),
				new LazyLoadMappingReviewRule() });
		reviewer.setConfiguration(cfg);
		MappingReviewReport report = reviewer.reviewAllMappings();
		System.out.println(report.getItems());
		System.out.println(report.getStats());
	}
}
