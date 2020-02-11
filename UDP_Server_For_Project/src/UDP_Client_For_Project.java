import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * UDP Server
 */
public class UDP_Client_For_Project
{

    public static void main(String args[])
    {

        // Step 1:Create the socket object for
        // carrying the data.
        try {
            DatagramSocket ds = new DatagramSocket();

            if (args.length < 2) {
            System.out.println("Syntax: UDP_Client_For_Project <hostname> <port>");
            return;
            }


            byte[] receive_buffer = new byte[65535];


            InetAddress ip = InetAddress.getLocalHost();


            // loop while user not enters "bye"
            // System.out.println("Enter PortNumber: ");

            int portNumber = Integer.parseInt(args[1]);



            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            //  String command="";
            // String spaceaftercommand=" ";
            //String spaceafterkey=" ";
            byte [] m = args[0].getBytes();
            DatagramPacket request =
                    new DatagramPacket(m, args[0].length(), ip, portNumber);
            ds.send(request);

            String quote="";
           // System.out.println("Reply: " + new String(reply.getData()));


            System.out.println("Client Started at address <" + ip.toString() + ">" + " and" + " port <" + portNumber + ">");

            /****Prepopulation of Key-Value Store****/
            // populate();
            String[] thisIsAStringArray = new String[5];
            thisIsAStringArray[0] = "1";
            thisIsAStringArray[1] = "1";
            thisIsAStringArray[2] = "value1";

            String[] thisIsAStringArray1 = new String[5];
            thisIsAStringArray1[0] = "1";
            thisIsAStringArray1[1] = "2";
            thisIsAStringArray1[2] = "value2";

            String[] thisIsAStringArray2 = new String[5];
            thisIsAStringArray2[0] = "1";
            thisIsAStringArray2[1] = "3";
            thisIsAStringArray[2] = "value3";

            String[] thisIsAStringArray3 = new String[5];
            thisIsAStringArray3[0] = "1";
            thisIsAStringArray3[1] = "4";
            thisIsAStringArray3[2] = "value4";

            String[] thisIsAStringArray4 = new String[5];
            thisIsAStringArray4[0] = "1";
            thisIsAStringArray4[1] = "5";
            thisIsAStringArray4[2] = "value5";


            UDP_Server_For_Project.insert_key_value(thisIsAStringArray);
            UDP_Server_For_Project.insert_key_value(thisIsAStringArray1);
            UDP_Server_For_Project.insert_key_value(thisIsAStringArray2);
            UDP_Server_For_Project.insert_key_value(thisIsAStringArray3);
            UDP_Server_For_Project.insert_key_value(thisIsAStringArray4);

        /*UDP_Server_For_Project.put("1", "value1");
        UDP_Server_For_Project.put("2", "value2");
        UDP_Server_For_Project.put("3", "value3");
        UDP_Server_For_Project.put("4", "value4");
        UDP_Server_For_Project.put("5", "value5");*/
            System.out.println("Populated HashMap. Check README for data description");

            /**Key Store Data**/
            int command = 0;
            int key = 0;
            String value = "";

            byte[] buf5;
            String inp3 = " ";  //space

            String inp4 = " "; //space
            int right_command = 0;
            int right_key = 0;
            String right_value = " ";
            String inp1 = ""; //key
            String inp2 = ""; //value
            String send_command = "";
            String send_key = "";

            DatagramPacket DpSend = null;

            DatagramPacket reply = new DatagramPacket(receive_buffer, receive_buffer.length);

          while (true) {

                // System.out.println("Enter Command: ");
                //String inp = sc.nextLine();  //command

                right_command = validateCommand(command, reader);
                send_command = Integer.toString(right_command);


                if (right_command == UDP_Server_For_Project.CMD_EXIT) {
                    UDP_Server_For_Project.Logger_Function("Goodbye");
                    ds.close();
                    //break;
                } else if (right_command == UDP_Server_For_Project.CMD_PUT) {

                    right_key = validateKey(key, reader);

                    send_key = Integer.toString(right_key);

                    // outStream.writeInt(right_key);

                    right_value = validateValue(value, reader);

                    // outStream.writeUTF(right_value);
                    buf5 = collate_put_query(send_command, send_key, right_value, inp3, inp4);

                    DpSend =
                            new DatagramPacket(buf5, buf5.length, ip, portNumber);

                    ds.send(DpSend);


                } else if (right_command == UDP_Server_For_Project.CMD_GET) {

                    right_key = validateKey(key, reader);

                    send_key = Integer.toString(right_key);


                    // outStream.writeInt(right_key);

                    buf5 = collate_get_query(send_command, send_key, inp3);

                    DpSend =
                            new DatagramPacket(buf5, buf5.length, ip, portNumber);

                    ds.send(DpSend);

                } else if (right_command == UDP_Server_For_Project.CMD_DEL) {
                    right_key = validateKey(key, reader);

                    send_key = Integer.toString(right_key);


                    // outStream.writeInt(right_key);

                    buf5 = collate_delete_query(send_command, send_key, inp3);

                    DpSend =
                            new DatagramPacket(buf5, buf5.length, ip, portNumber);

                    ds.send(DpSend);

                }


                // UDP_Server_For_Project.Logger_Function("[FROM Server] " + UDP_Server_For_Project.decode(receive));


                //  outStream.writeInt(right_command);


//            if (inp.equals("bye")) {
//                break;
//            }
//
//            else if (inp.equals( "put")) {
//                System.out.println("Enter key: ");
//                inp1 = sc.nextLine();  //key
//
//                inp4 = " ";  //space
//
//                System.out.println("Enter value: ");
//                inp2 = sc.nextLine();  //value
//
//                byte[] buf5=put(inp, inp1, inp2, inp3, inp4);
//
//                DatagramPacket DpSendput =
//                        new DatagramPacket(buf5, buf5.length, ip, portNumber);
//
//                ds.send(DpSendput);
//
//            }
//
//            else if (inp.equals("get")){
//                System.out.println("Enter key: ");
//                inp1 = sc.nextLine();  //key
//
//                inp3 = " ";  //space
//
//                byte[] buf5=getordelete(inp, inp1, inp3);
//
//                DatagramPacket DpSendget =
//                        new DatagramPacket(buf5, buf5.length, ip, portNumber);
//
//                ds.send(DpSendget);
//
//            }
//
//
//            else if (inp.equals("delete")){
//                System.out.println("Enter key: ");
//                inp1 = sc.nextLine();  //key
//
//                inp3 = " ";  //space
//
//                byte buf5[]=getordelete(inp, inp1, inp3);
//
//                DatagramPacket DpSenddelete =
//                        new DatagramPacket(buf5, buf5.length, ip, portNumber);
//
//                ds.send(DpSenddelete);
//            }
          /*  DpReceive = new DatagramPacket(receive, receive.length);
            ds.receive(DpReceive);

            UDP_Server_For_Project.Logger_Function("[FROM Server] " + UDP_Server_For_Project.decode(receive));
            Arrays.fill(receive, (byte) 0);*/
                ds.receive(reply);
             // String modifiedSentence = new String(reply.getData());
            //   quote = new String(receive_buffer, 0, reply.getLength());
              // System.out.println(modifiedSentence + "/0");

                UDP_Server_For_Project.Logger_Function("[FROM Server] " + UDP_Server_For_Project.decode(reply.getData()));

                //receive_buffer = new byte[65535];


          }
        }

            catch (SocketException e) {

                System.out.println("Socket: " + e.getMessage());
            }
            catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }
            //finally {
                //if (ds != null)
                   // ds.close();
          // }



    }

    public static byte [] collate_put_query(String inp,  String inp1, String inp2, String inp3, String inp4) {

        byte buf5[]=concatenateByteArraysputcorrect(inp.getBytes(), inp3.getBytes(), inp1.getBytes(), inp4.getBytes(), inp2.getBytes());

        String inp5 = inp + inp3 + inp1 + inp4 + inp2;
        //UDP_Server_For_Project.Logger_Function("[FROM Server] " +  "Key-value pair:" + inp5 + " successfully stored");

        //System.out.println("Concat Strings:" + inp5);

        return buf5;
    }

    public static byte [] collate_get_query(String inp, String inp1, String inp3) {

        byte buf5[]=concatenateByteArraysgetanddeletecorrect(inp.getBytes(), inp3.getBytes(), inp1.getBytes());

        String inp5 = inp + inp3 + inp1;
        //UDP_Server_For_Project.Logger_Function("[FROM Server] " +  " Got " + inp5+ " from server ");

        //System.out.println("Concat Strings:" + inp5);

        return buf5;


    }

    public static byte [] collate_delete_query(String inp, String inp1, String inp3) {

        byte buf5[]=concatenateByteArraysgetanddeletecorrect(inp.getBytes(), inp3.getBytes(), inp1.getBytes());

        String inp5 = inp + inp3 + inp1;
        //UDP_Server_For_Project.Logger_Function("[FROM Server] " + inp5 + " deleted ");

        //System.out.println("Concat Strings:" + inp5);

        return buf5;


    }


    public static byte[] concatenateByteArraysputcorrect(byte[] a, byte[] b, byte[] c, byte[] d, byte[] e) {
        ByteArrayOutputStream my_stream = new ByteArrayOutputStream();
        try {
            my_stream.write(a);
            my_stream.write(b);
            my_stream.write(c);
            my_stream.write(d);
            my_stream.write(e);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        byte[] concatenated_byte_array = my_stream.toByteArray();
        //for(int i=0; i< concatenated_byte_array.length ; i++) {
           // System.out.print(concatenated_byte_array[i] +" ");
        //}

        return concatenated_byte_array;


    }


    public static byte[] concatenateByteArraysgetanddeletecorrect(byte[] a, byte[] b, byte[] c) {
        ByteArrayOutputStream my_stream = new ByteArrayOutputStream();
        try {
            my_stream.write(a);
            my_stream.write(b);
            my_stream.write(c);


        } catch (IOException e1) {
            e1.printStackTrace();
        }
        byte[] concatenated_byte_array = my_stream.toByteArray();
        //for(int i=0; i< concatenated_byte_array.length ; i++) {
           // System.out.print(concatenated_byte_array[i] +" ");
        //}

        return concatenated_byte_array;

    }


    /**
     * Validate Command (GET,PUT,DELETE,EXIT) from Client
     * @param c is the command to be validated
     * @param cr reads the text from an Input stream
     * @return the validated command
     */
    public static int validateCommand(int c, BufferedReader cr ) {
        while (true) {
            try {
                System.out.println("Enter 1 to PUT, 2 to GET, 3 to DELETE, 4 to EXIT");
                System.out.println("Enter Command: ");
                c = Integer.parseInt(cr.readLine());

                //Validate Command PUT(1),GET(2),DELETE(3),EXIT(4) from Client
                if((c == UDP_Server_For_Project.CMD_PUT) || (c ==UDP_Server_For_Project.CMD_GET) || (c ==UDP_Server_For_Project.CMD_DEL) || (c == UDP_Server_For_Project.CMD_EXIT)){
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println("command is not an int value or must be in range 1<=command<4. Re-enter command");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return c;
    }


    /**
     * Validate Key from User
     * @param k is the key to be validated
     * @param kr reads the text from an Input stream
     * @return the validated key
     */
    public static int validateKey(int k, BufferedReader kr){
        while (true) {
            try {
                System.out.println("Enter Numeric Values Only for Key");
                System.out.println("Enter Key: ");
                k = Integer.parseInt(kr.readLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("key is not an int value. Reneter key");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return k;
    }

    /**
     * Validate Value from User
     * @param value is the value to be validated
     * @param vr reads the text from an Input stream
     * @return the validated value
     */
    public static String validateValue(String value, BufferedReader vr){
        while (true) {
            try {
                System.out.println("Enter Alphanumeric characters, whitespaces and symbols allowed");
                System.out.println("Enter Value: ");
                value = vr.readLine();
                break;
            } catch (NumberFormatException e) {
                System.out.println("key is not an int value. Reneter key");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }


          /*  String enter_key="Enter key: ";
            try {
                toClientStream.writeUTF(enter_key);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String enter_value="Enter value:";
            try {
                toClientStream.writeUTF(enter_value);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
       /* public void put(int k, String v) {

        while (true) {
            if (key_value_store.containsKey(k)) {
                try {
                    toClientStream.writeInt(INVALID_KEY);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Cannot PUT: Key already taken: Enter another key");
                }

            } else {
                System.out.println("About to put");
                System.out.println(k);
                System.out.println(v);
                // int a=4;
                // String value="test";

                key_value_store.put(k, v);

                //System.out.println(key_value_store.isEmpty());

                try {
                    toClientStream.writeInt(SUCCESS);
                } catch (IOException e) {
                    System.out.print("is this where the exception is coming from?");
                    e.printStackTrace();
                }
                System.out.println("From Server to Client-" + newClient + " Key-value pair " + " " + k + " " + v + " succesfully stored ");
                break;
            }

        }
    }


    public void get(int k) {
        while(true) {
            if (!key_value_store.containsKey(k)) {
                try {
                    toClientStream.writeInt(INVALID_KEY);
                    System.out.println("Cannot GET: Key does not exist: Choose another key");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("About to get");
                System.out.println(k);
                String value=key_value_store.get(k);
                try {
                    toClientStream.writeInt(SUCCESS);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("From Server to Client-" + newClient + " Got " + value + " from server " + "from " + k);
                break;
            }
        }



    }

    public void delete(int k) {
        while(true){
            if (!key_value_store.containsKey(k)) {
                try {
                    toClientStream.writeInt(INVALID_KEY);
                    System.out.println("Cannot GET: Key does not exist: Choose another key");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                key_value_store.remove(k);
                try {
                    toClientStream.writeInt(SUCCESS);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("From Server to Client-" + newClient + k + "deleted");
                break;
            }

        }
    }*/




    /*public void saveHashToFile(HashMap<String, Object> hash) throws IOException{
        String filePath = getApplicationContext().getFilesDir().getPath().toString() + "/your.properties";
        File file = new File(filePath);
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(getProperty_Global());
        s.close();


    }*/








}

/*import java.net.*;
import java.io.*;
public class UDP_Client_For_Project {
    public static void main(String args[]){
// args give message contents and server hostname
        DatagramSocket aSocket = null;
        if (args.length < 3) {
            System.out.println("Usage: java UDPClient <message> <Host name> <Port number>");
            System.exit(1);
        }
        try {
            aSocket = new DatagramSocket();
            byte [] m = args[0].getBytes();
            InetAddress aHost = InetAddress.getByName(args[1]);
            int serverPort = Integer.valueOf(args[2]).intValue();
            DatagramPacket request =
                    new DatagramPacket(m, args[0].length(), aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            System.out.println("Reply: ” + new String(reply.getData())");
        }
        catch (SocketException e) {
            System.out.println("Socket: ” + e.getMessage()");
        }
        catch (IOException e) {
            System.out.println("IO: ” + e.getMessage()");
        }
        finally {
            if (aSocket != null)
                aSocket.close();
        }
    }
}*/

// convert the String input into the byte array.
           /* buf = inp.getBytes();
            String s = new String(buf);

            //System.out.println(s);

            buf3 =inp3.getBytes();
            String s3 = new String(buf);


            buf1 = inp1.getBytes();
            String s1 = new String(buf);


            buf4 =inp4.getBytes();
            String s4 = new String(buf);


            buf2 = inp2.getBytes();
            String s2 = new String(buf);




            byte buf5[]=concatenateByteArrays(buf, buf3, buf1, buf4, buf2);
            String inp5 = inp + inp3 + inp1 + inp4 + inp2;
            System.out.println("Concat Strings" + inp5);

            // Step 2 : Create the datagramPacket for sending
            // the data.


            DatagramPacket DpSend =
                    new DatagramPacket(buf5, buf5.length, ip, portNumber);
          //  DatagramPacket DpKey =
                    //new DatagramPacket(buf, buf.length, ip, portNumber);
           // DatagramPacket DpValue =
                    //new DatagramPacket(buf, buf.length, ip, portNumber);
            // Step 3 : invoke the send call to actually send
            // the data.
            ds.send(DpSend);
           // ds.send(DpKey);
            //ds.send(DpValue);*/

// break the loop if user enters "bye"