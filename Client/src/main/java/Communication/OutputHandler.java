package Communication;
import Exceptions.*;
import GUI.UserOptions.GameStaticVersion;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class OutputHandler implements Runnable {
    private static OutputHandler outputHandler = null;

    private final PrintStream output;
    private final InputHandler inputHandler;

    public OutputHandler(Socket socket, InputHandler inputHandler) throws IOException, AllreadyHaveAnOutputHandler {
        if (outputHandler != null)
            throw new AllreadyHaveAnOutputHandler();

        output = new PrintStream(socket.getOutputStream());
        this.inputHandler = inputHandler;
        outputHandler = this;
    }

    public static OutputHandler getOutputHandler() throws NoOutPutHandlerException {
        if (outputHandler == null)
            throw new NoOutPutHandlerException();
        return outputHandler;
    }

    @Override
    public void run() { }

    void sendStringToServer(String string){
        output.println(string);
    }

    public synchronized void login(String username,String password) throws NoUserFoundException, PasswordIncorrectException {
        this.sendStringToServer("login " + username + " " + password);
        String answer = inputHandler.readNextLine();
        switch (answer){
            case "loggedIn":
                return;
            case "noUserFound":
                throw new NoUserFoundException();
            case "passwordIncorrect":
                throw new PasswordIncorrectException();
        }
    }

    public synchronized void signup(String name,String username,String password) throws PasswordNotLongEnoughException, PasswordInvalidCharacterException, UserExistsException {
        this.sendStringToServer("signup " + name + " " + username + " " + password);

        switch (inputHandler.readNextLine()){
            case "signedUp":
                return;
            case "passShort":
                throw new PasswordNotLongEnoughException();
            case "passCharsCorrect":
                throw new PasswordInvalidCharacterException();
            case "userExists":
                throw new UserExistsException();
        }
    }

    public synchronized String getMyData(){
        this.sendStringToServer("getMyData");
        String[] inputs = inputHandler.readNextLine().split(" ");

        String result = "Name: " + inputs[0] + "\n" +
                "Username: " + inputs[1] + "\n" +
                "Number Of Wins: " + inputs[2] + "\n" +
                "Number Of Looses: " + inputs[3];

        return result;

    }

    public synchronized void logout() throws notLoggedInException {
        this.sendStringToServer("logout");

        String result = inputHandler.readNextLine();

        switch (result){
            case "loggedOut":
                return;
            case "notLoggedIn":
                throw new notLoggedInException();
        }

    }

    public synchronized void blockUser(String usernameString) throws NoAaccountWithThisUsernameException {
        this.sendStringToServer("block");
        this.sendStringToServer(usernameString);

        String result = inputHandler.readNextLine();

        switch (result){
            case "blocked":
                return;
            case "noAccountWithThisUsername":
                throw new NoAaccountWithThisUsernameException();
        }
    }

    public synchronized ArrayList<String> getOnlineUsers(){
        sendStringToServer("viewOnlineUsers");

        String result = inputHandler.readNextLine();

        return new ArrayList<>(Arrays.asList(result.split("%")));
    }

    public synchronized ArrayList<String> searchOnlineUsers(String query) throws EmptyListException {
        sendStringToServer("searchUser " + query);

        String result = inputHandler.readNextLine();

        if (result.equals(""))
            throw new EmptyListException();


        return new ArrayList<>(Arrays.asList(result.split("%")));
    }

    public synchronized void request(String accountUsername) throws AccountIsNotOnlineException, YouAreBlockedException {
        sendStringToServer("requestGame " + accountUsername);

        String result = inputHandler.readNextLine();

        switch (result) {
            case "accountNotOnline": throw new AccountIsNotOnlineException();
            case "URBlocked": throw new YouAreBlockedException();
            case "sent": return;
        }
    }

    public synchronized ArrayList<String> getScoreBoard() {
        sendStringToServer("viewScoreBoard");

        String result = inputHandler.readNextLine();

        return new ArrayList<>(Arrays.asList(result.split("%")));
    }

    public synchronized ArrayList<String> getHistory() throws EmptyListException {
        sendStringToServer("history");

        String result = inputHandler.readNextLine();

        if (result.equals(""))
            throw new EmptyListException();

        return new ArrayList<>(Arrays.asList(result.split("%")));
    }

    public synchronized void accept(){
        sendStringToServer("accept");
        inputHandler.readNextLine();
    }

    public synchronized void reject() {
        sendStringToServer("reject");
        inputHandler.readNextLine();
    }

    public synchronized void changePassword(String oldPassword, String newPassword) throws PasswordIncorrectException {
        sendStringToServer("changePassword " + oldPassword + " " + newPassword);
        String response = inputHandler.readNextLine();

        switch (response){
            case "passChanged":
                return;
            case "passwordNotCorrect":
                throw new PasswordIncorrectException();
        }

    }

    public synchronized void message(String text) {
        outputHandler.sendStringToServer("message");
        outputHandler.sendStringToServer(text);
    }

    public synchronized void getValidMoves(int row,int column) throws InvalidPositionException, NoPieceAtLocationException {
        outputHandler.sendStringToServer("possibleMoves " + row + " " + column);

        String response = inputHandler.readNextLine();

        switch (response) {
            case "invalidPos" -> throw new InvalidPositionException();
            case "noPieceAtLoc" -> throw new NoPieceAtLocationException();
            default -> GameStaticVersion.highlightCells(response);
        }
    }

    public synchronized void move(int row1,int col1,int row2,int col2){
        outputHandler.sendStringToServer("movePiece");

    }
}
