package com.csv.upload.product_data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "outlet")
public class Outlet implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id")
	private Long id=0L;
	
	@Column(name = "lastname")
	private String lastName;
	
	@Column(name = "location")	
	private String location;
	
	@Column(name = "outletName")
	private String outletName;
	
	@Column(name = "outletType")
	private String outletType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOutletName() {
		return outletName;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	public String getOutletType() {
		return outletType;
	}

	public void setOutletType(String outletType) {
		this.outletType = outletType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((outletName == null) ? 0 : outletName.hashCode());
		result = prime * result + ((outletType == null) ? 0 : outletType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Outlet other = (Outlet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (outletName == null) {
			if (other.outletName != null)
				return false;
		} else if (!outletName.equals(other.outletName))
			return false;
		if (outletType == null) {
			if (other.outletType != null)
				return false;
		} else if (!outletType.equals(other.outletType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Outlet [id=" + id + ", lastName=" + lastName + ", location=" + location + ", outletName=" + outletName
				+ ", outletType=" + outletType + "]";
	}
	
	
	
}
