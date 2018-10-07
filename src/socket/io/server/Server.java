package socket.io.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author Yoon1
 *
 */
public class Server extends Thread {
	private String ip = null;
	// Receiver
	private InputStream receiver = null;
	private OutputStream sender = null;

	/**
	 * 
	 * @author Yoon1
	 *
	 */
	public Server(String ip, InputStream receiver, OutputStream sender) {
		this.ip = ip;
		this.receiver = receiver;
		this.sender = sender;
	}

	/**
	 * 
	 * @author Yoon1
	 *
	 */
	@Override
	public void run() {
		try {
			while (true) {
				boolean isBreak = false;
				byte[] data = new byte[9];
				receiver.read(data, 0, 9);
				String message = new String(data);
				message.replace("\0", "");
				String out = String.format("recieve - %s", message);
				System.out.println(out);
				if (message.trim().length() == 0) {
					isBreak = true;
				}

				message = "From Server: " + ip;
				data = message.getBytes();

				this.sender.write(data);
				if (isBreak) {
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	}

	/**
	 * 
	 * @author Yoon1
	 *
	 */
	public void close() {
		try {
			this.receiver.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			this.sender.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @author Yoon1
	 *
	 */
	public static void main(String... args) {

		InetAddress local;
		try (ServerSocket server = new ServerSocket()) {

			Scanner scan = new Scanner(System.in);
			System.out.print("Input Port :");
			int port = scan.nextInt();

			InetSocketAddress ipep = new InetSocketAddress(port);

			local = InetAddress.getLocalHost();

			String ip = local.getHostAddress();

			server.bind(ipep);

			System.out.print(ip);
			System.out.println(": Ready to Recieve");

			while (true) {
				Socket client = server.accept();
				final String client_ip = client.getInetAddress().getLocalHost().getHostAddress();
				System.out.println("Connection with " + client_ip);

				final Server  serverThread = new Server(ip, client.getInputStream(), client.getOutputStream());
				serverThread.start();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
