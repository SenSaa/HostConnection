import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


/* Server Application that interacts with a client application (Android ConnectToHost). */

public class HostConnection {

	ServerSocket serverSocket = null;
	Socket socket = null;
	int port;
	
	PrintWriter outputStream;
	BufferedReader inputStream;
	BufferedReader userInput;
	Scanner scannerInput;
	Scanner inputStream2;
	ObjectInputStream objectInputStream;
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		HostConnection classObj = new HostConnection();
		classObj.openSocket();
	}
	
	
	// Open Socket.
	private void openSocket() throws IOException, ClassNotFoundException {
		port = 6000;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server Socket Open.");
		}
		catch (IOException ioExcep) {
			System.err.println("Input/Output Exception occurred" + ioExcep);
		}
		
		acceptConnection();
	}
	
	
	// Listen and accept connection.
	private void acceptConnection() throws IOException, ClassNotFoundException {
		try {
			socket = serverSocket.accept();
			System.out.println("Server Socket ready to accept Client connection.");
		} 
		catch (IOException ioExcep) {
			System.out.println(ioExcep);
		}
		
		ioStreams();
	}
	
	
	// Open output and input streams.
	private void ioStreams() throws IOException, ClassNotFoundException {
		// Open Output Stream.
		outputStream = new PrintWriter(socket.getOutputStream(), true);
		
		// Open Input Stream.
		inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		inputStream2 = new Scanner(socket.getInputStream());
		
		System.out.println("I/O Streams Open.");
		
		userInput = new BufferedReader(new InputStreamReader(System.in));
		scannerInput = new Scanner(System.in);
				
		readClientInput();
	}
	
	
	// -----Read and print Client input.-----
	private void readClientInput() throws IOException, ClassNotFoundException {
		
		System.out.println("Listening to Client input!");
		
		String input;
		// Read client input, and while it's not null, then...
		if ( (input = inputStream.readLine()) != null ) { 
					
			// Write client input (print client input line).
			System.err.println("Client wrote: " + input);
			
			// If client input is "Bye!" - exit app.
			if ( input.equals("Bye!") ) {
				System.out.println("Server will exit.");
				System.exit(1); // Exit
			}

		}
		
		writeToClient();
		
	}
	
	// -----Read user input and write it to the Client.-----
	private void writeToClient() throws IOException, ClassNotFoundException {
		
		System.out.println("Write to the Client!");
		
		String serverUerInputLine; 
		System.err.print ("Server Input: "); // First input field for user.
		
		// Read user input, and store it in the String variable declared above.
		if ( (serverUerInputLine = scannerInput.nextLine()) != null ) {
			
			// Write input to Client.
			outputStream.println(serverUerInputLine); 
			
			// If input is "Bye!" - close connection.
			if ( serverUerInputLine.equals("Bye!" ) ) {
				System.out.println("Server will exit.");
				close();
			}
			
		}
		readClientInput(); 
	}
	
	
	// 
	private void close() throws IOException {
		System.out.println("close() running");
		// Close I/O Streams and Server Socket.
		outputStream.close();
		inputStream.close();
		userInput.close();
		scannerInput.close();
		inputStream2.close();
		socket.close();
		serverSocket.close();
		System.out.println("Server Close Socket");
	}
	
}
