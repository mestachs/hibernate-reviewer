package be.sms.hibernate.reviewer.graph;

import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.cfg.Configuration;

import be.sms.model.BadSoldier;
import be.sms.model.BadTroop;
import be.sms.model.Child;
import be.sms.model.Parent;
import be.sms.model.bank.BadAccount;
import be.sms.model.books.BadAuthor;
import be.sms.model.books.BadBook;
import be.sms.model.books.Category;

public class HibernateGraphControllerTest extends TestCase {
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
		HibernateGraphController controller = new HibernateGraphController();
		controller.setConfiguration(cfg);

		DefaultAttributeFiller attributeFiller = new DefaultAttributeFiller();
		controller.setAttributeFiller(attributeFiller);
		Map<String, Object> results = controller
				.generate(new DefaultGraphFilter("be.sms.model.books"));
		System.out.println(results);
		results = controller.generate(new DefaultGraphFilter("be.sms.model"));
		System.out.println(results);
		results = controller.generate(new DefaultGraphFilter("^be.*"));
		System.out.println(results);
	}
}
