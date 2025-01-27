package com.example.trachax;

public class ChildModel {
    private String name;
    private int age;
    private String gender;
    private String school;
    private String grade;
    private String id;

    // Empty constructor required for Firebase
    public ChildModel() {}

    // Constructor with parameters
    public ChildModel(String name, int age, String gender, String school, String grade) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.school = school;
        this.grade = grade;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
