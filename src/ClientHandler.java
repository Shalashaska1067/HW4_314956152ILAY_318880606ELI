import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
	private final Socket clientSocket;

	public ClientHandler(Socket socket) { this.clientSocket = socket; }

	@Override
	public void run() {
		try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

			String input;
			while ((input = in.readLine()) != null) {
				if (input.equals("EXIT")) break;
				String[] parts = input.split(":");

				if (parts.length < 4 || parts[0].isBlank() || parts[1].isBlank() || parts[3].isBlank()) {
					out.println("Error 200: some fields are blank.");
					continue;
				}
				try {
					String name = parts[0];
					String id = parts[1];
					int code = Integer.parseInt(parts[2]);
					int qnt = Integer.parseInt(parts[3]);
					
					if (qnt < 0) {
						out.println("202: Quantity cannot be negative");
						continue;
					}
					
					String result = CentralServer.updateClient(name, id, code, qnt);
					out.println(result);
					
				} catch (NumberFormatException e) {
					out.println("Error 200: ID and quantity fields must be numbers");
				}
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
}