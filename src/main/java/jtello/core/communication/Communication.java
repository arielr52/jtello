package jtello.core.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

import jtello.core.exception.TelloConnectionException;


public class Communication {

	private static final Logger logger = Logger.getLogger(Communication.class.getName());

	/**
	 * Datagram connection to the Tello drone.
	 */
	private DatagramSocket ds;

	private DatagramSocket serverDs;

	/**
	 * Drone's IP address.
	 */
	private InetAddress ipAddress;

	private InetAddress serverIpAddress;
	
	  /*
	   * Connection IP address.
	   */
	  public static final String IP_ADDRESS = "192.168.10.1";

	  /*
	   * Connection UDP Port.
	   */
	  public static final Integer UDP_PORT = 8889;
	  
	/**
	 * Drones UDP PORT.
	 */
	private Integer udpPort;

	public Communication() throws TelloConnectionException {
	    try {
	      this.ipAddress = InetAddress.getByName(IP_ADDRESS);
	      serverIpAddress = InetAddress.getByName("0.0.0.0");
	      this.udpPort = UDP_PORT;
	    } catch (UnknownHostException e) {
	      throw new TelloConnectionException("Unknown host");
	    }
	    try {
		//	ds = new DatagramSocket(udpPort);
		//	ds.connect(ipAddress, udpPort);
			
			serverDs = new DatagramSocket(8890);
			serverDs.connect(serverIpAddress, 8890);
			
		} catch (SocketException e) {
			logger.info("Connection to the drone could not be established.");
			throw new TelloConnectionException("Could not connect");
		}
	  }

	public boolean connect() {
		try {
			ds = new DatagramSocket(udpPort);
			ds.connect(ipAddress, udpPort);
		} catch (SocketException e) {
			logger.info("Connection to the drone could not be established.");
			throw new TelloConnectionException("Could not connect");
		}
		return true;
	}

	public String executeCommand(final String command) {
		logger.info("Executing tello command: " + command);

		try {
			sendData(command);
			String response = receiveData();
			logger.info("Tello response: " + response);
			return response;
		} catch (IOException e) {
			logger.severe("Exception occurred during sending and receiving command" +e);
			return e.toString();
		}
	}


	private void sendData(String data) throws IOException {
		byte[] sendData = data.getBytes();
		final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, udpPort);
		ds.send(sendPacket);
	}

	private String receiveData() throws IOException {
		byte[] receiveData = new byte[1024];
		final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		ds.receive(receivePacket);
		return trimExecutionResponse(receiveData, receivePacket);
	}

	public String receiveStatus() throws IOException {
		byte[] receiveData = new byte[1024];
		final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		serverDs.receive(receivePacket);
		return trimExecutionResponse(receiveData, receivePacket);
	}
	
	private String trimExecutionResponse(byte[] response, DatagramPacket receivePacket) {
		response = Arrays.copyOf(response, receivePacket.getLength());
		return new String(response, StandardCharsets.UTF_8);
	}

}
