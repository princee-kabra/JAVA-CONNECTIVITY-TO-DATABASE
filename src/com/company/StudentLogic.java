package com.company;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;


public class StudentLogic extends JFrame implements ActionListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton btnAdd,btnUpdate,btnDelete,btnShow;
    JTextField txtRoll,txtName,txtAddress;
    JLabel lblRoll,lblName,lblAddress;
    JTable jTable;
    Container c;

    StudentLogic(){
        //set Title of JFrame
        super("Student Form");

        // adding container to JFrame
        c = getContentPane();

        // setting Layout of JFrame to NUll
        c.setLayout(null);

        //Creating Label Text for each TextField and adding to container
        lblRoll = new JLabel("Enter Roll : ");
        lblRoll.setBounds(20,50,80,30);
        c.add(lblRoll);
        txtRoll =  new JTextField();
        txtRoll.setBounds(120,50,200,30);
        c.add(txtRoll);
        lblName = new JLabel("Enter Name : ");
        lblName.setBounds(20,100,100,30);
        c.add(lblName);
        txtName =  new JTextField();
        txtName.setBounds(120,100,200,30);
        c.add(txtName);
        lblAddress = new JLabel("Enter Address : ");
        lblAddress.setBounds(20,150,150,30);
        c.add(lblAddress);
        txtAddress =  new JTextField();
        txtAddress.setBounds(120,150,200,30);
        c.add(txtAddress);

        //creating button and adding to container
        btnAdd = new JButton("Add");
        btnAdd.setBounds(10,250,80,40);
        c.add(btnAdd);
        btnShow = new JButton("Show");
        btnShow.setBounds(110,250,80,40);
        c.add(btnShow);
        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(210,250,80,40);
        c.add(btnUpdate);
        btnDelete = new JButton("Delete");
        btnDelete.setBounds(310,250,80,40);
        c.add(btnDelete);

        //adding action Listener to each Button
        btnAdd.addActionListener(this);
        btnShow.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);

    }

    ArrayList<StudentDetails> getStudents(){
        ArrayList<StudentDetails> studentData = new ArrayList<>();
        try{
            Statement stmt = Main.con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from student");
            StudentDetails studentDetails;
            while(rs.next()){
                int roll = rs.getInt(1);
                String name = rs.getString(2);
                String address = rs.getString(3);
                studentDetails = new StudentDetails(roll,name,address);
                studentData.add(studentDetails);
            }
        }catch(SQLException e1){
            e1.printStackTrace();
        }
        return studentData;
    }

    JTable getTable(){
        Vector<Object> columnData = new Vector<>();
        columnData.add("Roll No.");
        columnData.add("Name");
        columnData.add("Address");
        Vector<Vector<Object>> data = new Vector<>();
        ArrayList<StudentDetails> list = getStudents();
        for (StudentDetails studentDetails : list) {
            Vector<Object> row = new Vector<>();
            row.add(studentDetails.getStudentRoll());
            row.add(studentDetails.getStudentName());
            row.add(studentDetails.getStudentAddress());
            data.add(row);
        }
        jTable = new JTable(data,columnData);
        return jTable;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==btnAdd){
            String name = txtName.getText();
            String address = txtAddress.getText();
            int roll = Integer.parseInt(txtRoll.getText());
            try{
                Statement stmt = Main.con.createStatement();
                stmt.executeUpdate("insert into student(roll_no,stu_name,stu_address) values("+roll+",'"+name+"','"+address+"')");
                JOptionPane.showMessageDialog(null,"Data inserted Successfully");
            }catch (SQLException e1){
                e1.printStackTrace();
            }
            txtRoll.setText(null);
            txtName.setText(null);
            txtAddress.setText(null);
        }else if(e.getSource()==btnShow){
            jTable = getTable();
            new StudentTable(jTable,"Student Table");
            jTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row_index = jTable.getSelectedRow();
                    txtName.setText(jTable.getValueAt(row_index,1).toString());
                    txtAddress.setText(jTable.getValueAt(row_index,2).toString());
                    txtRoll.setText(jTable.getValueAt(row_index,0).toString());
                }
            });
        }else if(e.getSource()==btnUpdate){
            try{
                Statement stmt = Main.con.createStatement();
                stmt.executeUpdate("update student set stu_name='"+txtName.getText()+"',stu_address='"+txtAddress.getText()+"' where roll_no="+Integer.parseInt(txtRoll.getText()));
            }catch (NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"To update the data first open the Table!");
                return;
            }catch (SQLException e1){
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null,"Data Updated Successfully");
            jTable = getTable();
            new StudentTable(jTable,"Student Table");
            txtRoll.setText(null);
            txtName.setText(null);
            txtAddress.setText(null);
        }else if(e.getSource()==btnDelete){
            jTable = getTable();
            new StudentTable(jTable,"Student Table");
            jTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row_index = jTable.getSelectedRow();
                    txtRoll.setText(jTable.getValueAt(row_index,0).toString());
                    int response = JOptionPane.showConfirmDialog(null,"Do you want to delete ? ","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(response==JOptionPane.YES_OPTION){
                        try{
                            Statement stmt = Main.con.createStatement();
                            stmt.executeUpdate("delete from student where roll_no="+Integer.parseInt(txtRoll.getText()));
                        }catch (SQLException e1){
                            e1.printStackTrace();
                        }
                        jTable = getTable();
                        new StudentTable(jTable,"Updated Table");
                    }else{
                        txtRoll.setText(null);
                    }
                }
            });
            txtRoll.setText(null);
            txtName.setText(null);
            txtAddress.setText(null);
        }
    }

}

