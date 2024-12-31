package com.example.trachax;

public class ChildModel {
    private String name;
    private String age;
    private String gender;
    private String school;
    private String childClass;

    public ChildModel(String name, String age, String gender, String school, String childClass) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.school = school;
        this.childClass = childClass;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getSchool() {
        return school;
    }

    public String getChildClass() {
        return childClass;
    }
}
