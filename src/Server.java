import java.io.*;
import java.net.*;
import  java.awt.*;
import  java.awt.event.*;
import  javax.swing.*;


// public server (public computer)
//JFrame -- GuI

public class Server extends JFrame{

    // JTextField is a one-line text field, whereas a JTextArea can span over multiple lines.
    private JTextField userText;
    private JTextArea  chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private ServerSocket server;
    // socket --means connections between computers
    private Socket connection;

    //constructor
  public Server(){

      super("Fiona Instant Messager");
      userText = new JTextField();
      // before connections no need to type something
      userText.setEditable(false);
      // hit the enter send message
      userText.addActionListener(
              new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                      sendMessage(e.getActionCommand());
                      // after sending out the message, blank it
                      userText.setText("");
                  }
              }
      );
      //ADD TO THE SCREEN
      add(userText, BorderLayout.NORTH);
      chatWindow = new JTextArea();
      add(new JScrollPane(chatWindow));
      setSize(300,150);
      setVisible(true);
  }

// set up and run the server

    public void startRunning(){
      try{
          server = new ServerSocket(6789,100); // where aka port (which application) you want to connect, how many ppl can wait
          //run forever
          while(true){
              try{
                    //connect and have conversation
                  waitForConnection();
                  setupStreams();
                  whileChatting();

              }catch (EOFException eofException){
                  showMessage("\n Server ended the connection");
              }finally {
                  colseCrap();

              }
          }
      }catch (IOException ioException){
          ioException.printStackTrace();
      }
    }



    //waiting for connection, then display connection information
private void waitForConnection() throws IOException{
     showMessage("Waiting for some to connect...\n");
     //set up socket (once the server connect, the socket is made)
    connection = server.accept();
    showMessage("Now connected to "+ connection.getInetAddress().getHostName()); //gethoset --convert to the strings
}
// get stream to send and receive data
private void setupStreams() throws IOException{
    output = new ObjectOutputStream(connection.getOutputStream());
    output.flush();//flush the data left over in the buffer
    input = new ObjectInputStream(connection.getInputStream());
    showMessage("\n Streams are now setup!\n");

}
//during the chat conversation
private void whileChatting() throws IOException{
  String message = "You are now connected!";
  showMessage(message);
  //let user typing
    ableToType(true);

    do{
      try{
          message =(String) input.readObject(); //user read as object, what message come to us as strings
          showMessage("\n" + message);
      }catch (ClassNotFoundException classnotfoundException){
          showMessage("\n idk wtf that user send!");
      }
    }while(!message.equals("CLIENT - END"));//user send END, the conversation stop

}

//close streams and sockets after you are done
    private void colseCrap(){
        showMessage("\n Closing connections..\n");
        ableToType(false);
        try{
             output.close();
             input.close();
             connection.close();

        }catch (IOException ioExcetion){
            ioExcetion.printStackTrace();
        }

    }
//send a message to client

    private void  sendMessage(String message){
        try{
          output.writeObject("SEVER -  " + message);
          output.flush();
          showMessage("\n SEVER - "+ message);

        }catch (IOException ioException){
         chatWindow.append("\n ERROR: I CANT SEND THAT MESSAGE");
        }

    }
    //update the GUI --the chatwindow

    private void showMessage(final String text){
        //allow to upset the thread that update the part of the GUI
        SwingUtilities.invokeLater(
                new Runnable(){
                    @Override
                    public void run() {
                        chatWindow.append(text);
                    }
                }
        );

    }

    //able to type in the text box

    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
                new Runnable(){

                    public void run() {
                        userText.setEditable(tof);
                    }
                }
        );

    }









}