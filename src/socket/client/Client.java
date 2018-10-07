package socket.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
	public static void main(String... args) {
		for (int i = 0; i < 10000; i++) {
			try (Socket client = new Socket()) {

				InetSocketAddress ipep = new InetSocketAddress("127.0.0.1", 54321);

				client.connect(ipep);

				try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {

					String message = "Hello" + i;
					byte[] data = message.getBytes();
					sender.write(data, 0, data.length);
					data = new byte[4096];
					receiver.read(data, 0, 4096);
					message = new String(data);
					String out = String.format("recieve - %s", message);
					System.out.println(out);

				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
