package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import recipes.model.Recipe;
import recipes.model.RecipeResponse;
import recipes.model.User;
import recipes.persistent.RecipeRepository;
import recipes.persistent.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public RecipeResponse getRecipe(int recipeId) {
        return recipeToRecipeResponse(recipeRepository.findRecipeById(recipeId));
    }

    public int addRecipe(Recipe recipe) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName());

        recipe.setUser(user);
        recipe.setDate(LocalDateTime.now());
        Recipe savedRecipe = recipeRepository.save(recipe);
        return savedRecipe.getId();
    }

    public HttpStatus delete(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName());

        Recipe recipe = recipeRepository.findRecipeById(id);
        if (recipe == null)
            return HttpStatus.NOT_FOUND;

        if (recipe.getUser().getId() != user.getId()) {
            return HttpStatus.FORBIDDEN;
        }

        recipeRepository.deleteById(id);
        return HttpStatus.NO_CONTENT;
    }

    public HttpStatus update(int id, Recipe recipe) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(auth.getName());

        Recipe dbRecipe = recipeRepository.findRecipeById(id);
        if (dbRecipe == null)
            return HttpStatus.NOT_FOUND;

        if (dbRecipe.getUser().getId() != user.getId()) {
            return HttpStatus.FORBIDDEN;
        }
        recipe.setUser(user);
        recipe.setDate(LocalDateTime.now());
        recipe.setId(id);
        recipeRepository.save(recipe);
        return HttpStatus.NO_CONTENT;
    }

    public List<RecipeResponse> getRecipeContainsName(String name) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name)
                .stream()
                .map(recipe -> recipeToRecipeResponse(recipe))
                .collect(Collectors.toList());
    }

    public List<RecipeResponse> getRecipeByCategory(String category) {
        return recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category)
                .stream()
                .map(recipe -> recipeToRecipeResponse(recipe))
                .collect(Collectors.toList());
    }

    private RecipeResponse recipeToRecipeResponse(Recipe recipe) {

        if (recipe == null)
            return null;

        List<String> ingredients = recipe.getIngredients()
                .stream()
                .map(ingredient -> ingredient.getIngredientName())
                .collect(Collectors.toList());

        List<String> directions = recipe.getDirections()
                .stream()
                .map(direction -> direction.getStep())
                .collect(Collectors.toList());

        return new RecipeResponse(recipe.getName(), recipe.getDescription(), ingredients, directions, recipe.getCategory(), recipe.getDate());
    }

    public HttpStatus userRegistration(User user) {
        if (userRepository.findUserByEmail(user.getEmail()) == null) {
            userRepository.save(user);
            return HttpStatus.OK;
        }

        return HttpStatus.BAD_REQUEST;
    }
}
