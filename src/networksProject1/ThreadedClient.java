import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;

/**
 * This class creates a single thread to handle a single request and response from the server and calculate turnaround time.
 * @author Dorian Johnson
 */
public class ThreadedClient extends Thread {
	private String host;
	private int port;
	private int clientNum;
	private String request;

	/**
	 * Constructor to create a ThreadedClient.
	 * @param host
	 * @param port
	 * @param clientNum
	 * @param request
	 */
	public ThreadedClient(final String host, final int port, final int clientNum, final String request) {
		this.host = host;
		this.port = port;
		this.clientNum = clientNum;
		this.request = request;
	}

	/**
	 * the run() method handles the behavior of client requests
	 */
	@Override
	public void run() {
		try (
				// opens socket for outgoing data
				final Socket socket = new Socket(host, port);
				// writer to output data
				final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				// reader to take in data
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			) {
			// outputs requests, manages responses, calculates turnaround time,
			Instant now = Instant.now();
			// send request to server
			out.printf("%s%n", request);
			out.flush();

			String output = null;
			// output response from server
			while ((output = in.readLine()) != null) {
				System.out.println(output);
			}
			//long turnaroundTime = System.nanoTime() - startTime;
			long turnaroundTime = Duration.between(now, Instant.now()).toMillis();
			System.out.printf("Client %s request processed in %d ms%n", clientNum, turnaroundTime);
			// Add turnaround time for this client thread to total in NetworkClient
			NetworkClient.totalTurnaroundTime.add(turnaroundTime);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}