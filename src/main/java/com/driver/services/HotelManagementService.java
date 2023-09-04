package com.driver.services;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.driver.repositories.HotelManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class HotelManagementService {

    HotelManagementRepository hotelManagementRepository = new HotelManagementRepository();
    public String addHotel(Hotel hotel) {
        if(hotel == null) return "FAILURE";
        if(hotel.getHotelName() == null || hotel.getHotelName().isEmpty()) return "FAILURE";
        boolean isHotelExist = hotelManagementRepository.isHotelExist(hotel.getHotelName());
        if(isHotelExist) return "FAILURE";
        hotelManagementRepository.addHotel(hotel);
        return "SUCCESS";
    }

    public int addUser(User user) {
        hotelManagementRepository.addUser(user);
        User resUser = hotelManagementRepository.getUserByaadharId(user.getaadharCardNo());
        return resUser.getaadharCardNo();
    }
    public String getLexicographicalSmallHotelName(String currentHotelName, String maxFecilitetedHotelName) {
        int len = Math.min(currentHotelName.length(), maxFecilitetedHotelName.length());
        String minCh = "";
        for (int ch = 0; ch < len; ch++) {
            if(currentHotelName.toLowerCase().charAt(ch) < maxFecilitetedHotelName.toLowerCase().charAt(ch)) return currentHotelName;
            if(maxFecilitetedHotelName.toLowerCase().charAt(ch) < currentHotelName.toLowerCase().charAt(ch)) return maxFecilitetedHotelName;
        }
        return currentHotelName;
    }
    public String getHotelWithMostFacilities() {
        Collection<Hotel> hotlsList = hotelManagementRepository.getHotelsList();
        int maxFacility = 0;
        String maxFecilitetedHotel = "";
        for(Hotel hotel: hotlsList) {
            if(maxFacility == 0 || hotel.getFacilities().size() > maxFacility) {
                maxFecilitetedHotel = hotel.getHotelName();
                maxFacility = hotel.getFacilities().size();
            }
            if(hotel.getFacilities().size() == maxFacility) {
                maxFecilitetedHotel = getLexicographicalSmallHotelName(hotel.getHotelName(), maxFecilitetedHotel);
            }
        }
        if(maxFacility == 0) return "";
        return maxFecilitetedHotel;
    }
    public int bookARoom(Booking booking) {
        Hotel hotel = hotelManagementRepository.getHotelByName(booking.getHotelName());
        if(hotel == null) return -1;
        if(hotel.getAvailableRooms() < booking.getNoOfRooms()) return -1;

        UUID bookingId = UUID.randomUUID();
        booking.setBookingId(bookingId.toString());
        int amountToBePaid = booking.getNoOfRooms()*hotel.getPricePerNight();
        booking.setAmountToBePaid(amountToBePaid);
        hotelManagementRepository.bookARoom(booking);

        int remainingCount = hotel.getAvailableRooms() - booking.getNoOfRooms();
        hotel.setAvailableRooms(remainingCount);
        hotelManagementRepository.addHotel(hotel);

        return amountToBePaid;
    }

    public int getBookings(Integer aadharCard) {
        Collection<Booking> allBookings= hotelManagementRepository.getBookings();
        int count = 0;
        for(Booking booking: allBookings) {
            if(booking.getBookingAadharCard() == aadharCard) count++;
        }
        return count;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelManagementRepository.getHotelByName(hotelName);
        List<Facility> existingFacilities = hotel.getFacilities();
        if(existingFacilities.isEmpty()) {
            hotel.setFacilities(newFacilities);
            hotelManagementRepository.addHotel(hotel);
        } else {
            for(Facility facility: newFacilities) {
                if(!existingFacilities.contains(facility)) {
                    existingFacilities.add(facility);
                }
            }
            hotel.setFacilities(existingFacilities);
            hotelManagementRepository.addHotel(hotel);
        }
        return hotel;
    }
}
