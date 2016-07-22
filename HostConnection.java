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
		////while ( (input = inputStream.readLine()) != null ) { 
		if ( (input = inputStream.readLine()) != null ) { 
					
			// Write client input (print client input line).
			System.err.println("Client wrote: " + input);
			// Write client input back to the client.
			//outputStream.writeUTF(input);
			////outputStream.println(input); //// <-- This line was preventing the switch between write & read modes.
			
			// If client input is "Bye!" - exit app.
			if ( input.equals("Bye!") ) {
				System.out.println("Server will exit.");
				System.exit(1); // Exit
			}
			/*
			// If client input is "Server!" - Break/exit the while loop.
			else if ( input.equals("Server!") ) {
				System.out.println("Server switched to writing mode"); 
				//System.out.print ("Server First Input: "); // First input field for user.
				break; // End while loop.
			}
			*/
		}
		
		writeToClient();
		
	}
	
	// -----Read user input and write it to the Client.-----
	private void writeToClient() throws IOException, ClassNotFoundException {
		
		System.out.println("Write to the Client!");
		
		String serverUerInputLine; 
		System.err.print ("Server Input: "); // First input field for user.
		
		// Read user input, and store it in the String variable declared above.
		////while ( (serverUerInputLine = userInput.readLine()) != null ) {
		if ( (serverUerInputLine = scannerInput.nextLine()) != null ) {
			
			// Write user input to Client.
			outputStream.println(serverUerInputLine); 
			
			// If user input is "Bye!" - Break/exit the while loop.
			if ( serverUerInputLine.equals("Bye!" ) ) {
				System.out.println("Server will exit.");
				close();
			}
			/*
			// If user input is "Client!" - Break/exit the while loop.
			else if ( serverUerInputLine.equals("Client!") ) { // If Server writes Client
				System.out.println("Server switched to reading mode!");
				break; // End while loop.
			}
			*/
			
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
