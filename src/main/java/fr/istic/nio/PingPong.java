package fr.istic.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PingPong {

	private static final int PORT_NUMBER = 9999;
	private static final int BUFFER_SIZE = 48;

	public static boolean isNum(String strNum) {
	    boolean ret = true;
	    try {

	        Double.parseDouble(strNum);

	    }catch (NumberFormatException e) {
	        ret = false;
	    }
	    return ret;
	}
	
	public static void main(String[] args) throws IOException {
	    ServerSocketChannel server = ServerSocketChannel.open();
	    server.socket().bind(new InetSocketAddress(PORT_NUMBER));
	    server.socket().setReuseAddress(true);
	    server.configureBlocking(false);
	    Selector selector = Selector.open();
	    server.register(selector, SelectionKey.OP_ACCEPT);

	    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
	    
	    while (true) {
	        int channelCount = selector.select();
	        if (channelCount > 0) {
	            Set<SelectionKey> keys = selector.selectedKeys();
	            Iterator<SelectionKey> iterator = keys.iterator();
	            while (iterator.hasNext()) {
	                SelectionKey key = iterator.next();
	                iterator.remove();
	                if (!key.isValid()) {
	                    continue;
	                }
	                if (key.isAcceptable()) {
	                    SocketChannel client = server.accept();
	                    client.configureBlocking(false);
	                    ByteBuffer bufferClient = ByteBuffer.allocate(BUFFER_SIZE);
	                    client.register(key.selector(), SelectionKey.OP_READ,bufferClient);
	                } else if (key.isReadable()) {
	                    SocketChannel client = (SocketChannel) key.channel();
	                    if (client.read(buffer) < 0) {
	                        key.cancel();
	                        client.close();
	                    } else {
	                        buffer.flip();
	                        byte[] bytes = new byte[buffer.remaining()];
	                        buffer.get(bytes, 0, buffer.remaining());
	                        String cmd = new String(bytes);
	                        String number = cmd.substring(5, cmd.length());
	                        buffer.clear();
	                        if (cmd.startsWith("PING")) {
	                            key.interestOps(SelectionKey.OP_WRITE);
	                            if(isNum(number)) {
		                        	String respond = "PONG "+number;
		                        	ByteBuffer tmp = (ByteBuffer) key.attachment();
		                        	tmp.put(respond.getBytes());
		                        }else {
		                        	System.out.println("please enter PING X with X like a number");
		                        }
	                        }
	                    }
	                }else if (key.isWritable()) {
	                	 SocketChannel client = (SocketChannel) key.channel();
	                	 ByteBuffer tmp = (ByteBuffer) key.attachment();
	                     tmp.flip();
	                	 client.write(tmp);
	                	 tmp.clear();
	                	 key.interestOps(SelectionKey.OP_READ);
                    }
	            }
	        }
	    }
	}
}
