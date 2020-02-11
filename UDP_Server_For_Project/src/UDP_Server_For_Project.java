// Java program to illustrate Server side
// Implementation using DatagramSocket
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class UDP_Server_For_Project {

    /**PROTOCOLS FOR KEY VALUE STORE**/
    public static final int SUCCESS =0;
    public static final int INVALID_KEY = -1;
    public static final int CMD_PUT = 1;
    public static final int CMD_GET = 2;
    public static final int CMD_DEL = 3;
    public static final int CMD_EXIT = 4;
    public static final  String format = "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS.%1$tL %1$Tp %2$s%n%4$s: %5$s%n";


    /**KEY VALUE STORE POPULATION**/
    public static Map<Integer, String> key_value_store  = new HashMap<Integer, String>() {{
        put(1, "value1");
        put(2, "value2");
        put(3, "value3");
        put(4, "value4");
        put(5, "value5");
    }};

    /**COMMUNICATION PACKET DEFINIITION**/
    public static  DatagramPacket DpReceive = null;
    public static  DatagramPacket DpSend = null;
    public static DatagramSocket ds = null;
    public static byte[] buf5;
    public static InetAddress ip;
    public static int portNumber;

    public static void main(String[] args) throws IOException {
        // Step 1 : Create a socket to listen at port 1234
        //DatagramSocket ds = new DatagramSocket(Integer.parseInt(args[0]));
        /**Check for Correct Program arguments**/
        if (args.length < 1) {
            System.out.println("Usage: java UDP_Server_For_Project <Port Number>");
            System.exit(1);
        }

        /**Start server and check for client availability**/
        try{
        ds = new DatagramSocket(Integer.parseInt(args[0]));
        byte[] receive = new byte[65535];

        ip = InetAddress.getLocalHost();
        portNumber =  Integer.parseInt(args[0]);

        if (args.length < 1) {
            System.out.println("Usage: java UDP_Server_For_Project "+ portNumber);
            System.exit(1);
        }

        System.out.println("Server Started at port <" + portNumber + ">");
        System.out.println("HashMap Empty? " + key_value_store.isEmpty());




            // DatagramPacket DpReceive = null;
        //DatagramPacket DpSend = null;

            System.out.println("Client:-" + decode(receive) + "started");
            /**
             * Check for valid commands which are PUT,GET,DELETE,POPULATE,EXIT.
             * This is the Communication logic with the client
             */
        while (true) {
            // Step 2 : create a DatgramPacket to receive the data.
            DpReceive = new DatagramPacket(receive, receive.length);

            // Step 3 : recieve the data in byte buffer.
            //System.out.println("length-" + receive.toString());

            ds.receive(DpReceive);

            // String[] tokens = data(receive).toString().split(" ");


            String[] tokens = decode(receive).split(" ");

            if (tokens[0].equals(Integer.toString(CMD_EXIT)))// change
            {
               // ds.close();
                Logger_Function("Client sent EXIT.....EXITING");
                break;
            } else if (tokens[0].equals(Integer.toString(CMD_PUT))) {
                insert_key_value(tokens);

                //Arrays.fill(receive, (byte) 0);
                //Arrays.fill(tokens, null);

            } else if (tokens[0].equals(Integer.toString(CMD_GET))) {

                get_key_value(tokens);
                //Arrays.fill(receive, (byte) 0);
               // Arrays.fill(tokens, null);


            } else if (tokens[0].equals(Integer.toString(CMD_DEL))) {

                delete_key_value(tokens);
               // Arrays.fill(receive, (byte) 0);
                //Arrays.fill(tokens, null);

            }

            System.out.println("HashMap Structure");
            for (HashMap.Entry<Integer,String> entry : key_value_store.entrySet()) {
                System.out.println("Key = " + entry.getKey() +
                        ", Value = " + entry.getValue());
            }

            // Clear the buffer after every message.
            //receive = new byte[65535];
            Arrays.fill(receive, (byte) 0);
            Arrays.fill(tokens, null);



        }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            Logger_Function("Client-"  + " exited!!");

        }
    }

    /**
    /**
     * Convert the byte array
     * data into a string representation.
     * @param a is the byte to be converted
     * @return converted byte to String
     */
    public static String decode(byte[] a) {

        for (int i = 0; i < a.length; i++) {
            if (a[i] == 0) {
                a[i] = 32;
            }
        }
        String result = new String(a, StandardCharsets.UTF_8);
        Arrays.fill(a, (byte) 0);

        return result;


    }

    /**
     * Put in Hashmap
     * @param tokens is the key-value pair to be parsed
     */
    public static void insert_key_value(String[] tokens) {

     //   Logger_Function("Client entered Put");
      //  Logger_Function("key from client: " + tokens[1].getClass());
//        Logger_Function("value from client: " + (tokens[2]));

        if (key_value_store.containsKey(Integer.parseInt(tokens[1]))) {
            try {
                // toClientStream.writeInt(INVALID_KEY);
                Logger_Function("Error from " + "<" + DpReceive.getAddress() + ">" + "<" + DpReceive.getPort()+ ">"+ "Cannot PUT: Key already taken: Enter another key");

               //send just one string
                buf5=collate_put_query("Cannot PUT: Key already taken: Enter another key");


                DpSend =
                        new DatagramPacket(buf5, buf5.length, ip, DpReceive.getPort());

                ds.send(DpSend);


               // printWriter.println("Cannot PUT: Key already taken: Enter another key");

            } catch (NullPointerException a) {
            } catch (IOException e) {
                e.printStackTrace();

            }

        }

        else {
            //try {
            key_value_store.put(Integer.parseInt(tokens[1]), (tokens[2]));
            //  toClientStream.writeInt(SUCCESS);
            try {
                Logger_Function("From Server to Client-" + "<" + DpReceive.getAddress() + ">" + "<" + DpReceive.getPort()+ ">" + " Key-value pair: " + " " + Integer.parseInt(tokens[1]) + ":" + tokens[2]+ " successfully stored ");
                buf5=collate_put_query(" Key:value pair- "+ Integer.parseInt(tokens[1]) + ":" + tokens[2]+ " successfully stored ");
                DpSend =
                        new DatagramPacket(buf5, buf5.length, ip, DpReceive.getPort());

                ds.send(DpSend);

            }catch (NullPointerException a){
            } catch (IOException e) {
                e.printStackTrace();
            }

        }




    }

    /**
     * Get from Hashmap
     * @param tokens is the key to be gotten
     */
    public static void get_key_value(String[] tokens) {

        Logger_Function("Client entered Get");
        Logger_Function("key from client: " + tokens[1]);

        if(key_value_store.isEmpty()){
            try {
                Logger_Function("Error from " + "<" + DpReceive.getAddress() + ">" + "<" + DpReceive.getPort()+">"+ "Cannot GET: HashMap is empty: PUT some values in" );
                buf5=collate_get_or_delete_query("Cannot GET: HashMap is empty: PUT some values in");


                DpSend =
                        new DatagramPacket(buf5, buf5.length, ip, DpReceive.getPort());

                ds.send(DpSend);

                // printWriter.println("Cannot GET: HashMap is empty: PUT some values in");
            } catch(NullPointerException a){

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (!key_value_store.containsKey(Integer.parseInt(tokens[1]))) {
            try {
                //  toClientStream.writeInt(INVALID_KEY);
                Logger_Function("Error from " + "<" + DpReceive.getAddress() + ">" + "<" + DpReceive.getPort()+ ">."+ "Cannot GET: Key does not exist: Choose another key.");
                buf5=collate_get_or_delete_query("Cannot GET: Key does not exist: Choose another key");


                DpSend =
                        new DatagramPacket(buf5, buf5.length, ip, DpReceive.getPort());

                ds.send(DpSend);

               // printWriter.println("Cannot GET: Key does not exist: Choose another key");

            } catch (NullPointerException a) {
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println(tokens[1]);
            String value=key_value_store.get(Integer.parseInt(tokens[1]));
            //try {
            //  toClientStream.writeInt(SUCCESS);
            try {
                Logger_Function("From Server to Client- " + "<" + DpReceive.getAddress() + ">" + "<" + DpReceive.getPort() + ">" + " Got " + tokens[1] + " from server " + "with key " + tokens[0]);
                buf5=collate_get_or_delete_query("Got " + value + " from server ");


                DpSend =
                        new DatagramPacket(buf5, buf5.length, ip, DpReceive.getPort());

                ds.send(DpSend);

              //  printWriter.println("Got " + value + " from server ");
            }catch(NullPointerException a){

            } catch (IOException e) {
                e.printStackTrace();
            }

            // } catch (IOException e) {
            //  e.printStackTrace();
            // }
        }

    }

    /**
     * Delete from Hashmap
     * @param tokens is the key to be deleted
     */
    public static void delete_key_value(String[] tokens) {
        Logger_Function("Client entered Delete");
        Logger_Function("key from client: " + tokens[1]);
        if(key_value_store.isEmpty()){
            try {
                Logger_Function("Error from " + "<" + DpReceive.getAddress() + ">" + "<" + DpReceive.getPort() + ">"+ "Cannot DELETE: HashMap is empty: PUT some values in" );
                buf5=collate_get_or_delete_query("Cannot DELETE: HashMap is empty: PUT some values in");


                DpSend =
                        new DatagramPacket(buf5, buf5.length, ip, DpReceive.getPort());

                ds.send(DpSend);
               // printWriter.println("Cannot DELETE: HashMap is empty: PUT some values in");
            } catch(NullPointerException a){

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (!key_value_store.containsKey(Integer.parseInt(tokens[1]))) {
            // try {
            //   toClientStream.writeInt(INVALID_KEY);
            try {
                Logger_Function("Error from " + "<" + DpReceive.getAddress() + ">" + "<" + DpReceive.getPort() + ">" + "Cannot DELETE: Key does not exist: Choose another key. Error from" + "<" + "address" + ">" + "<" + "portNumber"+ ">");
                buf5=collate_get_or_delete_query("Cannot DELETE: Key does not exist: Choose another key");


                DpSend =
                        new DatagramPacket(buf5, buf5.length, ip, DpReceive.getPort());

                ds.send(DpSend);

               // printWriter.println("Cannot DELETE: Key does not exist: Choose another key");
            } catch(NullPointerException a){

            } catch (IOException e) {
                e.printStackTrace();
            }

            //} catch (IOException e) {
            // e.printStackTrace();
            // }

        } else {
            key_value_store.remove(Integer.parseInt(tokens[1]));
            try {
                Logger_Function("From Server to Client- " + "<" + DpReceive.getAddress() + ">" + "<" + DpReceive.getPort() + ">"+ ":" + tokens[1] + "deleted");
                buf5=collate_get_or_delete_query("Key " + tokens[1] + " deleted");


                DpSend =
                        new DatagramPacket(buf5, buf5.length, ip, DpReceive.getPort());

                ds.send(DpSend);
               // printWriter.println("Key " + k + "deleted");
            } catch (NullPointerException a){

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

       // System.out.println(tokens[0]);
      //  System.out.println(tokens[1]);

        //key_value_store.remove(Integer.parseInt(tokens[1]));
       // System.out.println(key_value_store.isEmpty());

    }

    /**
     * Function to log to server/client
     * @param message is the message to be displayed on the logger
     */
    public static void Logger_Function(String message){
        LogRecord record = new LogRecord(Level.INFO, message);
        System.out.println(String.format(format,
                new java.util.Date(record.getMillis()),
                record.getSourceClassName(),
                record.getLoggerName(),
                record.getLevel().getLocalizedName(),
                record.getMessage(),
                String.valueOf(record.getThrown())));
    }


    /**
     * Concatenate Put Query
     * @param inp is the input to be concatenated
     * @return concatenated string
     */
    public static byte [] collate_put_query(String inp) {

        byte buf5[]=concatenateByteArraysputcorrect(inp.getBytes());

        String inp5 = inp ;
        //System.out.println("Concat Strings: " + inp5);

        return buf5;
    }

    /**
     * Concatenate get_or_delete_query byte array
     * @param inp is the input to be concatenated
     * @return concatenated byte array
     */
    public static byte [] collate_get_or_delete_query(String inp) {

        byte buf5[]=concatenateByteArraysgetanddeletecorrect(inp.getBytes());

        String inp5 = inp;
        //System.out.println("Concat Strings:" + inp5);

        return buf5;


    }

    /**
     * Concatenate get_or_delete_query
     * @param a
     * @return concatenated byte array
     */
    public static byte[] concatenateByteArraysgetanddeletecorrect(byte[] a) {
        ByteArrayOutputStream my_stream = new ByteArrayOutputStream();
        try {
            my_stream.write(a);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        byte[] concatenated_byte_array = my_stream.toByteArray();
       // for(int i=0; i< concatenated_byte_array.length ; i++) {
           // System.out.print(concatenated_byte_array[i] +" ");
        //}

        return concatenated_byte_array;

    }
    /**
     * Concatenate put query
     * @param a is the byte array to be concatenated
     * @return concatenated byte array
     */
    public static byte[] concatenateByteArraysputcorrect(byte[] a) {
        ByteArrayOutputStream my_stream = new ByteArrayOutputStream();
        try {
            my_stream.write(a);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        byte[] concatenated_byte_array = my_stream.toByteArray();
        //for(int i=0; i< concatenated_byte_array.length ; i++) {
           // System.out.print(concatenated_byte_array[i] +" ");
        //}

        return concatenated_byte_array;


    }

}