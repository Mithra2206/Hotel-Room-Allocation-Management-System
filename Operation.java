package org.example;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Operation {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/hotel";
    private static final String user = "root";
    private static final String password = "root";
    public void start(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            Scanner readInput = new Scanner(System.in);
            System.out.println("WELCOME TO HOTEL RESERVATION SYSTEM");
            while (true){
                System.out.println("1. Book a reservation");
                System.out.println("2. View a reservations");
                System.out.println("3. Get Guest Details");
                System.out.println("4. Update a reservation");
                System.out.println("5. Close a reservation");
                System.out.println("6. exit");
                int choice = readInput.nextInt();
                boolean endProgram = false;
                readInput.nextLine();
                switch (choice){
                    case 1:
                        bookReservation(readInput, statement);
                        break;
                    case 2:
                        viewReservation(statement);
                        break;
                    case 3:
                        getDetails(readInput, statement);
                        break;
                    case 4:
                        updateReservation(readInput, statement);
                        break;
                    case 5:
                        closeReservation(statement, readInput);
                        break;
                    case 6:
                        try {
                            exit();
                        }catch (InterruptedException e){
                            System.out.println(e.getMessage());
                        }
                        endProgram = true;
                        break;
                    default:
                        System.out.println("Enter a valid Choice!!!");
                }
                if(endProgram){
                    break;
                }
            }
            readInput.close();
            statement.close();
            connection.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void bookReservation(Scanner readInput, Statement statement) throws SQLException{
        while (true){
            System.out.print("Guest Name :");
            String name = readInput.nextLine();
            System.out.print("Room Number: ");
            int roomNumber = readInput.nextInt();
            readInput.nextLine();
            System.out.print("Contact Number: ");
            String contactNumber = readInput.nextLine();
            if(contactNumber.length() > 10){
                System.out.println("Enter a valid number!!!");
            }else if(check(statement, roomNumber)){
                System.out.println("Room is Occupied Choose another room!!!");
            }else{
                int rowsEffected = statement.executeUpdate("INSERT INTO reservations(guest_name, room_number, contact_number) VALUES('"+name+"','"+roomNumber+"','"+contactNumber+"')");
                if(rowsEffected > 0){
                    System.out.println("Reservation Successful");
                }else{
                    System.out.println("Reservation Failed");
                }
            }
            System.out.print("Enter 7 to return to main menu :: ");
            int choice = readInput.nextInt();
            readInput.nextLine();
            if(choice == 7){
                break;
            }
        }
    }

    private boolean check(Statement statement, int roomNumber) throws SQLException{
        ResultSet resultSet = statement.executeQuery("SELECT room_number FROM reservations WHERE room_number = '"+roomNumber+"'");
        while (resultSet.next()){
            if(resultSet.getInt(1) > 0){
                return true;
            }
        }
        return false;
    }

    private void viewReservation(Statement statement) throws SQLException{
        ResultSet resultSet = statement.executeQuery("SELECT * FROM reservations");
        System.out.println("+------------------------------------------------------+");
        while (resultSet.next()){
            System.out.println("Reservation Id: " + resultSet.getInt(1));
            System.out.println("Guest Name: " + resultSet.getString(2));
            System.out.println("Room Number: " + resultSet.getInt(3));
            System.out.println("Contact Number: " + resultSet.getString(4));
            System.out.println("Reservation Date: " + resultSet.getString(5));
            System.out.println("+------------------------------------------------------+");
        }
    }

    private void getDetails(Scanner readInput,Statement statement) throws SQLException{
        while (true){
            System.out.print("Enter reservation id: ");
            int reservationId = readInput.nextInt();
            System.out.print("Enter room number: ");
            int roomNumber = readInput.nextInt();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM reservations WHERE reservation_id = '" + reservationId + "' AND room_number = '" + roomNumber + "'");
            System.out.println("+------------------------------------------------------+");
            boolean isExist = true;
            while (resultSet.next()) {
                System.out.println("Reservation Id: " + resultSet.getInt(1));
                System.out.println("Guest Name: " + resultSet.getString(2));
                System.out.println("Room Number: " + resultSet.getInt(3));
                System.out.println("Contact Number: " + resultSet.getString(4));
                System.out.println("Reservation Date: " + resultSet.getString(5));
                isExist = false;
            }
            if(isExist){
                System.out.println("Enter Valid Details!!!");
            }
            System.out.println("+------------------------------------------------------+");
            System.out.print("Enter 7 to return to main menu :: ");
            int choice = readInput.nextInt();
            readInput.nextLine();
            if(choice == 7){
                break;
            }
        }
    }


    private void updateReservation(Scanner readInput, Statement statement){
        while (true){
            System.out.print("Enter reservation id:");
            int id = readInput.nextInt();
            readInput.nextLine();
            try{
                if(!reservationExist(statement, id)){
                    System.out.println("Reservation Id doesn't exist!!!");
                }else {
                    System.out.print("Enter guest name :");
                    String name = readInput.nextLine();
                    System.out.print("Enter room number :");
                    int roomNumber = readInput.nextInt();
                    readInput.nextLine();
                    System.out.print("Enter phone number :");
                    String phoneNumber = readInput.nextLine();
                    if(check(statement, roomNumber)){
                        System.out.println("Room Occupied choose Another room");
                    }else{
                        int rowsEffected = statement.executeUpdate("UPDATE reservations SET  guest_name ='"+name+"', room_number ='"+roomNumber+"',  contact_number = '"+phoneNumber+"' WHERE reservation_id = '"+id+"'");
                        if(rowsEffected > 0){
                            System.out.println("Updation Successful");
                        }else{
                            System.out.println("Updation Failed");
                        }
                    }
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
            System.out.print("Enter 7 to return to main menu :: ");
            int choice = readInput.nextInt();
            readInput.nextLine();
            if(choice == 7){
                break;
            }
        }
    }

    private boolean reservationExist(Statement statement, int id) throws SQLException{
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(reservation_id) FROM reservations WHERE reservation_id = '"+id+"'");
        return resultSet.next();
    }

    private void closeReservation(Statement statement, Scanner readInput) throws SQLException{
        while (true){
            System.out.print("Enter guest Id :");
            int id = readInput.nextInt();
            int rowsEffected = statement.executeUpdate("DELETE FROM reservations WHERE reservation_id = '"+id+"'");
            if(rowsEffected > 0){
                System.out.println("Reservation Closed Successfully");
            }else{
                System.out.println("Reservation Closed Failed");
            }
            System.out.print("Enter 7 to return to main menu :: ");
            int choice = readInput.nextInt();
            readInput.nextLine();
            if(choice == 7){
                break;
            }
        }
    }

    private void exit() throws InterruptedException{
        System.out.print("Exiting System");
        for(int i = 0; i < 5; i++){
            System.out.print(".");
            Thread.sleep(1000);
        }
        System.out.println();
        System.out.println("Thanks for using Hotel Management system");
    }
}
