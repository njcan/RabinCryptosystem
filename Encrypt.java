import java.math.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class Encrypt
{
    /*
   FUNCTION:   Encrypt()

   ARGUMENTS:  Takes no arguments

   RETURNS:    Returns nothing

   NOTES:      Constructor for encrypting. Calls the necessary functions
               to encrypt the text file with the public key n 
    */
    Encrypt()
    {
    	String text = ReadText();              // Read the text file into a string
    	BigInteger n = new BigInteger(GetN()); // Get N from the Public.txt file
    	Encrypt(text, n);                      // Encrypt input.txt  
    }

    /*
   FUNCTION:   ReadText()

   ARGUMENTS:  Takes no arguments

   RETURNS:    Returns a String containing the entire text file

   NOTES:      The file to be read MUST be named "Input.txt"
    */
    private String ReadText()
    {
	String inFile = "Input.txt"; // The name of the file to be read in
	String line = "";            // Store each line of the file here
	String text = "";            // Store the entire file's text here
	
	// Try to instantiate both readers
	 try
	 {
		 FileReader reader = new FileReader(inFile);          // Create a filereader
		 BufferedReader bReader = new BufferedReader(reader); // Wrap the file reader so we may call readLine()
		 
		 // Try to actually read from the file
		 try
		 {
			 // While there's something to read, append it to "text"
			 while((line = bReader.readLine()) != null)
			     {
				 	text = text + line;
			     }

			 // The file is done being read in, we must pad 
			 // the string to be a multiple of 50
			 while(text.length() % 50 != 0)
			     {
				 	Random rand = new Random();                     // Seed random 
				 	int random = rand.nextInt((126 - 32) + 1) + 32; // The next random int is between 32 to 126 (prinatable ascii chars)
				 	text = text + (char)random;                     // Append the random char to the text
			     }

			 // Return the string when we're done
			 System.out.println("Successfully read plaintext from file!");
			 bReader.close();
			 return text;

			 // When attempting to read from the file it has the
			 // potential to throw an error, we have to keep the 
			 // compiler happy by catching this exception
		     } catch(IOException e)
		     {
		    	 System.out.println("Error reading file!");
		     }

		 // When attempting to open the file, it may not exist
		 // we need to compensate for this and report the 
		 // error to the user
	     } catch(FileNotFoundException e)
	     {
	    	 System.out.println("File not found or does not exist!");
	     }

	 // Keeps the compiler happy, should never get here
	 return text;
    }

    /*
   FUNCTION:   GetN()

   ARGUMENTS:  Takes no arguments

   RETURNS:    Returns the public key n as a string

   NOTES:      The return value is a string as the BigInteger constructor 
               users a string to create the value. The file to read from
	       MUST be named Public.txt
    */
    private String GetN()
    {
	String inFile = "Public.txt"; // The file must be named
	String line = "";             // Empty string to read into

	// Try to instantiate the readers
	try
	{
		FileReader reader = new FileReader(inFile);          // Create a file reader for the input
		BufferedReader bReader = new BufferedReader(reader); // Wrap the file reader so we may call readLine()
		
		// Try to read from the file
		try 
		{
			// We only need to read one line
			line = bReader.readLine();
			bReader.close();
			return line;
			
			// If there's an issue read from the file
			// we must report it to the user
		    } catch(IOException e)
		    {
		    	System.out.println("Error reading file!");
		    }
	     
		// If the file is not found we must
		// report it to the user
	    } catch(FileNotFoundException e)
	    {
	    	System.out.println("File not found or does not exist!");
	    }
	
		// Should never get here, keeps the compiler happy
		return line;
    }

    /*
   FUNCTION:   Encrypt(String text, BigInteger n)

   ARGUMENTS:  Takes the text string and the public key for encryption

   RETURNS:    Nothing

   NOTES:      Will produce an output file of the encrypted text
    */
    public void Encrypt(String text, BigInteger n)
    {
    	int pos = 1;           // Position of the char we are at (non-zero based)
    	String toEncrypt = ""; // Blank string to hold 50 chars

    	// Try to create and write to the file
    	try
    	{
    		File outFile = new File("Encrypted.txt");      // Create the outFile to hold encryption
    		PrintWriter writer = new PrintWriter(outFile); // New writer to write to outFile
	
    		// While we haven't read through the entire file
    		for(int i = pos-1; i < text.length(); i++)
		    {
			
    			toEncrypt = toEncrypt + (int)text.charAt(i); // Add int value of char to be encrypted
			
    			// If we are at a multiple of 50 chars
    			if(pos % 50 == 0)
			    {
    				final BigInteger TWO = new BigInteger("2");       // constant "2"
    				BigInteger plaintext = new BigInteger(toEncrypt); // make a BigInteger from the string of ints 
    				BigInteger ciphertext = plaintext.modPow(TWO, n); // c = m^2 mod n (Encryption formula)
				
    				// Write the ciphertext to the file and clear the string 
    				// for the next 50 chars
    				writer.println(ciphertext);
    				toEncrypt = "";
			    }
			// Go to the next char in the plaintext 
    			pos++;
		    }
    		writer.close();
    		System.out.println("Successfully encrypted plaintext!");

		// As a precaution, it's possible for the file to not be 
		// created, in this case we report it to the user
	    } catch(FileNotFoundException e)
	    {
	    	System.out.print("File not found or does not exist!");
	    }
    }

    /*
   PROGRAM:   Rabin Cryptosystem Encryption
   AUTHOR:    Nicholas Cantner
   DUE DATE:  May 23rd, 2016, 6pm

   FUNCTION:  The purpose of this program is to encrypt plaintext
              using the encryption formula for the rabin cryptosystem.
	      We read in a plaintext file "Input.txt" and a file
	      "Public.txt" which contains the public key n.
	      We then break the plaintext in groupings of 50 and convert 
	      it into their ascii integer values producing plaintext
	      value m. We need encrypt m using the formula
	      c = m^2 mod n. We then write the encrypted values to
	      the file "Encrypted.txt"

   INPUT:     2 files, "Input.txt", containing the plaintext and
              "Public.txt", containing the public key used for encryption

   OUTPUT:    Will output the text file "Encrypted.txt" containing the
              encrypted plaintext values

   NOTES:     Main calls the object constructor which will read the text file
              and the public key, then encrypt it and make the correct output
              
    */
    public static void main(String args[])
    {
    	Encrypt encryption = new Encrypt();
    }

}