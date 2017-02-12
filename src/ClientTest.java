import javax.swing.JFrame;

public class ClientTest {
    public static void main(String[] args){
      Client xuyue = new Client("127.0.0.1"); // local host (my computer)
        xuyue.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        xuyue.startRuunig();



    }
}
