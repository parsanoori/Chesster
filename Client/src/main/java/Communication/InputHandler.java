package Communication;

import Exceptions.AllreadyHaveAnInputHandlerException;
import Exceptions.NoInputHandlerException;
import Exceptions.NoOutPutHandlerException;
import GUI.App;
import GUI.UserOptions.CreateRequestAlert;
import GUI.UserOptions.GameStaticVersion;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static Communication.OutputHandler.getOutputHandler;

public class InputHandler implements Runnable {

    private static InputHandler inputHandler = null;

    private Scanner scanner;

    public InputHandler(Socket socket) throws IOException, AllreadyHaveAnInputHandlerException {
        if (inputHandler != null)
            throw new AllreadyHaveAnInputHandlerException();
        scanner = new Scanner(socket.getInputStream());
        inputHandler = this;
    }

    public static InputHandler getInputHandler() throws NoInputHandlerException {
        if (inputHandler == null)
            throw new NoInputHandlerException();
        return inputHandler;
    }

    public synchronized String readNextLine(){
        return scanner.nextLine();
    }

    @Override
    public void run() {
        try {
            synchronized (getOutputHandler()) {
                if (scanner.hasNext()){
                    String command = scanner.next();
                    switch (command){
                        case "requestSentBy":
                            String account = scanner.next();
                            String wins = "Number Of Wins: " + scanner.next();
                            String looses = "Number Of Looses: " + scanner.next();
                            CreateRequestAlert.createRequestAlert(account,wins,looses);
                            break;
                        case "gameAccepted":
                            App.setRoot("UserOptions/ChessGame");
                            break;
                        case "gameRejected":
                            break;
                        case "msg":
                            GameStaticVersion.getMessage(inputHandler.readNextLine());
                            break;
                        case "move":
                            int x1 = scanner.nextInt();
                            int y1 = scanner.nextInt();
                            int x2 = scanner.nextInt();
                            int y2 = scanner.nextInt();
                            GameStaticVersion.visuallyMovePiece(x1,y1,x2,y2);
                    }
                }
            }
        } catch (NoOutPutHandlerException | IOException ignored) { }
    }

}
