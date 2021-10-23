package com.ecommerce.inventorymanagement.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String username;

	@NotNull
	private String password;

	@NotNull
	@Column(unique = true)
	private String email;

	@OneToMany
	@JoinTable(name = "user_group_map",
			   joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			   inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
	private Set<UserGroup> groups;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<UserGroup> getGroups() {
		return new HashSet<>(groups);
	}

	public void setGroups(Set<UserGroup> groups) {
		this.groups = new HashSet<>(groups);
	}

	public void addGroup(UserGroup group) {
		this.groups.add(group);
	}

	public void removeGroup(UserGroup group) {
		this.groups.remove(group);
	}
}
