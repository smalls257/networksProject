import java.net.ServerSocket;

/**
 * This class will create a multi-threaded server with a service listening on a
 * port for incoming client requests.
 * 
 * @author Dorian Johnson
 */
public class NetworkServer {
	// Arbitrary port number
	final static int PORT = 19999;

	/**
	 * This is the main method to start the Network Server.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String... args) {
		try (	// instantiates serverSocket
				ServerSocket serverSocket = new ServerSocket(PORT)
			) {
			// lets the user know that the server is ready
			System.out.printf("Server initialized and listening on port %d.%n", PORT);
			while (true) {	
				// accept new client connection and start a thread to handle the client request
				new SingleThreadedServer(serverSocket.accept()).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} 
	}
}

