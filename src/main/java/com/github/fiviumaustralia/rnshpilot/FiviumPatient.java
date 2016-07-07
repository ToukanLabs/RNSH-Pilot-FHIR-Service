package com.github.fiviumaustralia.rnshpilot;

public class FiviumPatient {
  private String id;
  private String mrn;
  private String ehrId;
  private String dob;
  private String firstname;
  private String surname;
  private String address;
  private String phone;
  private String email;
  private String gender;
  private String tumorType;
  private String surgical;
  private String allergies;
  //public static String Id;

//  public FiviumPatient(String pId, String fname, String lname, String sex, String dateOfBirth, String addy) {
//    id = pId;
//    firstname = fname;
//    surname = lname;
//    gender = sex;
//    dob = dateOfBirth;
//    address = addy;
//  }

  @Override
  public String toString() {
    String s = "";
    s = s.concat(id).concat(" ").concat(firstname).concat(" ").concat(surname).concat(" ").concat(address);
    return s;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getMrn() {
    return mrn;
  }
  public void setMrn(String mrn) {
    this.mrn = mrn;
  }
  public String getEhrId() {
    return ehrId;
  }
  public void setEhrId(String ehrId) {
    this.ehrId = ehrId;
  }
  public String getDob() {
    return dob;
  }
  public void setDob(String dob) {
    this.dob = dob;
  }
  public String getFirstname() {
    return firstname;
  }
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
  public String getSurname() {
    return surname;
  }
  public void setSurname(String surname) {
    this.surname = surname;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }
  public String getTumorType() {
    return tumorType;
  }
  public void setTumorType(String tumorType) {
    this.tumorType = tumorType;
  }
  public String getSurgical() {
    return surgical;
  }
  public void setSurgical(String surgical) {
    this.surgical = surgical;
  }
}