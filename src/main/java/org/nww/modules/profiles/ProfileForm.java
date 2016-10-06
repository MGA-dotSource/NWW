/**
 * 
 */
package org.nww.modules.profiles;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.nww.core.data.form.AbstractPersistentObjectForm;

/**
 * Provides profile pojo validation information.
 * 
 * @author mga
 */
public class ProfileForm extends AbstractPersistentObjectForm {
	
	/****************************************
	 * GENERAL PROFILE FIELDS
	 ****************************************/
	
	@Length(min = 3, max = 25)
	private String surename;
	@Length(min = 3, max = 25)
	private String name;
	@Length(max = 100)
	private String company;
	@Length(max = 15)
	private String telephone;
	@Length(max = 15)
	private String mobile;
	@NotNull
	@Length(min = 5, max = 100)
	@Email
	private String email;
	@Length(max = 250)
	private String homepage;
	
	// additional fields after release 0.6
	@Length(max = 1)
	private String title;
	@Length(max = 2000)
	private String companyDescription;
	@Length(max = 15)
	private String fax;
	
	// additional fields after release 0.7
	@Length(max = 50)
	private String companyUserRole;

	
	/****************************************
	 * ADDRESS FIELDS
	 ****************************************/
	
	@Length(max = 25)
	private String street1;
	@Length(max = 50)
	private String street2;
	@Length(max = 10)
	private String houseNo;
	@Length(max = 10)
	private String postalCode;
	@Length(max = 25)
	private String city;
	
	public String getSurename() {
		return surename;
	}
	
	public void setSurename(String surename) {
		this.surename = surename;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCompany() {
		return company;
	}
	
	public void setCompany(String organization) {
		this.company = organization;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	
	/**
	 * @return the street1
	 */
	public String getStreet1() {
		return street1;
	}
	
	/**
	 * @param street1 the street1 to set
	 */
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	
	/**
	 * @return the street2
	 */
	public String getStreet2() {
		return street2;
	}
	
	/**
	 * @param street2 the street2 to set
	 */
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	
	/**
	 * @return the houseNo
	 */
	public String getHouseNo() {
		return houseNo;
	}
	
	/**
	 * @param houseNo the houseNo to set
	 */
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getCompanyDescription() {
		return companyDescription;
	}
	
	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}
	
	public String getFax() {
		return fax;
	}
	
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	/**
	 * @return the users company role description
	 */
	public String getCompanyUserRole() {
		return companyUserRole;
	}
	
	/**
	 * Set the user role description inside the company.
	 * @param companyUserRole the company role description
	 */
	public void setCompanyUserRole(String companyUserRole) {
		this.companyUserRole = companyUserRole;
	}
}
