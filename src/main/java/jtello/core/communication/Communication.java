package jtello.core.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.log4j.Logger;

/**
 * Connect with Tello to execute command supply the drone flight status
 * @author Ariel
 *
 */
public class Communication implements Supplier<Optional<FlightStatus>> {

	final static Logger logger = Logger.getLogger(Communication.class);

	private BlockingQueue<FlightStatus> flightStatusQueue = new LinkedBlockingDeque<>(1);

	private DatagramSocket commandSocket;

	private DatagramSocket statusSocket;

	private InetAddress ipAddress;

	public static final String DRONE_IP_ADDRESS = "192.168.10.1";

	public static final Integer DRONE_COMMAND_UDP_PORT = 8889;
	
	public static final Integer DRONE_COMMAND_SOCKET_TIMEOUT = 1000;

	public static final Integer DRONE_STATUS_UDP_PORT = 8890;

	private List<Consumer<String>> commandListeners = new ArrayList<>();
	
	public Communication(){
		try {
			this.ipAddress = InetAddress.getByName(DRONE_IP_ADDRESS);
		} catch (UnknownHostException e) {
			throw new RuntimeException("Unknown host");
		}
		connect();
		collectStatus();
	}
	
	/**
	 * collect drone status and update the flightStatusQueue for another Thread to consume.
	 */
	private void collectStatus() {
		try {
			statusSocket = new DatagramSocket(DRONE_STATUS_UDP_PORT);
		} catch (SocketException e) {
			throw new RuntimeException("Could not connect");
		}
		
		new Thread(() -> {
			while (true) {
				try {
					FlightStatus flightStatus = receiveStatus();
					logger.debug("status= " + flightStatus);
					flightStatusQueue.clear();
					flightStatusQueue.offer(flightStatus);
				} catch (Exception e) {
					logger.warn("receiveStatus", e);
				}
			}
		}).start();
	}

	private void connect() {
		try {
			commandSocket = new DatagramSocket(DRONE_COMMAND_UDP_PORT);
			commandSocket.connect(ipAddress, DRONE_COMMAND_UDP_PORT);
			commandSocket.setSoTimeout(DRONE_COMMAND_SOCKET_TIMEOUT);
		} catch (SocketException e) {
			throw new RuntimeException("Could not connect");
		}
	}

	/**
	 * add a listener for executed commands
	 * @param listener
	 */
	public void addCommandListener(Consumer<String> listener) {
		commandListeners.add(listener);
	}
	
	public boolean executeCommand(final String command) {
		logger.info("Executing tello command: " + command);
		commandListeners.forEach(i->i.accept(command));
		try {
			sendData(command);
			String response = receiveData();
			commandListeners.forEach(i->i.accept(command+" - "+response));
			logger.info("Tello response: " + response);
			return "ok".equalsIgnoreCase(response);
		} catch (IOException e) {
			logger.warn("Exception occurred during sending and receiving command" + e);
			commandListeners.forEach(i->i.accept(command+" - error"));
			return false;
		}
	}

	private void sendData(String data) throws IOException {
		byte[] sendData = data.getBytes();
		final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, DRONE_COMMAND_UDP_PORT);
		commandSocket.send(sendPacket);
	}

	private String receiveData() throws IOException {
		byte[] receiveData = new byte[1024];
		final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		commandSocket.receive(receivePacket);
		return trimExecutionResponse(receiveData, receivePacket);
	}

	private FlightStatus receiveStatus() {
		byte[] receiveData = new byte[1024];
		final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			statusSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new FlightStatus(trimExecutionResponse(receiveData, receivePacket));
	}

	private String trimExecutionResponse(byte[] response, DatagramPacket receivePacket) {
		response = Arrays.copyOf(response, receivePacket.getLength());
		return new String(response, StandardCharsets.UTF_8);
	}

	@Override
	public Optional<FlightStatus> get() {
		try {
			return Optional.ofNullable(flightStatusQueue.poll(1, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
