package nutritionalRecipe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeTest {

    @Test
    void calcNutritionFactsPerPortion() {
        Recipe myRecipe = new Recipe("myRecipe");
        Ingredient ing1 = new Ingredient("chocolate", 60, "Grams");
        Ingredient ing2= new Ingredient("sugar", 1, "Cup");
        ing1.setCalories(100);
        ing2.setCalories(60);
        ing1.setFat(10);
        ing2.setFat(0);
        ing1.setCarbs(15);
        ing2.setCarbs(20);
        ing1.setSugar(10);
        ing2.setSugar(20);
        ing1.setFiber(2);
        ing2.setFiber(0);
        ing1.setProtein(2);
        ing2.setProtein(0);
        myRecipe.setPortions(4);
        myRecipe.addIngredient(ing1);
        myRecipe.addIngredient(ing2);
        myRecipe.calcNutritionFactsPerPortion();
        assertEquals(myRecipe.getNutritionFacts().get(0), 40);
    }


}