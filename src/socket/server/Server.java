package socket.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	private InputStream receiver = null;
	private OutputStream sender = null;

	public Server(InputStream receiver, OutputStream sender) {
		this.receiver = receiver;
		this.sender = sender;
	}

	@Override
	public void run() {
		try {
			while (true) {
				boolean isBreak = false;
				// Ŭ���̾�Ʈ�κ��� �޽��� ���
				byte[] data = new byte[4];
				receiver.read(data, 0, 4);
				String message = new String(data);
				message.replace("\0", "");
				String out = String.format("recieve - %s", message);
				System.out.println(out);
				// �޽����� EXIT�� �����Ѵ�.
				if ("EXIT".equals(message)) {
					isBreak = true;
				}
				message = "Welcome";
				data = message.getBytes();
				// Welcome �޽��� �����ϴ�.
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

	public static void main(String... args) {
		// �ڵ� close
		try (ServerSocket server = new ServerSocket()) {
			// ���� �ʱ�ȭ
			InetSocketAddress ipep = new InetSocketAddress(9999);
			server.bind(ipep);
			System.out.println("Initialize complate");

			while (true) {
				// LISTEN ���
				Socket client = server.accept();
				System.out.println("Connection");

				// Stream�� ������� �ѱ��
				Server serverThread = new Server(client.getInputStream(), client.getOutputStream());
				// ������ ����(run�� ȣ���Ѵ�.)
				serverThread.start();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
