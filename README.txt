This is an example Rabin Cryptosystem complete with fully commented code. The programs are written in Java and utilize Java's BigNum package. This is my first time using Java and Java's BigNum package. 
Normally, the private keys are not revealed. In this example they are accessible so they user may view them to gain understanding about the system.
See https://en.wikipedia.org/wiki/Rabin_cryptosystem for more information.

1. Compile & run the KeyGenerator.java file. It will output two .txt files, the first called "Public.txt" and the second "Private.txt". Keep them as named.
2. Compile & run the Encrypt.java file. It will output a .txt file called "Encrypted.txt". Keep this as named.
3. Compile & run the Decrypt.java file. It will output a .txt file called "Decrypted.txt". Keep this as named. 

Notes: 
  – You must have a file called "Input.txt" to be read in by the program for encryption. 
  – Make sure the "Input.txt" file is in the same directory as the program.
  – Currently, it generates 2 private keys, p & q, where each are 1024 digits in length each. 
  – The private keys are then multiplied together to make the public key, which is 2048 digits in length.
