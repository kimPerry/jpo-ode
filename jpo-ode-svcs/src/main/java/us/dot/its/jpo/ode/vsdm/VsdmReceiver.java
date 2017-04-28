package us.dot.its.jpo.ode.vsdm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VsdmReceiver implements Runnable{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private DatagramSocket socket = null;
	
	public VsdmReceiver(){
		try {
			socket = new DatagramSocket(4445);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		 while (true) {
	            try {
	            	logger.info("Listening on port 4445...");
	                byte[] buf = new byte[256];

	                // receive request
	                DatagramPacket packet = new DatagramPacket(buf, buf.length);
	                socket.receive(packet);
	                
	                String received = new String(packet.getData(), 0, packet.getLength());
	                logger.info("Packet received: " + received);

	                // figure out response
	                String dString = "Hello from Server";

	                buf = dString.getBytes();

			// send the response to the client at "address" and "port"
	                logger.info("Sending response to the client...");
	                InetAddress address = packet.getAddress();
	                int port = packet.getPort();
	                packet = new DatagramPacket(buf, buf.length, address, port);
	                socket.send(packet);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	}

}
