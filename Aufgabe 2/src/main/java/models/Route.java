package models;

import java.util.ArrayList;

public class Route {

    public ArrayList<Hotel> hotels;

    public Route() {
        this.hotels = new ArrayList<>();
    }

    public Route(Route route) {
        this.hotels = new ArrayList<>(route.hotels);
    }

    public Route appendHotel(Hotel hotel) {
        hotels.add(hotel);
        return this;
    }

    public float getLowestHotelRating() {
        float lowest = 5.0f;
        for(Hotel hotel : hotels)
            if(hotel.averageRating < lowest)
                lowest = hotel.averageRating;
        return lowest;
    }

    public String asString() {
        StringBuilder instanceBuilder = new StringBuilder();
        for(Hotel hotel : hotels) instanceBuilder.append(hotel.averageRating).append(" --> ");
        return instanceBuilder.substring(0, instanceBuilder.length() - " --> ".length());
    }

}
