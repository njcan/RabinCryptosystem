import java.math.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class KeyGenerator
{
    private BigInteger q; // BigInteger q to hold the prime that is q
    private BigInteger p; // BigInteger p to hold the prime that is p
    public  BigInteger n; // BigInteger n to hold the value of p*q

   /*
   FUNCTION:   KeyGenerator()

   ARGUMENTS:  Takes no arguments

   RETURNS:    Returns nothing

   NOTES:      Constructor for the key generator. Calls the necessary functions
               to generate the keys and write them to their respective files
   */
    KeyGenerator()
    {
    	p = GeneratePrime(); 				// Generate p 
    	System.out.println("Generated private key P with length: " + p.toString().length());
    	
    	q = GeneratePrime(); 				// Generate q
    	System.out.println("Generated private key Q with length: " + q.toString().length());
    	
    	n = p.multiply(q);   				// Generate n
    	System.out.println("Generated public key N with length: " + n.toString().length());
	
    	CreatePublicKeyFile(n);		// Write to file
    	CreatePrivateKeyFile(p, q);	// Write to file
    }

   /*
   FUNCTION:   private boolean Is3mod4(BigInteger prime)

   ARGUMENTS:  Takes a prime to be tested 

   RETURNS:    Returns true if the prime is congruent to 3mod4
               Returns false if the prime is not congruent to 3mod4

   NOTES:      If it fails the if statement, it defaults to false
   */
    private boolean Is3mod4(BigInteger prime)
    {
    	final BigInteger ZERO = new BigInteger("0");  // Constant BigInteger ZERO
    	final BigInteger THREE = new BigInteger("3"); // Constant BigInteger THREE
    	final BigInteger FOUR = new BigInteger("4");  // Constant BigInteger FOUR
	
    	// To test for congruency, we must test if ((prime - 3) mod 4 = 0)
    	if((((prime.subtract(THREE)).mod(FOUR)).equals(ZERO)))
	    {
    		return true;
	    }
    	return false;
    }

   /*
   FUNCTION:   private BigInteger GeneratePrime()

   ARGUMENTS:  Takes no arguments

   RETURNS:    Returns a BigInteger congruent to 3mod4 that is prime

   NOTES:      Modifying length modifies the length of the key generated
   */
    private BigInteger GeneratePrime()
    {

    	final int LENGTH = 1024;                   	// Length of the key we want produced 
    	Random rnd = new Random();               	// Instantiate random number generator 
    	BigInteger prime = new BigInteger("13"); 	// The prime to be returned, starts at 13 to fail the initial congruency test

    	// Loop while the generated primes are
    	// NOT congruent to 3 mod 4
    	while(!(Is3mod4(prime)))
    	{
    		StringBuilder sb = new StringBuilder(LENGTH); // Generate a string builder of fixed length LENGTH
		
    		// For each digit in the speciffied length we want to generate
    		// a random digit and append it
    		for(int i = 0; i < LENGTH; i++)
    		{
    			// On the first digit, if we generate a zero, our key will be of [LENGTH - 1] length
    			// which isn't what we want
    			if(i == 0)
    			{
    				sb.append((char)('0' + (rnd.nextInt(9) + 1)));
			    }
			// We can have a zero for digit indices 1 - [LENGTH - 1]
    			else 
			    {
    				sb.append((char)('0' + (rnd.nextInt(10))));
			    }
		    }

    		String number = sb.toString();  // Generate a string and convert the sb to a string
    		prime = new BigInteger(number); // Invoke BigInteger constructor for making a BigInteger from a string 

    		// Find the next prime AFTER the BigInteger we've created
    		prime = prime.nextProbablePrime();
	    }
    	// Return the prime for testing at the beginning of the loop 
    	return prime;
    }

    /*
   FUNCTION:   public void CreatePublicKeyFile(BigInteger n)

   ARGUMENTS:  BigInteger n, which will be written to a file

   RETURNS:    Void function, returns nothing, but does create an output file

   NOTES:      This function creates and writes the public keys to the file
               "public.txt"
    */
    public void CreatePublicKeyFile(BigInteger n)
    {
	// Try to create a new file and write the results to it
	try
	{
		File outFile = new File("Public.txt");         // File to be written to
		PrintWriter writer = new PrintWriter(outFile); // Instantiate PrinterWriter so we can write to the file
		writer.println(n);
		writer.close();
	      // This can fail, but shouldn't due to manually creating the file
	      // Having this catch keeps the compiler happy
	    } catch(FileNotFoundException e)
	    {
	    	System.out.println("File not found or error creating file!");
	    }
    }

    /*
   FUNCTION:   public void CreatePrivateKeyFile(BigInteger p, BigInteger q)

   ARGUMENTS:  BigInteger's p and q, containing the 2 large primes to be written

   RETURNS:    Void function, returns nothing, but does create an output file

   NOTES:      This function creates and writes the private keys to the file 
               "private.txt"
    */
    public void CreatePrivateKeyFile(BigInteger p, BigInteger q)
    {
	// Try to create a new file and write the results to it
	try
	{
		File outFile = new File("Private.txt");        // File to be written to
		PrintWriter writer = new PrintWriter(outFile); // Instantiate PrintWriter so we can write to the file
		writer.println(p);
		writer.println(q);
		writer.close();
	      // This can fail, but shouldn't due to manually creating the file
	      // Having this catch keeps the compiler happy
	    } catch(FileNotFoundException e)
	    {
	    	System.out.println("File not found or error creating file!");
	    }
    }

   /*
   PROGRAM:   Rabin Cryptosystem KeyGenerator
   AUTHOR:    Nicholas Cantner
   DUE DATE:  May 23rd, 2016, 6pm

   FUNCTION:  The purpose of this program is to produce keys for the
              Rabin Cryptosystem. Namely 2 large primes p and q whose
              length exceeds 25 digits. By multiplying p and q, we generate
              a public key, n, and write p & q and to the file "private.txt"
              and n to the file "public.txt"

   INPUT:     No external input required to run this program.

   OUTPUT:    Will output two text files, the first "public.txt" 
              which holds n, the public key, and "private.txt"
              which holds p and q, the private keys.

   NOTES:     Main calls the object constructor which will generate
              all three keys, p, q, and n, and then write them to the
	      correct files.
   */
    public static void main(String args[])
    {
    	KeyGenerator generateKeys = new KeyGenerator();
    }
    
}