package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import GuessingGame.GuessingGame;

public class GameServer {

	private static final int PORT = 4005;

	public static void main(String[] args) throws IOException {

		ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
		ServerSocket serverSocket = new ServerSocket(PORT);
		ExecutorService pool = Executors.newFixedThreadPool(3);
		GuessingGame game = new GuessingGame();

		System.out.println("ChatServer started");
		System.out.println("[SERVER] waiting for client connections");
		try {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Created socket with client : " + clientSocket.getLocalAddress().getHostName());
				ClientHandler client = new ClientHandler(clientSocket, game, clients);
				clients.add(client);
				pool.submit(client);
			}
		} finally {
			serverSocket.close();
			pool.shutdown();
		}

	}

}
