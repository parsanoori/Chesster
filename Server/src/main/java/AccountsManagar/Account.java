package AccountsManagar;

import ChessGame.Game;
import Communication.ClientInputHandler;
import Communication.ClientOutputHandler;
import DataBase.DataBase;
import Exceptions.PasswordsDoNotMatchException;
import Exceptions.UsernameAlreadyExistsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static Tools.CharactersTools.isStringContainingOnlyLetterOrDigit;
import static Tools.SHA256Encryption.getStringHash;

public class Account implements Serializable {

    public final String name;
    private String username;
    private String password;

    private ArrayList<Game> gamesMadeByThisUser = new ArrayList<>();

    private int numberOfWins = 0;
    private int numberOfLooses = 0;

    private ArrayList<Account> blockedAccounts = new ArrayList<>();


    private ClientOutputHandler clientOutputHandler;
    private ClientInputHandler clientInputHandler;

    private Account currentOpponentOrRequestedAccount;

    private Game currentGame;

    public Account(String name, String username, String password) throws PasswordNotLongEnoughException, PasswordShouldntContainNonDigitsOrLatinLettersException, UsernameAlreadyExistsException {
        this.name = name;

        if (DataBase.userExists(username))
            throw new UsernameAlreadyExistsException();

        this.username = username;

        if (password.length() < 8)
            throw new PasswordNotLongEnoughException();

        if (!isStringContainingOnlyLetterOrDigit(password))
            throw new PasswordShouldntContainNonDigitsOrLatinLettersException();

        this.password = getStringHash(password);
    }

    public boolean checkPassword(String password)
    {
        return this.password.equals(getStringHash(password));
    }

    public void changePassword(String currentPassword,String newPassword) throws PasswordsDoNotMatchException {
        if (!checkPassword(currentPassword))
            throw new PasswordsDoNotMatchException();

        this.password = getStringHash(newPassword);
    }

    public void addGameToGamesMade(Game game){
        Objects.requireNonNull(game);
        gamesMadeByThisUser.add(game);
    }



    public void changeUserName(String username) { this.username = username; }

    public String getUsername() { return username; }

    public void incrementNumberOfWins(){
        numberOfWins++;
    }

    public void incrementNumberOfLooses(){
        numberOfLooses++;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public int getNumberOfLooses() {
        return numberOfLooses;
    }

    public ClientOutputHandler getClientOutputHandler() {
        return clientOutputHandler;
    }

    public void setClientOutputHandler(ClientOutputHandler clientOutputHandler) {
        this.clientOutputHandler = clientOutputHandler;
    }

    public ClientInputHandler getClientInputHandler() {
        return clientInputHandler;
    }

    public void setClientInputHandler(ClientInputHandler clientInputHandler) {
        this.clientInputHandler = clientInputHandler;
    }

    public void userLoggedOut(){
        clientInputHandler = null;
        clientOutputHandler = null;
        currentOpponentOrRequestedAccount = null;
    }

    public void sendRequest(Account account){
        account.getClientOutputHandler().sendRequest(account);
        currentOpponentOrRequestedAccount = account;
    }

    public void acceptGame(){
        Boolean isThisUserWhite = (new Random()).nextBoolean();
        currentGame = new Game(isThisUserWhite ? this : currentOpponentOrRequestedAccount,
                isThisUserWhite ? currentOpponentOrRequestedAccount : this
                );
        currentOpponentOrRequestedAccount.currentGame = currentGame;

        this.gamesMadeByThisUser.add(currentGame);
        currentOpponentOrRequestedAccount.gamesMadeByThisUser.add(currentGame);

        currentOpponentOrRequestedAccount.getClientOutputHandler().sendGameAccepted();
    }

    public void rejectGame(){
        currentOpponentOrRequestedAccount.getClientOutputHandler().sendGameRejected();
        currentOpponentOrRequestedAccount.currentOpponentOrRequestedAccount = null;
        currentOpponentOrRequestedAccount = null;
    }



    public Game getCurrentGame() {
        return currentGame;
    }

    public String gamesMadeToString (){
        return gamesMadeByThisUser.stream()
                .map(Game::toString)
                .reduce((string, string2) -> string + "%" + string2)
                .orElse("");
    }

    public ClientOutputHandler getOpponentsOutputHandler(){
        return currentOpponentOrRequestedAccount.getOpponentsOutputHandler();
    }

    public class PasswordNotLongEnoughException extends Exception {
    }

    public class PasswordShouldntContainNonDigitsOrLatinLettersException extends Throwable {
    }

    @Override
    public String toString() {
        return this.name + " " + this.getUsername() + " " + this.getNumberOfWins() + " " + this.getNumberOfLooses();
    }

    public void blockAccount(Account account){
        Objects.requireNonNull(account);
        blockedAccounts.add(account);
    }

    public boolean isBlocked(Account account){
        Objects.requireNonNull(account);
        return blockedAccounts.contains(account);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return username.equals(account.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}

