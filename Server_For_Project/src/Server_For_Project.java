import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;


/**
 * TCP SERVER
 */
public class Server_For_Project {

    /**PROTOCOLS FOR KEY VALUE STORE**/
    public static final int SUCCESS=0;
    public static final int INVALID_KEY=-1;
    public static final int CMD_PUT=1;
    public static final int CMD_GET=2;
    public static final int CMD_DEL=3;
    public static final int CMD_EXIT=4;



    /**Handle Milliseconds for Log**/
    public static final String format = "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS.%1$tL %1$Tp %2$s%n%4$s: %5$s%n";


    /**KEY VALUE STORE POPULATION**/
    public static Map<Integer, String> key_value_store  = new HashMap<Integer, String>() {{
        put(1, "value1");
        put(2, "value2");
        put(3, "value3");
        put(4, "value4");
        put(5, "value5");
    }};

    /**Send request to Client**/
    public static PrintWriter printWriter = null;

    /**Data content for Client**/
    public static DataInputStream fromClientStream = null;

    /**Data content for Server**/
    public static DataOutputStream toClientStream=null;

    /**Socket for new Client***/
    public static Socket clientHandler;

    /**New client**/
    public static int newClient;

    public static InetAddress addrString;

    public static void main(String[] args){
        try {

            /**Start Server**/

            int portNumber = Integer.parseInt(args[0]);



            ServerSocket server = new ServerSocket(portNumber);
            int counter=0;
            /**Check for Correct Program arguments**/
            if (args.length < 1) {
                System.out.println("Usage: java Server_For_Project  <portNumber> ");
                System.exit(1);
            }

            System.out.println("Server Started at port <" + portNumber + ">");
            //System.out.println("HashMap Empty? " + key_value_store.isEmpty());




            /**Check for client availability **/
            while (true){
                counter++;
                Socket serverClient = server.accept();
                //BufferedReader reader = new BufferedReader(new InputStreamReader(serverClient.getInputStream()));

                printWriter = new PrintWriter(serverClient.getOutputStream(), true);

                Logger_Function(" >> " + "Client No: " + counter + " started!");


                ClientHandler chandler= new ClientHandler(serverClient, counter );
                chandler.run();

            }



        } catch (IOException e) {
            //serverClient.close();
            e.printStackTrace();
        }

    }


    /**
     * Creates a new Client. Single threaded logic. Only one client at a time can connect
     */
    public static class ClientHandler {
        ClientHandler(Socket fromClientSocket, int counter) {
            clientHandler = fromClientSocket;
            newClient = counter;
        }

        //DataInputStream fromClientStream;
        //DataOutputStream toClientStream;

        /**
         * Start new client
         */
        public void run() {
            try {
                try {
                        fromClientStream = new DataInputStream(clientHandler.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                try {
                    toClientStream = new DataOutputStream(clientHandler.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int command = 0;
                int key = 0;
                String value = "";

                /**
                 * Check for valid commands which are PUT,GET,DELETE,POPULATE,EXIT.
                 * This is the Communication logic with the client
                 */
                while (true) {

                    command=fromClientStream.readInt();

                    if (command == CMD_EXIT) {
                        Logger_Function("Client sent EXIT.....EXITING");
                        clientHandler.close();
                        break;
                    }

                    else if (command == CMD_PUT) {
                        key=fromClientStream.readInt();
                       // Logger_Function("key from client: " + key);
                        value=fromClientStream.readUTF();
                        insert_key_value(key, value);
                       // System.out.println("HashMap Empty? "+ key_value_store.isEmpty());

                    } else if (command == CMD_GET) {
                        //Logger_Function("Client entered Get");
                        key=fromClientStream.readInt();
                        //Logger_Function("key from client: " + key);
                        get_key_value(key);
                       // System.out.println("HashMap Empty? "+ key_value_store.isEmpty());

                    } else if (command== CMD_DEL){
                        key=fromClientStream.readInt();
                        delete(key);
                        //System.out.println("HashMap Empty? "+ key_value_store.isEmpty());

                    }

                    for (HashMap.Entry<Integer,String> entry : key_value_store.entrySet()) {
                        System.out.println("Key = " + entry.getKey() +
                                ", Value = " + entry.getValue());
                    }


                }
                fromClientStream.close();
                toClientStream.close();

            } catch (Exception ex) {
                System.out.println(ex);
            } finally {
                Logger_Function("Client-" + newClient + " exited!!");

            }


        }
    }

        /**
         * Put into HashMap
         * @param k is the key to be put
         * @param v is the value to be put
         */
        public static void insert_key_value(int k, String v) {

            Logger_Function("Client entered Put");
            Logger_Function("key from client: " + k);
            Logger_Function("value from client: " + v);

            //while (true) {
            if (key_value_store.containsKey(k)) {
                System.out.println(key_value_store.containsKey(k));
               try {
                   //addrString = new InetAddress(clientHandler.getInetAddress());
                   // toClientStream.writeInt(INVALID_KEY);

                    Logger_Function("Error from " + "<" + clientHandler.getInetAddress() + ">" + "<" + clientHandler.getLocalPort() + ">"+ "Cannot PUT: Key already taken: Enter another key");
                    printWriter.println("Cannot PUT: Key already taken: Enter another key");

               } catch (NullPointerException a) {
               }

            } else {
                //try {
                Logger_Function("key from client: " + k);
                Logger_Function("value from client: " + v);

                    key_value_store.put(k, v);
                  //  toClientStream.writeInt(SUCCESS);
                    Logger_Function("From Server to Client-" + newClient + " Key:value pair- "+ k + ":" + v + " successfully stored ");
                    try {
                        printWriter.println("Key:value pair- " + k + ":" + v + " successfully stored ");
                    }catch (NullPointerException a){
                    }
                //} catch (IOException e) {
                   // System.out.print("201-is this where the exception is coming from?");
                    //e.printStackTrace();
               // }
            }


        }

        /**
         * Get from hashmap
         * @param k is the key to be gotten
         */
        public static void get_key_value(int k) {
            Logger_Function("Client entered Get");
            Logger_Function("key from client: " + k);

            if(key_value_store.isEmpty()){
                try {
                    Logger_Function("Error from " + "<" + clientHandler.getInetAddress() + ">" + "<" + clientHandler.getLocalPort()+ ">."+ "Cannot GET: HashMap is empty: PUT some values in" );
                    printWriter.println("Cannot GET: HashMap is empty: PUT some values in");
                } catch(NullPointerException a){

                }

            }

            else if (!key_value_store.containsKey(k)) {
                try {
                  //  toClientStream.writeInt(INVALID_KEY);
                    Logger_Function("Error from " + "<" + clientHandler.getInetAddress() + ">" + "<" + clientHandler.getLocalPort()+ ">."+ "Cannot GET: Key does not exist: Choose another key. Error from" + "<" + "address" + ">" + "<" + "portNumber"+ ">");
                    printWriter.println("Cannot GET: Key does not exist: Choose another key");

                } catch (NullPointerException a) {
               }

            } else {
                System.out.println(k);

                String value=key_value_store.get(k);
                //try {
                  //  toClientStream.writeInt(SUCCESS);
                    try {
                        Logger_Function("From Server to Client- " + newClient +" Got " + value + " from server " + "with key " + k);
                        printWriter.println("Got " + value + " from server ");
                    }catch(NullPointerException a){

                    }

               // } catch (IOException e) {
                  //  e.printStackTrace();
               // }
            }
        }

        /**
         * Delete from hashmap
         * @param k is the key to be deleted
         */
        public static void delete(int k) {
            Logger_Function("Client entered Delete");
            Logger_Function("key from client: " + k);
            if(key_value_store.isEmpty()){
                try {
                    Logger_Function("Error from " + "<" + clientHandler.getInetAddress() + ">" + "<" + clientHandler.getLocalPort()+ ">."+ "Cannot DELETE: HashMap is empty: PUT some values in" );
                    printWriter.println("Cannot DELETE: HashMap is empty: PUT some values in");
                } catch(NullPointerException a){

                }

            }
            else if (!key_value_store.containsKey(k)) {
               // try {
                 //   toClientStream.writeInt(INVALID_KEY);
                    try {
                        Logger_Function("Error from " + "<" + clientHandler.getInetAddress() + ">" + "<" + clientHandler.getLocalPort()+ ">." + "Cannot DELETE: Key does not exist: Choose another key. Error from" + "<" + "address" + ">" + "<" + "portNumber"+ ">");
                        printWriter.println("Cannot DELETE: Key does not exist: Choose another key");
                    } catch(NullPointerException a){

                    }

                //} catch (IOException e) {
                   // e.printStackTrace();
               // }

            } else {

                key_value_store.remove(k);
                    try {
                        Logger_Function("From Server to Client- " + newClient + ":" + k + "deleted");
                        printWriter.println("Key " + k + "deleted");
                    } catch (NullPointerException a){

                    }

            }

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


}
