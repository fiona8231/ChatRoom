import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;

    //constructor

    public Client(String host){

        super("Client mofo!");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                       sendData(e.getActionCommand());
                       userText.setText("");
                    }
                }
        );
     add(userText, BorderLayout.NORTH);
     chatWindow = new JTextArea();
     add(new JScrollPane(chatWindow), BorderLayout.CENTER);
     setSize(300,150);
     setVisible(true);
    }

    //connect to server

    public void startRuunig(){
        try{
            connectionToServer();
            setupStreams();
            whileChatting();
        }catch (EOFException eofException){
            showMessage("Client terminate conncetion");
        }catch (IOException ioExcetion){
            ioExcetion.printStackTrace();
        }finally {
            closeCrap();
        }
    }

    private void connectionToServer() throws IOException {
        showMessage("Attempthing connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to :" + connection.getInetAddress().getHostName());
    }

    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n your streams are now good to go!");
    }

    private void whileChatting() throws IOException{
        ableToType(true);

        do{
            try{
                 message = (String) input.readObject();
                showMessage("\n" + message);

            }catch (ClassNotFoundException classnotfoundException){
                showMessage("\n I dont know that object type");
            }

        }while(!message.equals("SEVER - END"));
    }

    private void closeCrap(){
        showMessage("\n closing crap down...");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();

        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

   private void sendData(String message){
      try{
          output.writeObject("CLIENT - "+ message);
          output.flush();
          showMessage("\n CLIENT - " + message);

      }catch (IOException ioException) {
          chatWindow.append("\n something messed up sending message hoss!");
      }
   }
   private void showMessage(final String m){
       SwingUtilities.invokeLater(
               new Runnable() {
                   @Override
                   public void run() {
                       chatWindow.append(m);
                   }
               }
       );

   }
   private void ableToType(final boolean tof) {
       SwingUtilities.invokeLater(
               new Runnable() {
                   @Override
                   public void run() {
                       userText.setEditable(tof);
                   }
               }
       );
   }
}


