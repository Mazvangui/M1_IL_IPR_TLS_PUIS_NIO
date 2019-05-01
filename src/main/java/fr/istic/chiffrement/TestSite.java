package fr.istic.chiffrement;

import java.io.*;
import javax.net.ssl.*;

public class TestSite {

	public static void main(String[] args) {
		connecteEtAffiche("www.google.fr", 443);
		connecteEtAffiche("nextinpact.com", 443);
		connecteEtAffiche("istic.univ-rennes1.fr", 443);

	}

	//www.google.fr sur le port 443
	//nextinpact.com sur le port 443
	//istic.univ-rennes1.fr sur 443
	public static void connecteEtAffiche(String host, int port) {
        // Creation de la socket
		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		try {
			SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port);
			// Choix de la cipher suite si nécessaire (sinon laisser defaut).
			// Connexion
			InputStream in = sslsocket.getInputStream();
			OutputStream out = sslsocket.getOutputStream();
			out.write(1);
			while (in.available() > 0) {
			    System.out.print(in.read());
			}
			System.out.println("Connection sécurisée active");
	        // Affichage des infos demandées :
			SSLSession session=sslsocket.getSession();
			if (session != null) {
			    System.out.println("Session: " + session);
				System.out.println("protocol utilisé: "+session.getProtocol());
			    System.out.println("Cypher suite: "+session.getCipherSuite());
			    System.out.println("Certificat serveur: "+session.getPeerCertificates().toString());
			    System.out.println("Local certificates: " + session.getLocalCertificates());
			    System.out.println("Local principal: " + session.getLocalPrincipal());
			    SSLSessionContext context=session.getSessionContext();
			    if (context != null) {
			      System.out.println("Session context: " + context);
			    }
			  }
			// Fermeture de la socket
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
}
