import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 8090));

        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    int byteCount = socketChannel.read(inputBuffer);
                    if (byteCount == -1) break;

                    final String msg = new String(inputBuffer.array(), 0, byteCount,
                            StandardCharsets.UTF_8);
                    String[] phrase = msg.split(" ");
                    StringBuilder sb = new StringBuilder();
                    for (String s : phrase) {
                        sb.append(s);
                    }
                    socketChannel.write(ByteBuffer.wrap((sb + " ").getBytes(StandardCharsets.UTF_8)));
                    inputBuffer.clear();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
