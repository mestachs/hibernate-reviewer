package be.sms.hibernate.reviewer;

import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.cfg.Configuration;

import be.sms.hibernate.reviewer.HibernateMappingReviewRule;
import be.sms.hibernate.reviewer.HibernateMappingReviewer;
import be.sms.hibernate.reviewer.MappingReviewReport;
import be.sms.hibernate.reviewer.rules.AvoidIgnoreNotFoundMappingReviewRule;
import be.sms.hibernate.reviewer.rules.AvoidJoinMappingReviewRule;
import be.sms.hibernate.reviewer.rules.AvoidPropertyRefMappingReviewRule;
import be.sms.hibernate.reviewer.rules.BatchSizeMappingReviewRule;
import be.sms.hibernate.reviewer.rules.CachingMappingReviewRule;
import be.sms.hibernate.reviewer.rules.LazyLoadMappingReviewRule;
import be.sms.model.BadSoldier;
import be.sms.model.BadTroop;
import be.sms.model.Child;
import be.sms.model.Parent;
import be.sms.model.bank.BadAccount;
import be.sms.model.books.BadAuthor;
import be.sms.model.books.BadBook;
import be.sms.model.books.Category;

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
