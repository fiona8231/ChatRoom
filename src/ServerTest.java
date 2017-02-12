import javax.swing.JFrame;

public class ServerTest {
    public static void main(String[] args){
        Server yingni = new Server();
        yingni.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        yingni.startRunning();
    }
}