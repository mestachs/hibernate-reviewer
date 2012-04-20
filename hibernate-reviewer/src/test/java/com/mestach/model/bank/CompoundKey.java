package com.mestach.model.bank;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompoundKey implements Serializable {

	private static final long serialVersionUID = -2656094444822943520L;

	private String empId;

	private String accountId;

	public CompoundKey() {
	}

	public CompoundKey(String empId, String accountId) {

		this.empId = empId;

		this.accountId = accountId;

	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

}