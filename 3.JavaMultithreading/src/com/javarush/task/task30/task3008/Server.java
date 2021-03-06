package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void sendBroadcastMessage(Message message) {
        for (Map.Entry<String, Connection> m : connectionMap.entrySet())
            try {
                m.getValue().send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Message was not delivered!");
            }
    }

    private static class Handler extends Thread {
        @Override
        public void run() {
            ConsoleHelper.writeMessage("Established new connection with remote address " + socket.getRemoteSocketAddress());
            String clientName = null;
            try (Connection connection = new Connection(socket)){
                ConsoleHelper.writeMessage("Connection with port " + connection.getRemoteSocketAddress());
                clientName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, clientName));
                sendListOfUsers(connection, clientName);
                serverMainLoop(connection, clientName);
            } catch (IOException | ClassNotFoundException e){
                ConsoleHelper.writeMessage("An error occurred while communicating with the remote address");
            } finally{
                if (clientName != null){
                    connectionMap.remove(clientName);
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, clientName));
                    ConsoleHelper.writeMessage(String.format("Connection with remote address (%s) is closed.", socket.getRemoteSocketAddress()));
                }
            }
        }

    private Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
        Message answer;
        while (true) {
            connection.send(new Message(MessageType.NAME_REQUEST));
            answer = connection.receive();
            if (answer.getType() == MessageType.USER_NAME)
                if (!answer.getData().isEmpty())
                    if (!connectionMap.containsKey(answer.getData())) {
                        connectionMap.put(answer.getData(), connection);
                        break;
                    }
        }
        connection.send(new Message(MessageType.NAME_ACCEPTED));
        return answer.getData();
    }

    private void sendListOfUsers(Connection connection, String userName) throws IOException {
        for (Map.Entry<String, Connection> m : connectionMap.entrySet()) {
            if (!m.getKey().equals(userName))
                connection.send(new Message(MessageType.USER_ADDED, m.getKey()));
        }
    }

    private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
        while (true) {
            Message m = connection.receive();
            if (m.getType() == MessageType.TEXT)
                sendBroadcastMessage(new Message(m.getType(), userName + ": " + m.getData()));
            else
                ConsoleHelper.writeMessage("Error!");
        }
    }

}

    public static void main(String[] args) {
        ConsoleHelper.writeMessage("Input server port:");
        try (ServerSocket serverSocket = new ServerSocket(ConsoleHelper.readInt())) {
            ConsoleHelper.writeMessage("Server started...");
            while (true) {
                new Handler(serverSocket.accept()).start();
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Something wrong, Server socket closed.");
        }
    }
}
