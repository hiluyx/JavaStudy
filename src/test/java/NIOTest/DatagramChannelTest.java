package NIOTest;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class DatagramChannelTest {
    @Test
    public void server() throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(8989));
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] b;
        while (true) {
            // 清空Buffer
            buffer.clear();
            // 接受客户端发送数据
            SocketAddress socketAddress = channel.receive(buffer);
            if (socketAddress != null) {
                int position = buffer.position();
                b = new byte[position];
                buffer.flip();
                for (int i = 0; i < position; ++i) {
                    b[i] = buffer.get();
                }
                System.out.println("receive remote " + socketAddress.toString() + ":" + new String(b, StandardCharsets.UTF_8));
                //接收到消息后给发送方回应
                sendReback(socketAddress, channel);
            }
        }
    }
    public static void sendReback(SocketAddress socketAddress, DatagramChannel datagramChannel) throws IOException {
        String message = "I has receive your message";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        datagramChannel.send(buffer, socketAddress);
    }
    @Test
    public void client() throws IOException, InterruptedException {
        final DatagramChannel channel = DatagramChannel.open();
        //接收消息线程
        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte[] b;
            while (true) {
                buffer.clear();
                SocketAddress socketAddress = null;
                try {
                    socketAddress = channel.receive(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (socketAddress != null) {
                    int position = buffer.position();
                    b = new byte[position];
                    buffer.flip();
                    for (int i = 0; i < position; ++i) {
                        b[i] = buffer.get();
                    }
                    System.out.println("receive remote " + socketAddress.toString() + ":" + new String(b, StandardCharsets.UTF_8));
                }
            }
        }).start();

        //发送控制台输入消息
        while (true) {
            String next = "Hello I'm Lucy.";
            try {
                sendMessage(channel, next);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(2000);
        }
    }
    public static void sendMessage(DatagramChannel channel, String mes) throws IOException {
        if (mes == null || mes.isEmpty()) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(mes.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        channel.send(buffer, new InetSocketAddress("localhost", 8989));
    }
}
