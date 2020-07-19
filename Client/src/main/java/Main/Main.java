package Main;

import Communication.InputHandler;
import Communication.OutputHandler;
import Exceptions.AllreadyHaveAnInputHandlerException;
import Exceptions.AllreadyHaveAnOutputHandler;
import GUI.App;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static OutputHandler outputHandler;
    public static InputHandler inputHandler;


    public static void main(String[] args) throws IOException, AllreadyHaveAnOutputHandler, AllreadyHaveAnInputHandlerException {
        InetAddress inetAddress = InetAddress.getByName("localhost");
        Socket socket = new Socket(inetAddress,9080);

        inputHandler = new InputHandler(socket);
        outputHandler = new OutputHandler(socket, inputHandler);

        Thread output = new Thread(outputHandler);
        output.start();

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(inputHandler,0,500, TimeUnit.MILLISECONDS);

        App.startGUI();
    }


}
