package recipes;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;
import recipes.model.RecipeResponse;
import recipes.model.User;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.Map;


@RestController
public class Controller {

    @Autowired
    RecipeService recipeService;
    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        System.out.println(user);
        user.setPassword(encoder.encode(user.getPassword()));
        return new ResponseEntity<>(recipeService.userRegistration(user));
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable int id) {
        RecipeResponse recipe = recipeService.getRecipe(id);
        if (recipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(recipe, HttpStatus.OK);
        }
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe recipe) {
        int recipeId = recipeService.addRecipe(recipe);
        System.out.println(recipe);
        return new ResponseEntity<>(Map.of("id", recipeId), HttpStatus.OK);
    }

    @DeleteMapping("api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable int id) {
        return new ResponseEntity<>(recipeService.delete(id));
    }

    @PutMapping("api/recipe/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable int id, @Valid @RequestBody Recipe recipe) {
        return new ResponseEntity<>(recipeService.update(id, recipe));
    }

    @GetMapping("api/recipe/search")
    public ResponseEntity<?> searchByNameOrCategory(@RequestParam(required = false) String name, @RequestParam(required = false) String category) {
        if (StringUtils.isBlank(name) && StringUtils.isBlank(category)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (StringUtils.isBlank(name)) {
            return new ResponseEntity<>(recipeService.getRecipeByCategory(category), HttpStatus.OK);
        } else if (StringUtils.isBlank(category)) {
            return new ResponseEntity<>(recipeService.getRecipeContainsName(name), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "/test is accessed";
    }
}
