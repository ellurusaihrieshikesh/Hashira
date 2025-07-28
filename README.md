Hashira Shamir Secret Sharing
=====================

This project implements Shamir's Secret Sharing algorithm in Java. It allows you to reconstruct a secret from a set of shares, each stored in JSON files with values in various bases.

Project Structure
-----------------
- src/ShamirSecret.java: Main Java source file implementing secret reconstruction.
- data1.json, data2.json: Example share files with secret shares in different bases.
- lib/json-20210307.jar: JSON library dependency.
- .vscode/: VS Code configuration for running the project.

How It Works
------------
- Each data*.json file contains:
  - "keys": Number of shares (n) and threshold (k).
  - Share entries: Each has an x value (the key), a base, and a value (the share in that base).
- The program reads the JSON, parses each share, and reconstructs the secret using Lagrange interpolation.

Requirements
------------
- Java 8 or higher
- org.json library (already included in lib/)

How to Run
----------
1. Compile:
   javac -cp lib/json-20210307.jar -d bin src/ShamirSecret.java

2. Run:
   java -cp "bin;lib/json-20210307.jar" ShamirSecret
   (Use ":" instead of ";" on Unix-based systems.)

3. The program will output the reconstructed secrets for data1.json and data2.json.

Customization
-------------
- To use your own shares, create a JSON file in the same format as data1.json or data2.json.
- Adjust the "n" and "k" values as needed.

License
-------
This project is for educational purposes.
