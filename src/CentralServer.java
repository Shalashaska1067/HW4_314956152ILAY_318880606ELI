import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CentralServer {
	private static ArrayList<Client> clientsList = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(9999)) {
			System.out.println("Server is listening on port 9999");
			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("New client connected!");
				ClientHandler clientHandler = new ClientHandler(clientSocket);
				clientHandler.start();
			}
		} catch (IOException e) { e.printStackTrace(); }
	}

	public synchronized static String updateClient(String name, String id, int itemCode, int qty) {
		for (Client c : clientsList) {
			if (c.getId().equals(id)) {
				if (!c.getName().equals(name))
					return "201: Name mismatch for this ID";
				
				c.update(itemCode, qty);
				return "100";
			}
		}
		Client newClient = new Client(name, id);
		newClient.update(itemCode, qty);
		clientsList.add(newClient);
		return "100";
	}
}