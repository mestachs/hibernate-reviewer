package be.sms.model.bank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BadAccount {

	private CompoundKey compoundKey;

	private int balance;

	@Id
	public CompoundKey getCompoundKey() {

		return compoundKey;

	}

	public void setCompoundKey(CompoundKey compoundKey) {

		this.compoundKey = compoundKey;

	}

	@Column
	public int getBalance() {

		return balance;

	}

	public void setBalance(int balance) {

		this.balance = balance;

	}

}