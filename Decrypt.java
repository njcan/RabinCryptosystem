import java.math.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class Decrypt
{
    /*
   FUNCTION:   Decrypt()

   ARGUMENTS:  Takes no arguments

   RETURNS:    Returns nothing

   NOTES:      Constructor for decrypting. Calls the necessary functions
               to decrypt the text file with the keys
    */
    Decrypt()
    {
    	BigInteger p = new BigInteger(GetP());
    	BigInteger q = new BigInteger(GetQ()); 
    	ComputeRoots(p, q);
    }
    
    /*
   FUNCTION:   ComputeRoots(BigInteger p, BigInteger q)

   ARGUMENTS:  Takes p & q as arugments, which are private, large, primes

   RETURNS:    Returns nothing

   NOTES:      A multitude happens in this function. First we read in p and q for computation.
               We also open the "Encrypted.txt" file for reading so we can decrypt the ciphertext.
	       We invoke xGCD, or Euclidean's Extended Algorithm to solve roots needed for computation.
	       We also calculate 4 roots as Rabin is a 4-1 function using the chinese remainder theorem.
    */
    private void ComputeRoots(BigInteger p, BigInteger q)
    {
	String inFile = "Encrypted.txt"; // File to read from
	String line = "";                // Blank string to store line
	BigInteger[] roots;              // Holds solution to yp*p + yq*q = 1
	roots = new BigInteger[2];       // Holds solution to yp*p + yq*q = 1
	roots = xGCD(p, q);              // Solves yp*p + yq*q = 1
	BigInteger n = p.multiply(q);    // Recalculate n instead of reading it 
	int counter = 1;                 // Counter to track line written

	// Try opening/creating the files
	try
	    {
		FileReader reader = new FileReader(inFile);          // Create a filereader
		BufferedReader bReader = new BufferedReader(reader); // Wrap the file reader so we may call readLine()
		File outFile = new File("Decrypted.txt");            // File to be written to
                PrintWriter writer = new PrintWriter(outFile);       // Instantiate PrinterWriter so we can write to the file

		// Try reading lines
		try
		    {
			// While there is still ciphertext to decrypt... Compute roots
			// based on the Chinese Remainder Theorem [crt]
			while((line = bReader.readLine()) != null)
			    {
				final BigInteger ONE = new BigInteger("1");   // BigInteger constant for computation
				final BigInteger FOUR  = new BigInteger("4"); // BigInteger constant for computation

				BigInteger c = new BigInteger(line);       // Ciphertext value read in 
				BigInteger ep = (p.add(ONE)).divide(FOUR); // (p+1)/4  [shortcut as p&q are congruent to 3mod4]
				BigInteger eq = (q.add(ONE)).divide(FOUR); // (q+1)/4  [shortcut as p&q are congruent to 3mod4]

				BigInteger mp = c.modPow(ep, p); // c^(p+1)/4 mod p
				BigInteger mq = c.modPow(eq, q); // c^(q+1)/4 mod q

				BigInteger yp = roots[0]; // yp*p + yq*q = 1
				BigInteger yq = roots[1]; // yp*p + yq*q = 1

				BigInteger pp = ((yp.multiply(p)).multiply(mq)).add(((yq.multiply(q)).multiply(mp)));      // yp*p*mq + yq*q*mp
				BigInteger qq = ((yp.multiply(p)).multiply(mq)).subtract(((yq.multiply(q)).multiply(mp))); // yp*p*mq - yq*q*mp
				
				BigInteger r  = pp.mod(n);     // value of root 1 = pp % n [crt]
				BigInteger nr = n.subtract(r); // value of root 2 = n - r  [crt]
				BigInteger s  = qq.mod(n);     // value of root 3 = qq % n [crt]
				BigInteger ns = n.subtract(s); // value of root 4 = n - s  [crt]

				// Print all 4 values to the output file
				writer.println("Possible plaintext values for line " + counter + " - ");
				writer.println(r);
				writer.println(nr);
				writer.println(s);
				writer.println(ns);
				writer.println("");
				
				// Increment the counter
				counter++;
			    }

			// Close the writer
			writer.close();
			System.out.println("Successfully decrypted the ciphertext!");

			// This can fail, but shouldn't due to manually creating the file
			// Having this catch keeps the compiler happy
		    } catch(FileNotFoundException e)
		    {
			System.out.println("File not found or error creating file!");
		    } catch(IOException e)
                    {
                        System.out.println("File not found or error creating file!");
                    }

		// This can fail, but shouldn't due to manually creating the file
		// Having this catch keeps the compiler happy
	    } catch(FileNotFoundException e)
            {
                System.out.println("File not found or error creating file!");
            } 
    }
    
    /*
   FUNCTION:   public static BigInteger[] xGCD(BigInteger p, BigInteger q)

   ARGUMENTS:  Takes 2 large primes, p and q

   RETURNS:    Returns array of BigIntegers holding the solution to the
               equation yp*p + yq*q = 1

   NOTES:      Extended Euclidean's Algorithm 
    */
    private static BigInteger[] xGCD(BigInteger p, BigInteger q)
    {
    	BigInteger x = BigInteger.ZERO;  //  x = 0
        BigInteger px = BigInteger.ONE;  // px = 1
        BigInteger y = BigInteger.ONE;   //  y = 1
        BigInteger py = BigInteger.ZERO; // py = 0 

	// As long as q doesn't equal zero
        while(!q.equals(BigInteger.ZERO))
	    {
		// Compute quotient and remainder
		BigInteger[] qr = p.divideAndRemainder(q); // dive and compute remainder
		BigInteger quotient = qr[0];               // quotient stored in qr[0]

		BigInteger temp = p; // Need to hold previous value for storage
		p = q;               // swap
		q = qr[1];           // q takes on remainder

		temp = x;                               // swap
		x = px.subtract(quotient.multiply(x));  // compute next x
		px = temp;                              // swap 

		temp = y;                               // swap
		y = py.subtract(quotient.multiply(y));  // compute next y
		py = temp;                              // swap
	    }

	BigInteger[] roots;        // Store values as roots
	roots = new BigInteger[2]; // Store values as roots
	roots[0] = px;             // Indice 0 is yp
	roots[1] = py;             // Indice 1 is yq
	
	return roots; 
    }
    
    /*
   FUNCTION:   GetP()

   ARGUMENTS:  Takes no arguments

   RETURNS:    Returns the private key P as a String

   NOTES:      Reads the first line of "Private.txt" as a String
    */
    private String GetP()
    {
        String inFile = "Private.txt"; // The file must be named
        String line = "";              // Empty string to read into

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
   FUNCTION:   GetQ()

   ARGUMENTS:  Takes no arguments

   RETURNS:    Returns the private key Q as a String

   NOTES:      Reads the second line of "Private.txt" as a String
    */
    private String GetQ()
    {
        String inFile = "Private.txt"; // The file must be named
        String line = "";              // Empty string to read into

        // Try to instantiate the readers
        try
            {
                FileReader reader = new FileReader(inFile);          // Create a file reader for the input
                BufferedReader bReader = new BufferedReader(reader); // Wrap the file reader so we may call readLine()

                // Try to read from the file
                try
                    {
                        // Read in the second line
                		bReader.readLine();
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
   PROGRAM:   Rabin Cryptosystem Decryption
   AUTHOR:    Nicholas Cantner
   DUE DATE:  May 23rd, 2016, 6pm

   FUNCTION: The purpose of this program is to decrypt the ciphertext
             into 4 possible plaintexts using the chinese remainder 
	      	 theorem and other algorithms such as euclidean's algorithm.

   INPUT:   3 files
            "Encrypted.txt" - text file holding the encrypted values
	      	"Public.txt" - text file holding the public key
	      	"Private.txt" - text file holding the private key

   OUTPUT:    Will output the text file "Decrypted.txt" containing the four
              possible decryptions for each ciphertext. 

   NOTES:     Main calls the object constructor which will read the text
              files, get the keys, and perform the decryption, writing
	      the plaintext values to "Decrypted.txt"

    */
    public static void main(String args[])
    {
        Decrypt decryption = new Decrypt();
    }

}