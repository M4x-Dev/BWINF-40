package models;

public class Hotel {

    public long distanceFromStart;
    public float averageRating;

    public Hotel(String rawText) {
        distanceFromStart = Long.parseLong(rawText.split(" ")[0]);
        averageRating = Float.parseFloat(rawText.split(" ")[1]);
    }

}