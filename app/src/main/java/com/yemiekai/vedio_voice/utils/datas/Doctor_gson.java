package com.yemiekai.vedio_voice.utils.datas;

import java.util.List;

public class Doctor_gson {

    private String name;
    private String description;
    private String resume;
    private String speciality;
    private List<String> post;
    private List<String> position;
    private String image_id;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setPost(List<String> post) {
        this.post = post;
    }

    public void setPosition(List<String> position) {
        this.position = position;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getResume() {
        return resume;
    }

    public String getSpeciality() {
        return speciality;
    }

    public List<String> getPost() {
        return post;
    }

    public List<String> getPosition() {
        return position;
    }

    public String getImage_id() {
        return image_id;
    }


    @Override
    public String toString() {
        return "Doctor_gson{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", resume='" + resume + '\'' +
                ", speciality='" + speciality + '\'' +
                ", post=" + post +
                ", position=" + position +
                ", image_id='" + image_id + '\'' +
                '}';
    }
}
