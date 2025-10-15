package bank.management.system;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Signup extends JFrame implements ActionListener {
    JRadioButton r1,r2;
    JButton next;

    JTextField textName, textEmail,textAdd,textcity,textState,textPin;
    JDateChooser dateChooser;
    Random ran = new Random();
    long first4 =(ran.nextLong() % 9000L) +1000L;
    String first = " " + Math.abs(first4);
    private JTextField textField;
    Signup(){
        super ("APPLICATION FORM");

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/bank.png"));
        Image i2 = i1.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(25,10,100,100);
        getContentPane().add(image);

        JLabel label1 = new JLabel("Registro Usuario");
        label1.setBounds(250,38,600,40);
        label1.setFont(new Font("Raleway",Font.BOLD,38));
        getContentPane().add(label1);

        JLabel labelName = new JLabel("Nombre:");
        labelName.setFont(new Font("Raleway", Font.BOLD, 20));
        labelName.setBounds(123,142,100,30);
        getContentPane().add(labelName);

        textName = new JTextField();
        textName.setFont(new Font("Raleway",Font.BOLD, 14));
        textName.setBounds(290,145,400,30);
        getContentPane().add(textName);

        JLabel DOB = new JLabel("Cumpleaños:");
        DOB.setFont(new Font("Raleway", Font.BOLD, 20));
        DOB.setBounds(123,255,200,30);
        getContentPane().add(DOB);

        dateChooser = new JDateChooser();
        dateChooser.setForeground(new Color(105,105,105));
        dateChooser.setBounds(300,340,400,30);
        getContentPane().add(dateChooser);

        JLabel labelG = new JLabel("Sexo:");
        labelG.setFont(new Font("Raleway", Font.BOLD, 20));
        labelG.setBounds(123,200,200,30);
        getContentPane().add(labelG);

        r1 = new JRadioButton("Male");
        r1.setFont(new Font("Raleway", Font.BOLD,14));
        r1.setBackground(new Color(222,255,228));
        r1.setBounds(290,203,60,30);
        getContentPane().add(r1);

        r2 = new JRadioButton("Female");
        r2.setBackground(new Color(222,255,228));
        r2.setFont(new Font("Raleway", Font.BOLD,14));
        r2.setBounds(433,203,90,30);
        getContentPane().add(r2);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(r1);
        buttonGroup.add(r2);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(new Font("Raleway", Font.BOLD, 20));
        labelEmail.setBounds(123,324,200,30);
        getContentPane().add(labelEmail);

        textEmail = new JTextField();
        textEmail.setFont(new Font("Raleway",Font.BOLD, 14));
        textEmail.setBounds(290,258,400,30);
        getContentPane().add(textEmail);

        ButtonGroup buttonGroup1 = new ButtonGroup();

        JLabel labelAdd = new JLabel("Dirección:");
        labelAdd.setFont(new Font("Raleway", Font.BOLD, 20));
        labelAdd.setBounds(123,389,200,30);
        getContentPane().add(labelAdd);

        textAdd = new JTextField();
        textAdd.setFont(new Font("Raleway",Font.BOLD, 14));
        textAdd.setBounds(290,325,400,30);
        getContentPane().add(textAdd);

        JLabel labelCity = new JLabel("Ciudad:");
        labelCity.setFont(new Font("Raleway", Font.BOLD, 20));
        labelCity.setBounds(123,455,200,30);
        getContentPane().add(labelCity);

        textcity = new JTextField();
        textcity.setFont(new Font("Raleway",Font.BOLD, 14));
        textcity.setBounds(290,389,400,30);
        getContentPane().add(textcity);

        JLabel labelPin = new JLabel("Código Pin:");
        labelPin.setFont(new Font("Raleway", Font.BOLD, 20));
        labelPin.setBounds(123,578,200,30);
        getContentPane().add(labelPin);

        textPin = new JTextField();
        textPin.setFont(new Font("Raleway",Font.BOLD, 14));
        textPin.setBounds(290,455,400,30);
        getContentPane().add(textPin);

        JLabel labelstate = new JLabel("Provincia:");
        labelstate.setFont(new Font("Raleway", Font.BOLD, 20));
        labelstate.setBounds(123,517,200,30);
        getContentPane().add( labelstate);

        textState = new JTextField();
        textState.setFont(new Font("Raleway",Font.BOLD, 14));
        textState.setBounds(290,520,400,30);
        getContentPane().add(textState);

        next = new JButton("Next");
        next.setFont(new Font("Raleway",Font.BOLD, 14));
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        next.setBounds(612,643,80,30);
        next.addActionListener(this);
        getContentPane().add(next);

        getContentPane().setBackground(new Color(222,255,228));
        getContentPane().setLayout(null);
        
        textField = new JTextField();
        textField.setFont(new Font("Dialog", Font.BOLD, 14));
        textField.setBounds(290, 581, 400, 30);
        getContentPane().add(textField);
        setSize(850,800);
        setLocation(360,40);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String formno = first;
        String name = textName.getText();
        String fname = textFname.getText();
        String dob = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();
        String gender = null;
        if(r1.isSelected()){
            gender = "Male";
        }else if (r2.isSelected()){
            gender = "Female";
        }
        String email = textEmail.getText();
        String marital =null;
        if (m1.isSelected()){
            marital = "Married";
        } else if (m2.isSelected()) {
            marital = "Unmarried";
        } else if (m3.isSelected()) {
            marital = "Other";
        }

        String address = textAdd.getText();
        String city = textcity.getText();
        String pincode = textPin.getText();
        String state = textState.getText();

        try{
            if (textName.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Fill all the fields");
            }else {
                Connn c = new Connn();
                String q = "insert into signup values('"+formno+"', '"+name+"','"+fname+"','"+dob+"','"+gender+"','"+email+"','"+marital+"', '"+address+"', '"+city+"','"+pincode+"','"+state+"' )";
                c.statement.executeUpdate(q);
                new Signup2(formno);
                setVisible(false);
            }

        }catch (Exception E){
            E.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Signup();
    }
}
