package NIOTest;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class TestServer {
    private static final int BUF_SIZE = 1024;
    private static final int PORT = 8080;
    private static final int TIMEOUT = 3000;

    @Test
    public void server(){
        Selector selector = null;
        ServerSocketChannel ssc = null;
        try {
            selector = Selector.open();
            ssc= ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(PORT));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while(true) {
                //阻塞监听
                if(selector.select(TIMEOUT) == 0) {
                    System.out.println("==");
                    continue;
                }
                //接受事件
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                //分配处理逻辑，可以启用线程池处理相同的事件，多级事件队列
                while(iter.hasNext()){
                    SelectionKey key = iter.next();
                    if(key.isAcceptable()) {
                        handleAccept(key);
                    }
                    if(key.isReadable()) {
                        handleRead(key);
                    }
                    if(key.isWritable() && key.isValid()) {
                        handleWrite(key);
                    }
                    if(key.isConnectable()) {
                        System.out.println("isConnectable = true");
                    }
                    iter.remove();
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(selector!=null) {
                    selector.close();
                }
                if(ssc!=null) {
                    ssc.close();
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void handleAccept(SelectionKey key) throws IOException{
        ServerSocketChannel ssChannel = (ServerSocketChannel)key.channel();
        SocketChannel sc = ssChannel.accept();
        sc.configureBlocking(false);
        sc.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocateDirect(BUF_SIZE));
    }

    public static void handleRead(SelectionKey key) throws IOException{
        SocketChannel sc = (SocketChannel)key.channel();
        ByteBuffer buf = (ByteBuffer)key.attachment();
        long bytesRead = sc.read(buf);
        while(bytesRead>0){
            buf.flip();
            while(buf.hasRemaining()){
                System.out.print((char)buf.get());
            }
            System.out.println();
            buf.clear();
            bytesRead = sc.read(buf);
        }
        if(bytesRead == -1){
            sc.close();
        }
    }

    public static void handleWrite(SelectionKey key) throws IOException{
        ByteBuffer buf = (ByteBuffer)key.attachment();
        buf.flip();
        SocketChannel sc = (SocketChannel) key.channel();
        while(buf.hasRemaining()){
            sc.write(buf);
        }
        buf.compact();
    }
}