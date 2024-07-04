package com.ai.genie.localdatabase;

import java.io.Serializable;

public class LocalScrollViewModel implements Serializable {

    public long id;
    public String category_id;
    public String question;
    public String answer;
    public String date_time;
    public String isImage = "";
    public String image;

   /* public LocalScrollViewModel(long id,String category_id, String question,String answer,String isImage,String date_time) {
        this.id = id;
        this.category_id = category_id;
        this.question = question;
        this.answer = answer;
        this.isImage = isImage;
        this.date_time = date_time;


//        this.date = date;
    }

    public String getIsImage() {
        return isImage;
    }

    public void setIsImage(String isImage) {
        this.isImage = isImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }*/
}
