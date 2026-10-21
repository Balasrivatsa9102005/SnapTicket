package com.busbooking.entities;

import java.util.ArrayList;
import java.util.List;

import com.busbooking.enums.BusType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Bus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String busNumber;
	private String operatorName;
	@Enumerated(EnumType.STRING)
	private BusType busType;
	private int totalSeats;
	
	
	@OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Seat> seats = new ArrayList<>();

	
	@OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Trip> trips = new ArrayList<>();
	
	
	public Bus() {}
	
	public Bus(String busNumber, String operatorName, BusType busType, int totalSeats) {
		this.busNumber = busNumber;
		this.operatorName = operatorName;
		this.busType = busType;
		this.totalSeats = totalSeats;
	}

	public String getBusNumber() {
		return busNumber;
	}

	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public BusType getBusType() {
		return busType;
	}

	public void setBusType(BusType busType) {
		this.busType = busType;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

}
