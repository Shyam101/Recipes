package recipes.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue
    Integer id;

    @Pattern(regexp = ".*@.*[.].*", message = "password should contain at least 8 characters and shouldn't be blank")
    private String email;

    @Pattern(regexp = "\\S{8,}", message = "password should contain at least 8 characters and shouldn't be blank")
    private String password;
}
