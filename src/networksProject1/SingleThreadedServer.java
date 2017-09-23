import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class is the service that will handle the client requests.
 * 
 * @author Dorian Johnson
 */
public class SingleThreadedServer extends Thread {

	private final Socket clientSocket;


	/**
	 * 
	 * @param clientSocket
	 */
	public SingleThreadedServer(final Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * run() method handles the behavior of the server
	 */
	@Override
	public void run() {
		try (
				// imported reader to read incoming data
				final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				// imported writer to write outgoing data
				final PrintWriter out = new PrintWriter(clientSocket.getOutputStream());) {

			// Reads incoming data, outputs data to the writer based on input
			final String input = in.readLine();
			System.out.printf("%nAccepted new client connection with command request: %s%n", input);

			switch (Integer.valueOf(input)) {
			case 1:
				System.out.println("Executing datetime request.");
				this.executeCommand(out, "date");
				break;
			case 2:
				System.out.println("Executing uptime request.");
				this.executeCommand(out, "uptime");
				break;
			case 3:
				System.out.println("Executing memory use request.");
				this.executeCommand(out, "free");
				break;
			case 4:
				System.out.println("Executing netstat request.");
				this.executeCommand(out, "netstat");
				break;
			case 5:
				System.out.println("Executing current user(s) request.");
				this.executeCommand(out, "who");
				break;
			case 6:
				System.out.println("Executing current processes request.");
				this.executeCommand(out, "ps", "-e");
				break;
			default:
				System.out.println("Unrecognized input detected.");
				break;
			}
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will execute each command and write out the response.
	 * 
	 * @param out
	 * @param command
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected void executeCommand(final PrintWriter out, final String... command) throws IOException, InterruptedException {
		final ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.redirectErrorStream(true);
		final Process process = processBuilder.start();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
			String response = null;
			while ((response = in.readLine()) != null) {
				out.println(response);
				out.flush();
			}
			process.waitFor();
		}
	}
}