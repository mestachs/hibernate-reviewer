package be.sms.hibernate.reviewer.graph;

import org.hibernate.mapping.PersistentClass;

public class OmittedRelationShip {
	private PersistentClass from;
	private PersistentClass to;
	private String type;

	public OmittedRelationShip(PersistentClass from, PersistentClass to, String type) {
		super();
		this.from = from;
		this.to = to;
		this.type = type;
	}

	public PersistentClass getFrom() {
		return from;
	}

	public void setFrom(PersistentClass from) {
		this.from = from;
	}

	public PersistentClass getTo() {
		return to;
	}

	public void setTo(PersistentClass to) {
		this.to = to;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return toString(from) + "\t" + toString(to) + "\t" + type;
	}

	private String toString(PersistentClass cl) {
		if (cl != null)
			return cl.getClassName();
		return "NA";
	}
}
