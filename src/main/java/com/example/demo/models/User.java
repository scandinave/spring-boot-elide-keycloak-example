package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.ReadPermission;

@ReadPermission(expression = "it's the user profile")
@Include(rootLevel = true)
@Entity
public class User {

    private Long id;

    private String name;

    private Integer age;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return this.id;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}