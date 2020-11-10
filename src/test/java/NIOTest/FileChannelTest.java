package NIOTest;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class FileChannelTest {
//    @Test
//    public void test() throws IOException, URISyntaxException {
//        FileChannel fileChannel = FileChannel.open(
//               Path.of(this.getClass().getResource("1.txt").toURI()),
//                StandardOpenOption.READ);
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        fileChannel.read(buffer);
//        System.out.println(new String(buffer.array(), StandardCharsets.UTF_8));
//    }
    @Test
    public void classPathTest(){
        System.out.println("==================Class.getClassLoader.getResource=============");
        System.out.println(this.getClass().getClassLoader().getResource(""));
        System.out.println(this.getClass().getClassLoader().getResource("1.txt"));
        System.out.println(this.getClass().getClassLoader().getResource("NIOTest/1.txt"));
        System.out.println("=================Class.getResource================");
        System.out.println(this.getClass().getResource(""));
        System.out.println(this.getClass().getResource("/"));
        System.out.println(this.getClass().getResource("/1.txt"));
        System.out.println(this.getClass().getResource("1.txt"));
    }
}
