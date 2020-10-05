package nutritionalRecipe;

import java.io.IOException;

public class Runner {
    public static void main(String[] args) {

        UserInteraction ui = new UserInteraction();
        Recipe recipe = ui.createRecipe();
        recipe = ui.guessCalories(recipe);
        
        RecipeBox rb = new RecipeBox();
        try {
		    rb.createRecipeFile();
        } catch (IOException e) {
		    e.printStackTrace();
        }
        rb.storeRecipes(recipe);
        System.out.println("We have saved the recipe in a file \"UserRecipes.csv\"");
    }
}
