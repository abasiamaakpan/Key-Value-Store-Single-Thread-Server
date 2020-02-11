import java.net.*;
import java.io.*;
import java.util.HashMap;

/**
 * TCP CLIENT
 */
public class Client_New {
    public static void main(String[] args) {

        try {
            /**Streams for I/O functionality**/

            if (args.length < 2) {
                System.out.println("Usage: java Client_For_Project  <portNumber> ");
                System.exit(1);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));

            /**Start Client**/

            String hostName = args[0];


            int portNumber = Integer.parseInt(args[1]);


            Socket s = new Socket(hostName, portNumber);
            System.out.println("Client Started at address <" + hostName + "> " + "and" + " port <" + portNumber + ">");

           // s.setSoTimeout(7000);
            //s.connect(new InetSocketAddress(InetAddress.getByName(hostName), portNumber)), timeout);

           /**Streams for I/O functionality**/
            DataInputStream inStream = new DataInputStream(s.getInputStream());
            DataOutputStream outStream = new DataOutputStream(s.getOutputStream());
            BufferedReader commandreader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader keyreader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader valuereader = new BufferedReader(new InputStreamReader(System.in));

            /**Key Store Data**/
            int command = 0;
            int key = 0;
            String value = "";

            /****Prepopulation of Key-Value Store****/
           // populate();

            Server_For_Project.key_value_store.put(1, "value1");
            Server_For_Project.key_value_store.put(2, "value2");
            Server_For_Project.key_value_store.put(1, "value3");
            Server_For_Project.key_value_store.put(4, "value4");
            Server_For_Project.key_value_store.put(5, "value5");


            System.out.println("Populated HashMap. Check README for data description");

            /***5 GETS***/
           /* Server_For_Project.get(1);
            Server_For_Project.get(2);
            Server_For_Project.get(3);
            Server_For_Project.get(4);
            Server_For_Project.get(5);*/

            /***5 DELETE***/
           /* Server_For_Project.delete(1);
            Server_For_Project.delete(2);
            Server_For_Project.delete(3);
            Server_For_Project.delete(4);
            Server_For_Project.delete(5);*/

            /**Communicate with server**/
            while (true) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

                /**Validate command, key and value and query server**/

                int right_command= validateCommand(command,commandreader);
                int right_key;
                String right_value;
                outStream.writeInt(right_command);

                if (right_command == Server_For_Project.CMD_EXIT) {
                    Server_For_Project.Logger_Function("Goodbye");
                    s.close();
                    break;
                }  else if (right_command == Server_For_Project.CMD_PUT) {

                    right_key= validateKey(key,keyreader);
                    outStream.writeInt(right_key);

                    right_value=validateValue(value, valuereader);
                    outStream.writeUTF(right_value);


                } else if (right_command == Server_For_Project.CMD_GET) {

                    right_key= validateKey(key,keyreader);

                    outStream.writeInt(right_key);


                } else if (right_command == Server_For_Project.CMD_DEL) {
                    right_key= validateKey(key,keyreader);

                    outStream.writeInt(right_key);


                }

                /**Server response**/
                Server_For_Project.Logger_Function("[FROM Server] " + reader.readLine());
            }
            /**check**/
           System.exit(0);
            inStream.close();
            outStream.close();

        /**Not Sure**/
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            Server_For_Project.Logger_Function("No response from server");
            Server_For_Project.Logger_Function("Goodbye");

        }
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
                    if(c>=1 && c<=4){
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



}
