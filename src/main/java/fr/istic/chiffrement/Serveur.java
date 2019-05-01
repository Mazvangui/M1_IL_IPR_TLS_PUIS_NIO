package fr.istic.chiffrement;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Date;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Serveur {
    static int PORT = 9999;
    // NE FAITES PAS CA EN PROD :
    private static char[] PASSWORD = "123456".toCharArray();

    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        //Choisir l'un ou l'autre en fonction de la question :
        ServerSocket server = creerSocketTLS(); // creerSocketClassique();
        System.out.printf("temps écoulé : %d ms %n", System.currentTimeMillis() - time);
        readPingSendPong(server);
    }

    public static ServerSocket creerSocketClassique() throws Exception {
        //TODO : retourner une socket classique qui écoute sur le port 9999
        return null;
    }

    /**
     * Créer une ServerSocket TLS qui écoute sur le port 9999
     * 
     * @return une socket classique
     * @throws Exception
     */
    public static ServerSocket creerSocketTLS() throws Exception {
        
        
    	
    	// Créer une ServerSocket TLS qui écoute sur le port 9999, avec les
    	// caractéristiques suivantes :
        // + protocole utilisé : TLS
        // + format de clef PKCS12
        // + mot de passe store et clef : variable PASSWORD plus haut.
        // + keystore : serveurstore.keys
    	SSLContext context = SSLContext.getInstance("TLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("PKCS12");

        ks.load(new FileInputStream("serveurstore.keys"), PASSWORD);
        kmf.init(ks, PASSWORD);
        context.init(kmf.getKeyManagers(), null, null);
        Arrays.fill(PASSWORD, '0'); // on efface pour qu'il ne traine pas en mémoire.

        SSLServerSocketFactory factory = context.getServerSocketFactory();
        SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(PORT);
        return server; // Retourner la socket correctement configurée
    }

    public static void readPingSendPong(ServerSocket server) throws Exception {
        //accepter une connexion
        //lire PING
        //écrire PONG vers le client
    	
    	SSLSocket sslsocket = (SSLSocket) server.accept();
        InputStream in = sslsocket.getInputStream();
        OutputStream out = sslsocket.getOutputStream();
        System.out.println("in :"+in.toString());
        System.out.println("out :"+out.toString());
        out.write("PONG".getBytes());
        in.close();out.close();sslsocket.close();
    }
}