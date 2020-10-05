package nutritionalRecipe;

/**
 * This class allows the user to "store" the recipes in a "Recipe Box" which writes the recipe information to a CSV
 */

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RecipeBox {
    private static final String RECIPE_FILENAME = "UserRecipes.csv";
    private File userRecipeFile;

    /**
     * creates csv file if one does not exist
     * @throws IOException
     */
    public void createRecipeFile() throws IOException {
        userRecipeFile = new File(RECIPE_FILENAME);
        if (!userRecipeFile.exists()) {
            userRecipeFile.createNewFile();
        }
    }

    /**
     * writes Recipe object to RecipeBox, stores information in csv
     * @param recipe
     */
    public void storeRecipes(Recipe recipe){
        try {
            FileWriter fw= new FileWriter(userRecipeFile, true);
            CSVWriter csvw = new CSVWriter(fw);
            String recipeStr=recipe.toStringRecipe();
            String nutrition=recipe.nfToString();
            String[] recipeInfo ={recipeStr, nutrition};
            csvw.writeNext(recipeInfo);
            csvw.flush();
            csvw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
