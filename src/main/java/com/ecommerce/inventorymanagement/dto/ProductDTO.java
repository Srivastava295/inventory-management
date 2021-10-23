package com.ecommerce.inventorymanagement.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

//Data transfer object for transferring Product data between application layers
public class ProductDTO {

	private Long id;
	
	@NotEmpty
	private String name;

	@NotEmpty
	private String description;

	@NotNull
	private Long stock;

	@NotEmpty
	private String size;

	@NotEmpty
	private String color;

	@NotNull
	private Long categoryId;

	@NotNull
	private Long groupId;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "ProductDTO [name=" + name + ", description=" + description + ", stock=" + stock + ", size=" + size
				+ ", color=" + color + ", categoryId=" + categoryId + ", groupId=" + groupId + "]";
	}

}
