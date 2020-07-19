package DataBase;

import AccountsManagar.Account;
import ChessGame.Game;
import Exceptions.PasswordsDoNotMatchException;
import Exceptions.UserDoesntExistException;
import Tools.StreamTools;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

public class DataBase {
    static private final Vector<Game> games = new Vector<>();
    static private final Vector<Account> accounts = new Vector<>();
    static private final Vector<Account> onlineAccounts = new Vector<>();

    public static void fillUsers(){
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(Paths.get("UsersData")))) {
            int size = objectInputStream.readInt();
            accounts.clear();
            for (int i = 0; i < size; i++)
                accounts.add((Account) objectInputStream.readObject());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeUsers(){
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get("UsersData")))){
            objectOutputStream.writeInt(accounts.size());
            for (Account account: accounts)
                objectOutputStream.writeObject(account);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void writeGames(){
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get("Games")))){
            objectOutputStream.writeObject(games.size());
            for (Game game: games)
                objectOutputStream.writeObject(game);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void fillGames(){
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(Paths.get("Games")))){
            games.clear();
            for (int i = 0; i < objectInputStream.readInt(); i++)
                games.add((Game) objectInputStream.readObject());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void readGamesAndUsers(){
        fillGames();
        fillUsers();
    }

    public static void writeGamesAndUsers(){
        writeGames();
        writeUsers();
    }

    public static void addAccount(Account account){
        Objects.requireNonNull(account);
        accounts.add(account);
    }

    public static void addGame(Game game){
        Objects.requireNonNull(game);
        games.add(game);
    }

    public static Account getAccount(String username,String password) throws PasswordsDoNotMatchException, UserDoesntExistException {
        Account account = accounts.stream().filter(a -> a.getUsername().equals(username)).findAny().orElseThrow(UserDoesntExistException::new);
        if (!account.checkPassword(password))
            throw new PasswordsDoNotMatchException();
        return account;
    }

    public static boolean userExists(String username){
        return accounts.stream().anyMatch(account -> account.getUsername().equals(username));
    }

    public static void addOnlineAccount(Account account){
        Objects.requireNonNull(account);
        onlineAccounts.add(account);
    }

    public static void removeAccountFromOnlineAccounts(Account account) {
        Objects.requireNonNull(account);
        onlineAccounts.remove(account);
    }

    public static String getOnlineAccounts() {
        return onlineAccounts.stream()
                .map(Account::toString)
                .reduce(((s1, s2) -> s1 + "%" + s2))
                .orElse("");
    }

    public static boolean isOnlineForGame(String accountName){
        return onlineAccounts.stream().anyMatch(account -> account.getUsername().equals(accountName));
    }

    public static Account getAccountForOtherUses(String username){
        return onlineAccounts.stream().filter(account -> account.getUsername().equals(username)).findAny().orElse(null);
    }

    public static ArrayList<Account> onlineAccountsStartingWith(String query){
        return StreamTools.getArrayListFromStream(
                onlineAccounts.stream().filter(account -> account.getUsername().startsWith(query))
        );
    }

    public static ArrayList<Account> getTopPlayers(){
        return StreamTools.getArrayListFromStream(accounts.stream()
                .sorted(Comparator.comparingInt(Account::getNumberOfWins)));
    }


}
