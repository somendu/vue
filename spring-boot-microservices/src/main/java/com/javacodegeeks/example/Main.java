package com.javacodegeeks.example;

import com.javacodegeeks.example.registration.EurekaServer;
import com.javacodegeeks.example.rest.addition.AdditionServer;
import com.javacodegeeks.example.rest.subtraction.SubtractionServer;
import com.javacodegeeks.example.web.WebServer;

public class Main {
	public static void main(String[] args) {

		String serverName = "";

		switch (args.length) {
		case 2:
			System.setProperty("server.port", args[1]);
		case 1:
			serverName = args[0].toLowerCase();
			break;

		default:
			return;
		}

		if (serverName.equals("eureka")) {
			EurekaServer.main(args);
		} else if (serverName.equals("addition")) {
			AdditionServer.main(args);
		} else if (serverName.equals("subtraction")) {
			SubtractionServer.main(args);
		} else if (serverName.equals("web")) {
			WebServer.main(args);
		} else {
			System.out.println("Unknown server type: " + serverName);
		}
	}
}
