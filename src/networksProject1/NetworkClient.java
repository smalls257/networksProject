import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.LongAdder;

/**
 * This class is responsible for creating a multi-threaded network client where each child thread handles processing for each client request and server response.
 * It expects one command line argument which is the IP address of the server host where the service is running.
 * It will ask for the number of clients to generate and display a menu of commands used to request information from the service running on the server host.
 * It will calculate and output the total turnaround time and mean time of all the requests generated by the clients.
 */
public class NetworkClient {
	// Arbitrary port number
	final private static int PORT = 19999;
    final private static String invalidInputMsg = "%nAn invalid input has been detected. Please try again.%n";
	final protected static LongAdder totalTurnaroundTime = new LongAdder();

	/**
	 * This is the main method to start the Network Client.
	 * @param args[0] - the HOST IP address
	 */
	public static void main(String... args) {
		if (args.length != 1) {
			System.err.println("Usage: java NetworkClient <host ip address>");
			System.exit(1);
		}
		try {
			final NetworkClient client = new NetworkClient();
			client.execute(args[0], PORT);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * This method will display the menu, read input from the user, create child threads for processing each client request and server response, 
	 * and output responses and calculations.
	 * @param host - IP address of host
	 * @param port - Listening port of service on server
	 */
	private void execute(String host, int port) {
		final Scanner userInput = new Scanner(System.in);
		final Scanner intInput = new Scanner(System.in);
		int numOfClients = 0;

		while (true) {
			System.out.printf("%nHow many clients should be generated?%n");
			try {
				// Number of clients to generate
				numOfClients = Integer.parseInt(intInput.nextLine());
				if (numOfClients <= 0) {
					System.out.printf(invalidInputMsg);
					continue;					
				}
			} catch (NumberFormatException nfe) {
				System.out.printf(invalidInputMsg);
				continue;
			}

			System.out.println(getDisplayMenu());
			final String choice = userInput.nextLine();

			if ("7".equals(choice)) {
				System.out.printf("%nExiting program.%n");
				break;
			}

			try {
				if (Integer.parseInt(choice) > 6 || Integer.parseInt(choice) < 1) {
					System.out.printf(invalidInputMsg);
				} else {
					// lets the user know that the clients are being sent, and on which port
					System.out.printf("Threaded client initialized. Communicating with host %s using port %d.%n", host,
							port);
					// Initialize all threads
					final List<ThreadedClient> threads = new ArrayList<ThreadedClient>(numOfClients);
					for (int i = 1; i < numOfClients + 1; i++) {
						final ThreadedClient thread = new ThreadedClient(host, port, i, choice);
						threads.add(thread);
					}
					// Start all threads
					for (ThreadedClient thread : threads) {
						thread.start();
					}
					// Join all threads to main thread so that the main thread waits till all child threads are finished processing
					for (ThreadedClient thread : threads) {
						thread.join();
					}
					System.out.printf("%d client(s) with a total turnaround time of %dms and a mean value of %.2fms per request.%n", numOfClients, totalTurnaroundTime.sum(), (totalTurnaroundTime.sum()*1.00f / numOfClients));
					// Reset total turnaround time
					totalTurnaroundTime.reset();
				}
			} catch (NumberFormatException nfe) {
				System.out.printf(invalidInputMsg);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}

		}
		userInput.close();
		intInput.close();
	}

	/**
	 * This methods creates and returns the display menu 
	 * @return String - the display menu
	 */
	private String getDisplayMenu() {
		final String newline = System.getProperty("line.separator");
		final StringBuilder sb = new StringBuilder();
		sb.append(newline);
		sb.append("Please choose a command (1-7) from the menu below:");
		sb.append(newline);
		sb.append("1. Host current Date and Time");
		sb.append(newline);
		sb.append("2. Host uptime");
		sb.append(newline);
		sb.append("3. Host memory use");
		sb.append(newline);
		sb.append("4. Host Netstat");
		sb.append(newline);
		sb.append("5. Host current users");
		sb.append(newline);
		sb.append("6. Host running processes");
		sb.append(newline);
		sb.append("7. Quit");
		sb.append(newline);

		return sb.toString();
	}
}

