import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import javax.swing.*;

public class Window extends JFrame{


    private JPanel userScreen;
    private JTextField userNameInput, userMailInput, userPasswordInput, userInputBalance;
    private JButton acceptButton, returnButton, setNewBalance, drawBalance;
    private JLabel serverOutput, textA, textB, textC;

    public int userID, userBalance, userNewBalance;
    public boolean status = false;

    //CONSTRUCTOR
    public Window(){
        this.setVisible(true); //Para hacer visible la ventana, luego corres el programa y no pasa nada, entonces abre 300 veces el programa.
        this.setSize(600, 600); //Para modificar el ancho y alto de la ventana, se mueven con las var de la linea 21.
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //Para que se cierre completamente.
        this.setLayout(null); //No queremos ningun layout predeterminado.
        this.setLocation(0,0); //Para ubicar la ventana en algun lugar de la pantalla.
        this.setTitle("LOGGER");
       
        userScreen = new JPanel();
        userScreen.setBounds(0,0,600, 600);
        userScreen.setBackground(Color.WHITE);
        userScreen.setLayout(null);
        this.add(userScreen);

       LoginScreen();
    }

    public void LoginScreen(){
        userScreen.removeAll();

        textA = new JLabel("Nombre de Usuario:");
        textA.setBounds(225,275,400,25);
        userScreen.add(textA);

        textC = new JLabel("Contraseña:");
        textC.setBounds(225,320,400,25);
        userScreen.add(textC);

        userNameInput = new JTextField();
        userNameInput.setBounds(225,300,150,25);
        userScreen.add(userNameInput);

        userPasswordInput = new JTextField();
        userPasswordInput.setBounds(225,340,150,25);
        userScreen.add(userPasswordInput);

        acceptButton = new JButton("Iniciar Sesión");
        acceptButton.setBounds(236,380,125,25);
        userScreen.add(acceptButton);

        acceptButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                
                String isOkey = dbRegisteredUser(userNameInput.getText(), userPasswordInput.getText());

                if(isOkey == "check"){
                    userID = getUserID(userNameInput.getText(), userPasswordInput.getText());
                    mainScreen();
                }else{
                    serverOutput.setText("El usuario o contraseña es incorrecto!");
                }


            }
        });

        returnButton = new JButton("Registrarse");
        returnButton.setBounds(236,420,125,25);
        userScreen.add(returnButton);

        returnButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                SignUpScreen();
            }
        });

        serverOutput = new JLabel();
        serverOutput.setBounds(50,500,400,25);
        serverOutput.setForeground(Color.red);
        userScreen.add(serverOutput);

        if (status == true){
            serverOutput.setText("Usuario creado!");    
        }

        this.repaint();
    }

    public void SignUpScreen (){
        userScreen.removeAll();

        textA = new JLabel("Nombre de Usuario:");
        textA.setBounds(225,275,400,25);
        userScreen.add(textA);

        textB = new JLabel("Correo:");
        textB.setBounds(225,320,400,25);
        userScreen.add(textB);

        textC = new JLabel("Contraseña:");
        textC.setBounds(225,360,400,25);
        userScreen.add(textC);

        userNameInput = new JTextField();
        userNameInput.setBounds(225,300,150,25);
        userScreen.add(userNameInput);

        userMailInput = new JTextField();
        userMailInput.setBounds(225,340,150,25);
        userScreen.add(userMailInput);

        userPasswordInput = new JTextField();
        userPasswordInput.setBounds(225,380,150,25);
        userScreen.add(userPasswordInput);

        acceptButton = new JButton("Registrarse");
        acceptButton.setBounds(236,420,125,25);
        userScreen.add(acceptButton);

        acceptButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                
                String isOkey = dbRegisterUser(userNameInput.getText(), userMailInput.getText(), userPasswordInput.getText());

                if(isOkey == "newUserAdded"){
                    status = true;
                    LoginScreen();
                }else{
                    serverOutput.setText("El usuario ya existe!");
                }
            }
        });

        returnButton = new JButton("Ya tengo una cuenta");
        returnButton.setBounds(200,460,200,25);
        userScreen.add(returnButton);

        returnButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                status = false;
                LoginScreen();
            }
        });


        serverOutput = new JLabel();
        serverOutput.setBounds(50,500,400,25);
        serverOutput.setForeground(Color.red);
        userScreen.add(serverOutput);

        this.repaint();
    }

    public void mainScreen(){

        userScreen.removeAll();

        textA = new JLabel();
        textA.setBounds(50,150,400,25);
        userScreen.add(textA);

        textB = new JLabel();
        textB.setBounds(50,200,400,25);
        userScreen.add(textB);

        textC =  new JLabel("Que desea realizar?");
        textC.setBounds(400,300,400,25);
        userScreen.add(textC);

        userInputBalance = new JTextField("0");
        userInputBalance.setBounds(400,350,150,25);
        userScreen.add(userInputBalance);

        setNewBalance = new JButton("Agregar");
        setNewBalance.setBounds(400,400,150,25);
        userScreen.add(setNewBalance);
        setNewBalance.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                int midBalance = userBalance + Integer.parseInt(userInputBalance.getText());
                setNewUserBalance(userID, midBalance);
                mainScreen();
            }
            
        });

        drawBalance = new JButton("Sacar");
        drawBalance.setBounds(400,450,150,25);
        userScreen.add(drawBalance);

        drawBalance.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                int midBalance = userBalance - Integer.parseInt(userInputBalance.getText());
                setNewUserBalance(userID, midBalance);
                mainScreen();
            }
            
        });

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "");
            Statement stmt = con.createStatement();
            

            String serverChecked = "SELECT * FROM `users` WHERE `ID` = " + userID;

            //System.out.println(serverChecked);
            ResultSet rs = stmt.executeQuery(serverChecked);
            
            if(rs.next()){
                textA.setText("Bienvenido " + rs.getString(2));
                userBalance = rs.getInt(5);
                textB.setText("Balance: " + userBalance);
                if(rs.getInt(5) <= 0){
                    textB.setForeground(Color.red);
                }else{
                    textB.setForeground(Color.black);
                }
            }else{
                userID = 0;
            }

            con.close();
        } catch (Exception ef) {
            //TODO: handle exception
            System.out.println(ef);
        }   
        acceptButton = new JButton("Borrar usuario");
        acceptButton.setBounds(200,525,150,25);
        userScreen.add(acceptButton);
        
        // DELETE FROM `users` WHERE `ID`= 4; 
        acceptButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                setWarningMsg("No se puede revertir esta accion!");
                setWarningMsg("Gracias por usar LOGGER!");

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "");
                    Statement stmt = con.createStatement();
                    
                    String serverChecked = "SELECT * FROM `users` WHERE `ID` = " + userID;
                    String serverDelete = "DELETE FROM `users` WHERE `ID` = " + userID;
        
                    //System.out.println(serverChecked);
                    ResultSet rs = stmt.executeQuery(serverChecked);
                    
                    if(rs.next()){
                        stmt.executeUpdate(serverDelete);
                    }else{
                        userID = 0;
                    }
        
                    con.close();
                } catch (Exception ef) {
                    //TODO: handle exception
                    System.out.println(ef);
                }    

                userID = 0;
                status = false;
                LoginScreen();
            }
        });

        returnButton = new JButton("Cerrar sesión");
        returnButton.setBounds(400,525,150,25);
        userScreen.add(returnButton);

        returnButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                userID = 0;
                status = false;
                LoginScreen();
            }
        });
    
        serverOutput = new JLabel();
        serverOutput.setBounds(25,400,400,25);
        userScreen.add(serverOutput);

        repaint();
    }

    private String getMD5(String userInput){

        byte[] msg = userInput.getBytes();
        byte[] hash = null;

        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            hash = md.digest(msg);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder strBuilder = new StringBuilder();
        for(byte b:hash)
        {
            strBuilder.append(String.format("%02x", b));
        }
        String hashlink = strBuilder.toString();
        
        return hashlink;
    }

    public String dbRegisteredUser(String userName, String password){

        String verified = "null";

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "");
                    Statement stmt = con.createStatement();

                    String serverChecked = "select * from users where Username = '" + userName + "' and Userpass = '" + getMD5(password) + "'";

                    //System.out.println(serverChecked);
                    ResultSet rs = stmt.executeQuery(serverChecked);
                    if(rs.next()){
                        verified = "check";
                    }else{
                        return verified;
                    }
                    con.close();
                } catch (Exception ef) {
                    //TODO: handle exception
                    System.out.println(ef);
                }       

        return verified;
    }

    public String dbRegisterUser(String userName, String userMail, String password){
        String verified = "null";

        

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "");
                    Statement stmt = con.createStatement();

                    String serverChecked = "select * from users where Username = '" + userName + "' and Usermail = '" + userMail + "'";
                    String serverNewUser = "INSERT INTO `users` (`ID`, `Username`, `Usermail`, `Userpass`, `balance`) VALUES (NULL, '" + userName + "', '" + userMail + "', '" + getMD5(password) + "', '" + 0 +  "') ";

                    //System.out.println(serverChecked);
                    ResultSet rs = stmt.executeQuery(serverChecked);
                    


                    if(rs.next()){
                        verified = "null";
                    }else{
                        stmt.executeUpdate(serverNewUser);
                        verified = "newUserAdded";
                    }

                    con.close();
                } catch (Exception ef) {
                    //TODO: handle exception
                    System.out.println(ef);
                }       

        return verified;
    }

    public int getUserID(String userName, String password){
        int userID = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "");
            Statement stmt = con.createStatement();

            String serverChecked = "select * from users where Username = '" + userName + "' and Userpass = '" + getMD5(password) + "'";

            //System.out.println(serverChecked);
            ResultSet rs = stmt.executeQuery(serverChecked);
            


            while(rs.next()){
                userID = rs.getInt(1);
            }

            con.close();
        } catch (Exception ef) {
            //TODO: handle exception
            System.out.println(ef);
        }    


        return userID;
    }

    public static void setWarningMsg(String text){
        Toolkit.getDefaultToolkit().beep();
        JOptionPane optionPane = new JOptionPane(text,JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog("Warning!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    public void setNewUserBalance(int userID, int balance){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "");
            Statement stmt = con.createStatement();

            String serverUpdate = "UPDATE `users` SET `balance` = '" + balance +"' WHERE `users`.`ID` = " + userID + ";";
            // UPDATE `users` SET `balance` = '50' WHERE `users`.`ID` = 9; 
            //System.out.println(serverChecked);
            stmt.executeUpdate(serverUpdate);

            con.close();
        } catch (Exception ef) {
            //TODO: handle exception
            System.out.println(ef);
        }    

    }
}