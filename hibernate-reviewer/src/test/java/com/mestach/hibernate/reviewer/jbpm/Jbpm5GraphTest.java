package com.mestach.hibernate.reviewer.jbpm;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.task.Attachment;
import org.jbpm.task.BooleanExpression;
import org.jbpm.task.Comment;
import org.jbpm.task.Content;
import org.jbpm.task.Deadline;
import org.jbpm.task.Delegation;
import org.jbpm.task.EmailNotification;
import org.jbpm.task.EmailNotificationHeader;
import org.jbpm.task.Escalation;
import org.jbpm.task.Group;
import org.jbpm.task.I18NText;
import org.jbpm.task.Notification;
import org.jbpm.task.OnAllSubTasksEndParentEndStrategy;
import org.jbpm.task.OnParentAbortAllSubTasksEndStrategy;
import org.jbpm.task.PeopleAssignments;
import org.jbpm.task.Reassignment;
import org.jbpm.task.Status;
import org.jbpm.task.SubTasksStrategy;
import org.jbpm.task.Task;
import org.jbpm.task.TaskData;
import org.jbpm.task.User;

import com.mestach.hibernate.reviewer.HibernateMappingReviewRule;
import com.mestach.hibernate.reviewer.HibernateMappingReviewer;
import com.mestach.hibernate.reviewer.MappingReviewReport;
import com.mestach.hibernate.reviewer.graph.DefaultAttributeFiller;
import com.mestach.hibernate.reviewer.graph.DefaultGraphFilter;
import com.mestach.hibernate.reviewer.graph.HibernateGraphController;
import com.mestach.hibernate.reviewer.rules.AvoidIgnoreNotFoundMappingReviewRule;
import com.mestach.hibernate.reviewer.rules.AvoidJoinMappingReviewRule;
import com.mestach.hibernate.reviewer.rules.AvoidPropertyRefMappingReviewRule;
import com.mestach.hibernate.reviewer.rules.BatchSizeMappingReviewRule;
import com.mestach.hibernate.reviewer.rules.CachingMappingReviewRule;
import com.mestach.hibernate.reviewer.rules.LazyLoadMappingReviewRule;


public class Jbpm5GraphTest extends TestCase {
	public void testSampleRules() throws Exception {
		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass(User.class);
		cfg.addAnnotatedClass(Attachment.class);
		cfg.addAnnotatedClass(Content.class);
		cfg.addAnnotatedClass(BooleanExpression.class);
		cfg.addAnnotatedClass(Comment.class);
		cfg.addAnnotatedClass(Deadline.class);
		cfg.addAnnotatedClass(Delegation.class);
		cfg.addAnnotatedClass(Escalation.class);

		cfg.addAnnotatedClass(Group.class);
		cfg.addAnnotatedClass(I18NText.class);
		cfg.addAnnotatedClass(Notification.class);
		cfg.addAnnotatedClass(EmailNotification.class);
		cfg.addAnnotatedClass(EmailNotificationHeader.class);
		cfg.addAnnotatedClass(PeopleAssignments.class);
		cfg.addAnnotatedClass(Reassignment.class);
		cfg.addAnnotatedClass(Status.class);
		cfg.addAnnotatedClass(Task.class);
		cfg.addAnnotatedClass(TaskData.class);
		cfg.addAnnotatedClass(PeopleAssignments.class);
		cfg.addAnnotatedClass(TaskData.class);
		cfg.addAnnotatedClass(SubTasksStrategy.class);
		cfg.addAnnotatedClass(OnParentAbortAllSubTasksEndStrategy.class);
		cfg.addAnnotatedClass(OnAllSubTasksEndParentEndStrategy.class);

		cfg.addAnnotatedClass(ProcessInstanceLog.class);
		cfg.addAnnotatedClass(NodeInstanceLog.class);
		cfg.addAnnotatedClass(VariableInstanceLog.class);
		cfg.buildMappings();
		fixBatchSize(cfg);
		//cfg.addAnnotatedClass(ProcessInstanceInfo)
		
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader()
				.getResourceAsStream("hibernate.properties"));
		cfg.setProperties(props);
	
		cfg.buildSessionFactory();
		HibernateGraphController controller = new HibernateGraphController();
		controller.setConfiguration(cfg);

		DefaultAttributeFiller attributeFiller = new DefaultAttributeFiller();
		controller.setAttributeFiller(attributeFiller);
		Map<String, Object> results = controller
				.generate(new DefaultGraphFilter("org.jbpm"));
		System.out.println(results);
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

	private void fixBatchSize(Configuration cfg) {
		Iterator<PersistentClass> persist = cfg.getClassMappings();
		while (persist.hasNext()) {
			PersistentClass persistentClass = persist.next();
			persistentClass.setBatchSize(50);
		}
		
		Iterator colls = cfg.getCollectionMappings();
		while (colls.hasNext()) {
			org.hibernate.mapping.Collection  object = (org.hibernate.mapping.Collection) colls.next();
			object.setBatchSize(50);
		}
	}
}
