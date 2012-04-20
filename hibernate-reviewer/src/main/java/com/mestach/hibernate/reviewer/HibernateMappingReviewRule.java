package com.mestach.hibernate.reviewer;


import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;

public interface HibernateMappingReviewRule {

	void review(Configuration configuration, PersistentClass mapping, MappingReviewReport report);

	void review(Configuration configuration, Collection colmapping, MappingReviewReport report);
}
