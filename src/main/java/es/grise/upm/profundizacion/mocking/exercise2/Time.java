package es.grise.upm.profundizacion.mocking.exercise2;

import java.sql.Timestamp;

public class Time {
	
	public Timestamp getCurrentTime() {
		
		return new Timestamp(System.currentTimeMillis());
		
	}

}
