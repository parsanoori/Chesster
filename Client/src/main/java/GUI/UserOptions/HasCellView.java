package GUI.UserOptions;

import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public abstract class HasCellView {
    abstract void updateError(String string);

    protected void fillList(ArrayList<String> accounts, ListView<CellView> listView) {
        ArrayList<CellView> cellViews = new ArrayList<>();
        for (String account : accounts) {
            String[] strings = account.split(" ");
            CellView cellView = new CellView(strings[0], strings[1], strings[2], strings[3],this);
            cellViews.add(cellView);
        }
        listView.setItems(FXCollections.observableList(cellViews));
    }

}
