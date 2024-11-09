package com.rawdapos.rawdapos.models;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Users {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String firstName;
  private String lastName;

  @Column(unique = true)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  private String phone;
  private String addresse;
  private String password;
  private String role;
  private Date createdAt;
  private Date updatedAt;

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddresse() {
    return addresse;
  }

  public void setAddresse(String addresse) {
    this.addresse = addresse;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt() {
    this.createdAt = new Date();
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt() {
    this.updatedAt = new Date();
  }
}