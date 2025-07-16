package com.example.nutigo_prm.Entity;

public class Feedback {
    private int id;
    private int rating;
    private String comment;
    private int productId;

    public Feedback(int id, int rating, String comment, int productId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.productId = productId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }

}
