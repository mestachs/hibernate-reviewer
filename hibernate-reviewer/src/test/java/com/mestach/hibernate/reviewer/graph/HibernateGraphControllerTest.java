package com.mestach.hibernate.reviewer.graph;

import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.cfg.Configuration;

import com.mestach.model.BadSoldier;
import com.mestach.model.BadTroop;
import com.mestach.model.Child;
import com.mestach.model.Parent;
import com.mestach.model.bank.BadAccount;
import com.mestach.model.books.BadAuthor;
import com.mestach.model.books.BadBook;
import com.mestach.model.books.Category;


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
				.generate(new DefaultGraphFilter("com.mestach.model.books"));
		System.out.println(results);
		results = controller.generate(new DefaultGraphFilter("com.mestach.model"));
		System.out.println(results);
		results = controller.generate(new DefaultGraphFilter("^com.*"));
		System.out.println(results);
	}
}
