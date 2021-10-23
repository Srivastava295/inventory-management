package com.ecommerce.inventorymanagement.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_group")
public class UserGroup {
	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	private String name;
	
	@OneToMany
	@JoinTable(name = "group_actions", 
			  joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"), 
			  inverseJoinColumns = @JoinColumn(name = "action_id", referencedColumnName = "id"))
	private Set<Action> actions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Action> getActions() {
		return new HashSet<>(actions);
	}

	public void setActions(Set<Action> actions) {
		this.actions = new HashSet<>(actions);
	}
	
	public void addAction(Action action) {
		this.actions.add(action);
	}
	
	public void removeAction(Action action) {
		this.actions.remove(action);
	}
}
