package recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipe {

    private static final String BAD_REQUEST_ERROR = "all fields are required, string fields can't be blank, arrays should have at least one item";

    @Id
    @GeneratedValue
    Integer id;
    @NotBlank(message = BAD_REQUEST_ERROR)
    private String name;
    @NotBlank(message = BAD_REQUEST_ERROR)
    private String description;
    @NotBlank(message = BAD_REQUEST_ERROR)
    private String category;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "usr_fk", referencedColumnName = "user_id")
    private User user;

    @OneToMany(targetEntity = Ingredient.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ri_fk", referencedColumnName = "id")
    @Size(min = 1,message = BAD_REQUEST_ERROR)
    @NotNull
    private List<Ingredient> ingredients;

    @OneToMany(targetEntity = Direction.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "rd_fk", referencedColumnName = "id")
    @Size(min = 1, message = BAD_REQUEST_ERROR)
    @NotNull
    private List<Direction> directions;

}
