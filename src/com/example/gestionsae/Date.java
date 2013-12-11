package com.example.gestionsae;

public class Date {
	
	private int id;
	private String jour;
	
	public Date() {
	}
	
	public Date(String jour) {
		super();
		this.jour = jour;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getJour() {
		return jour;
	}
	
	public void setJour(String jour) {
		this.jour = jour;
	}
	
	@Override
	public String toString() {
		return jour;
	}
}
