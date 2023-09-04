package com.driver.repositories;

import com.driver.model.Booking;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Repository
public class HotelManagementRepository {
    public HashMap<String, Hotel> hotelsDB;
    public HashMap<Integer, User> usersDB;
    public HashMap<String, Booking> bookingDB;

    public HotelManagementRepository() {
        hotelsDB = new HashMap<>();
        usersDB = new HashMap<>();
        bookingDB = new HashMap<>();
    }

    public Boolean isHotelExist(String hotelName) {
        return hotelsDB.containsKey(hotelName);
    }
    public void addHotel(Hotel hotel) {
        hotelsDB.put(hotel.getHotelName(), hotel);
    }

    public User getUserByaadharId(int aadharCardNo) {
        return usersDB.get(aadharCardNo);
    }
    public void addUser(User user) {
        usersDB.put(user.getaadharCardNo(), user);
    }
    public Collection<Hotel> getHotelsList() {
        return hotelsDB.values();
    }
    public void bookARoom(Booking booking) {
        bookingDB.put(booking.getBookingId(), booking);
    }
    public Hotel getHotelByName(String hotelName) {
        return hotelsDB.get(hotelName);
    }
    public Collection<Booking> getBookings() {
        return bookingDB.values();
    }
}
