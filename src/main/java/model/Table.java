package model;

import common.constants.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Table {

    private int id;
    private int seats;

    public Table(int id, int seats) {
        this.id = id;
        this.seats = seats;
    }

    @Override
    public String toString() {
        return Constants.TABLE+ Constants.SPACE + id + Constants.SPACE + Constants.HYPHEN + Constants.SPACE + seats + Constants.SPACE + Constants.SEATS;
    }
}
