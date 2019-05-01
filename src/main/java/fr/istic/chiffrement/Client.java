package fr.istic.chiffrement;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Client {

    private static char[] PASSWORD = "654321".toCharArray();

    public static void main(String[] args) throws Exception {

        System.setProperty("javax.net.ssl.trustStoreType", "pkcs12");
        System.setProperty("javax.net.ssl.trustStore", "clientstore.keys");
        System.setProperty("javax.net.ssl.trustStorePassword", "654321");
        // Création d'une socket ssl client 
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 9999);
        //Creation des printwriter et buffered reader puis :
        BufferedReader in = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(sslsocket.getOutputStream()));
        out.write("PING");
        out.flush();
        System.out.println(in.readLine());
        
    }

}