package socket.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
	public static void main(String... args) {
		// �ڵ� close
		for (int i = 0; i < 1000; i++) {
			try (Socket client = new Socket()) {
				// Ŭ���̾�Ʈ �ʱ�ȭ
				InetSocketAddress ipep = new InetSocketAddress("127.0.0.1", 9999);
				// ����
				client.connect(ipep);
				// send,reciever ��Ʈ�� �޾ƿ���
				// �ڵ� close

				try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {
					// ������ ������ ������
					// 4byte
					String message = "EXIT";
					byte[] data = message.getBytes();
					sender.write(data, 0, data.length);
					// �����κ��� ������ �ޱ�
					// 7byte - Welcome
					data = new byte[7];
					receiver.read(data, 0, 7);
					// ���Ÿ޽��� ���
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
