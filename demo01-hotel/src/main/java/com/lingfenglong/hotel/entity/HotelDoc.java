package com.lingfenglong.hotel.entity;

public record HotelDoc(
        String id,
        String name,
        String address,
        Integer price,
        Integer score,
        String brand,
        String city,
        String starName,
        String business,
        String location,
        String pic
) {
    public HotelDoc(Hotel hotel) {
        this(
                hotel.getId().toString(),
                hotel.getName(),
                hotel.getAddress(),
                hotel.getPrice(),
                hotel.getScore(),
                hotel.getBrand(),
                hotel.getCity(),
                hotel.getStarName(),
                hotel.getBusiness(),
                hotel.getLatitude() + ", " + hotel.getLongitude(),
                hotel.getPic()
        );
    }
}
