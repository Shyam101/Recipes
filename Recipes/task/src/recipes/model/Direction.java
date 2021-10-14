package recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Direction {

    @Id
    @GeneratedValue
    private int id;
    private String step;

    public Direction(String step) {
        this.step = step;
    }
}
