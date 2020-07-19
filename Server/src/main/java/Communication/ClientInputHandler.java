package Communication;

import AccountsManagar.Account;
import ChessGame.Position;
import DataBase.DataBase;
import Exceptions.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class ClientInputHandler implements Runnable {
    private Socket socket;
    private Scanner input;
    private Account account;
    private ClientOutputHandler clientOutputHandler;

    public ClientInputHandler(Socket socket, ClientOutputHandler clientOutputHandler) {
        try {
            this.socket = socket;
            input = new Scanner(socket.getInputStream());
            this.clientOutputHandler = clientOutputHandler;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String command;
        while (true) {
            try {

                command = input.next();

                if (command.equals("Exit")) {
                    System.out.println("Client " + this.socket + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.socket.close();
                    System.out.println("Connection closed");
                    break;
                }

                switch (command) {
                    case "login":
                        login();
                        break;
                    case "logout":
                        logout();
                        break;
                    case "signup":
                        signup();
                        break;
                    case "getMyData":
                        getMyData();
                        break;
                    case "viewOnlineUsers":
                        viewOnlineUsers();
                        break;
                    case "changePassword":
                        changePassword();
                        break;
                    case "requestGame":
                        requestGame();
                        break;
                    case "accept":
                        accept();
                        break;
                    case "searchUser":
                        searchOnlineUsers();
                        break;
                    case "reject":
                        reject();
                        break;
                    case "isWhiteChecked":
                        isWhiteChecked();
                        break;
                    case "isBlackChecked":
                        isBlackChecked();
                        break;
                    case "isWhiteCheckMated":
                        isWhiteCheckMated();
                        break;
                    case "isBlackCheckMated":
                        isBlackCheckMated();
                        break;
                    case "viewScoreBoard":
                        viewScoreboard();
                        break;
                    case "history":
                        history();
                        break;
                    case "movePiece":
                        movePiece();
                        break;
                    case "message":
                        message();
                        break;
                    case "possibleMoves":
                        possibleMoves();
                        break;
                    case "block":
                        block();
                        break;
                }
            } catch (Exception e) {
                DataBase.removeAccountFromOnlineAccounts(account);
            }
        }
    }

    private void block() {
        String accountUsername = input.next();
        try {
            account.blockAccount(
                    DataBase.getAccountForOtherUses(accountUsername)
            );
            clientOutputHandler.sendStringToClient("blocked");
        } catch (NullPointerException e){
            clientOutputHandler.sendStringToClient("noAccountWithThisUsername");
        }

    }

    private void possibleMoves() {
        int x = input.nextInt();
        int y = input.nextInt();
        try {
            ArrayList<Position> positions = account.getCurrentGame().validPositionsForPieceAt(x, y);
            String result = "";
            for (Position position: positions)
                result += position.getX() +  " " + position.getY();
            clientOutputHandler.sendStringToClient(result);

        } catch (Position.InvalidPositionException e) {
            clientOutputHandler.sendStringToClient("invalidPos");
        } catch (NoPieceAtThisLocationException e) {
            clientOutputHandler.sendStringToClient("noPieceAtLoc");
        }
    }

    private void message() {
        String test = input.nextLine();

        try{
            account.getClientOutputHandler().sendStringToClient("msg\n" + test);
        } catch (Exception e){

        }
    }

    private void movePiece() {
        int fromX = input.nextInt();
        int fromY = input.nextInt();
        int toX = input.nextInt();
        int toY = input.nextInt();
        try {
            Objects.requireNonNull(account.getCurrentGame());
            account.getCurrentGame().move(fromX, fromY, toX, toY);
            clientOutputHandler.sendStringToClient("move " + fromX + " " + fromY + " " + toX + " " + toY);
        } catch (Position.InvalidPositionException e) {
            clientOutputHandler.sendStringToClient("invalidPos");
        } catch (Position.InvalidEndPositionException e) {
            clientOutputHandler.sendStringToClient("invalidEndPos");
        } catch (NoPieceAtThisLocationException e) {
            clientOutputHandler.sendStringToClient("noPieceAtLoc");
        } catch (ItsNotYourTurnException e) {
            clientOutputHandler.sendStringToClient("notUrTurn");
        }
    }

    private void history() {
        clientOutputHandler.sendStringToClient(
                account.gamesMadeToString()
        );
    }

    private void viewScoreboard() {
        clientOutputHandler.sendStringToClient(
                DataBase.getTopPlayers().stream()
                .map(Account::toString)
                .reduce((s1,s2) -> s1 + "%" + s2)
                .orElse("")
        );
    }

    private void isBlackCheckMated(){
        clientOutputHandler.sendStringToClient(
                String.valueOf(account.getCurrentGame().isFinished()
                        && !account.getCurrentGame().isWhiteWinner()
                )
        );
    }

    private void isWhiteCheckMated(){
        clientOutputHandler.sendStringToClient(
                String.valueOf(account.getCurrentGame().isFinished()
                        && account.getCurrentGame().isWhiteWinner()
                )
        );
    }

    private void isBlackChecked() {
        clientOutputHandler.sendStringToClient(
                String.valueOf(account.getCurrentGame().isBlackChecked())
        );
    }

    private void isWhiteChecked() {
        clientOutputHandler.sendStringToClient(
                String.valueOf(account.getCurrentGame().isWhiteChecked())
        );
    }

    private void searchOnlineUsers() {
        String query = input.next();

        String onlineUsersStartingWith = DataBase.onlineAccountsStartingWith(query).stream()
                .map(Account::toString)
                .reduce((s1 , s2) -> s1 + "%" + s2)
                .orElse("");

        clientOutputHandler.sendStringToClient(onlineUsersStartingWith);
    }



    private void reject() {
        account.rejectGame();
        clientOutputHandler.sendStringToClient("rejectSent");
    }

    private void accept() {
        account.acceptGame();
        clientOutputHandler.sendStringToClient("acceptanceSent");
    }

    private void requestGame() {
        String accountName = input.next();
        if (!DataBase.isOnlineForGame(accountName)){
            clientOutputHandler.sendStringToClient("accountNotOnline");
            return;
        }
        Account account = DataBase.getAccountForOtherUses(accountName);
        if (account.isBlocked(this.account)){
            clientOutputHandler.sendStringToClient("URBlocked");
            return;
        }
        clientOutputHandler.sendStringToClient("sent");
        account.sendRequest(this.account);
    }

    private void changePassword() {
        String currentPass, newPass;
        currentPass = input.next();
        newPass = input.next();
        try {
            account.changePassword(currentPass,newPass);
            clientOutputHandler.sendStringToClient("passChanged");
        } catch (PasswordsDoNotMatchException e) {
            clientOutputHandler.sendStringToClient("passwordNotCorrect");
        }
    }

    private void viewOnlineUsers() {
        clientOutputHandler.sendStringToClient(DataBase.getOnlineAccounts());
    }

    private void getMyData() {
        clientOutputHandler.sendStringToClient(
                account.toString()
        );

    }

    private void signup() {
        String name, username, password;
        name = input.next();
        username = input.next();
        password = input.next();
        try {
            Account account = new Account(name, username, password);
            DataBase.addAccount(account);
            clientOutputHandler.sendStringToClient("signedUp");
        } catch (Account.PasswordNotLongEnoughException e) {
            clientOutputHandler.sendStringToClient("passShort");
        } catch (Account.PasswordShouldntContainNonDigitsOrLatinLettersException e) {
            clientOutputHandler.sendStringToClient("passCharsCorrect");
        } catch (UsernameAlreadyExistsException e) {
            clientOutputHandler.sendStringToClient("userExists");
        }

    }

    private void logout() {
        if (account == null) {
            clientOutputHandler.sendStringToClient("notLoggedIn");
            return;
        }

        DataBase.removeAccountFromOnlineAccounts(account);
        account.userLoggedOut();
        account = null;
        clientOutputHandler.sendStringToClient("loggedOut");

    }

    private void login() {
        String username, password;
        username = input.next();
        password = input.next();

        try {
            Account account = DataBase.getAccount(username, password);
            this.account = account;
            DataBase.addOnlineAccount(account);
            account.setClientInputHandler(this);
            account.setClientOutputHandler(clientOutputHandler);
            clientOutputHandler.sendStringToClient("loggedIn");
        } catch (UserDoesntExistException e) {
            clientOutputHandler.sendStringToClient("noUserFound");
        } catch (PasswordsDoNotMatchException e) {
            clientOutputHandler.sendStringToClient("passwordIncorrect");
        }
    }
}
