package be.sms.hibernate.reviewer.model;

import org.hibernate.mapping.PersistentClass;

import be.sms.hibernate.reviewer.HibernateMappingReviewRule;

public class ReviewReportViolation {

	private HibernateMappingReviewRule violatedRule;
	private PersistentClass mapping;
	private ViolationSeverity severity;
	private String comment;

	public ReviewReportViolation(HibernateMappingReviewRule violatedRule, PersistentClass mapping,
			ViolationSeverity severity, String comment) {
		super();
		this.violatedRule = violatedRule;
		this.mapping = mapping;
		this.severity = severity;
		this.comment = comment;
	}

	public HibernateMappingReviewRule getViolatedRule() {
		return violatedRule;
	}

	public void setViolatedRule(HibernateMappingReviewRule violatedRule) {
		this.violatedRule = violatedRule;
	}

	public PersistentClass getMapping() {
		return mapping;
	}

	public void setMapping(PersistentClass mapping) {
		this.mapping = mapping;
	}

	public ViolationSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(ViolationSeverity severity) {
		this.severity = severity;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String asString() {
		return this.mapping.getClassName() + "\t" + violatedRule.getClass().getSimpleName() + "\t" + severity + "\t" + comment;
	}

	@Override
	public String toString() {
		return asString();
	}
}
