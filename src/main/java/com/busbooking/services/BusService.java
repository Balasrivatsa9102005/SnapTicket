package com.busbooking.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busbooking.dto.ApiResponseDTO;
import com.busbooking.dto.BusRequestDTO;
import com.busbooking.dto.BusResponseDTO;
import com.busbooking.entities.Bus;
import com.busbooking.entities.Seat;
import com.busbooking.exceptions.ResourceNotFoundException;
import com.busbooking.repositories.BusRepository;
import com.busbooking.repositories.SeatRepository;

@Service
public class BusService {

    private final BusRepository busRepository;
    private final SeatRepository seatRepository;

    public BusService(
            BusRepository busRepository,
            SeatRepository seatRepository) {
        this.busRepository = busRepository;
        this.seatRepository = seatRepository;
    }

    private BusResponseDTO toDTO(Bus bus) {
        return new BusResponseDTO(
                bus.getId(),
                bus.getBusNumber(),
                bus.getOperatorName(),
                bus.getBusType(),
                bus.getTotalSeats()
        );
    }

    @Transactional
    public ApiResponseDTO createBus(BusRequestDTO dto) {
        if (busRepository.existsByBusNumber(dto.busNumber())) {
            return new ApiResponseDTO(false, "Bus number already exists");
        }

        Bus bus = new Bus(
                dto.busNumber(),
                dto.operatorName(),
                dto.busType(),
                dto.totalSeats()
        );

        busRepository.save(bus);
        generateSeats(bus);

        return new ApiResponseDTO(true, "Bus created successfully");
    }

    public BusResponseDTO findBus(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bus not found: " + id));

        return toDTO(bus);
    }

    @Transactional
    public ApiResponseDTO updateBus(Long id, BusRequestDTO dto) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bus not found: " + id));

        if (!bus.getBusNumber().equals(dto.busNumber())
                && busRepository.existsByBusNumber(dto.busNumber())) {
            return new ApiResponseDTO(false, "Bus number already exists");
        }

        if (bus.getTotalSeats() != dto.totalSeats()) {
            return new ApiResponseDTO(
                    false,
                    "Seat capacity cannot be changed after bus creation");
        }

        bus.setBusNumber(dto.busNumber());
        bus.setOperatorName(dto.operatorName());
        bus.setBusType(dto.busType());

        busRepository.save(bus);

        return new ApiResponseDTO(true, "Bus updated successfully");
    }

    @Transactional
    public ApiResponseDTO deleteBus(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bus not found: " + id));

        busRepository.delete(bus);

        return new ApiResponseDTO(true, "Bus deleted successfully");
    }

    private void generateSeats(Bus bus) {
        List<Seat> seats = new ArrayList<>();

        for (int i = 1; i <= bus.getTotalSeats() / 2; i++) {
            seats.add(new Seat(i + "A", bus));
            seats.add(new Seat(i + "B", bus));
        }

        if (bus.getTotalSeats() % 2 != 0) {
        	seats.add(new Seat((bus.getTotalSeats() / 2 + 1) + "A", bus));
        }

        seatRepository.saveAll(seats);
    }
}