package com.example.SERVER.util.exception.custom;

public class WrongCurrentPasswordException extends Exception{
	public WrongCurrentPasswordException(String message) {
		super(message);
	}
}
