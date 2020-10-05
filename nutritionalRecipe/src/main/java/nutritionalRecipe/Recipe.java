package nutritionalRecipe;

/**
 * this class represents a Recipe object that includes a list of ingredients, a specified number of servings, and the nutrition facts
 * of one portion
 */

import java.io.IOException;
import java.util.ArrayList;

public class Recipe {
    private String name;
    private ArrayList<Ingredient> ingredients;
    private int numPortions;
    private ArrayList<Double> nutritionFacts;

    public Recipe(String name){
        ingredients=new ArrayList<Ingredient>();
        int numPortions;
        this.name=name;
        nutritionFacts=new ArrayList<Double>();
    }

    public String getName() {
        return name;
    }

    public void setPortions(int numPortions){
        this.numPortions = numPortions;
    }
    
    public ArrayList<Double> getNutritionFacts() {
		return nutritionFacts;
	}

	public int getPortions() {
        return numPortions;
    }

    /**
     * gets list of ingredients
     * @return
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * adds ingredient to recipe
     * @param newIngredient
     */
    public void addIngredient(Ingredient newIngredient){
        ingredients.add(newIngredient);
    }
    
    /**
     * calculate nutrition facts of one portion of the recipe
     */
    public void calcNutritionFactsPerPortion(){
	this.nutritionFacts=new ArrayList <Double>();
	
        double totalCalories=0;
        for(int i=0; i<ingredients.size(); i++) {
            totalCalories += (double) (ingredients.get(i).getCalories())/ (double) numPortions;
        }
        nutritionFacts.add(totalCalories);

        double totalGramsFat=0;
        for(int i=0; i<ingredients.size(); i++) {
            totalGramsFat += (double) (ingredients.get(i).getFat())/ (double) numPortions;
        }
        nutritionFacts.add(totalGramsFat);

        double totalProtein=0;
        for(int i=0; i<ingredients.size(); i++) {
            totalProtein += (double) (ingredients.get(i).getProtein())/ (double) numPortions;
        }
        nutritionFacts.add(totalProtein);

        double totalCarbohydrates=0;
        for(int i=0; i<ingredients.size(); i++) {
            totalCarbohydrates += (double) (ingredients.get(i).getCarbs())/ (double) numPortions;
        }
        nutritionFacts.add(totalCarbohydrates);

        double totalSugar=0;
        for(int i=0; i<ingredients.size(); i++) {
            totalSugar += (double) (ingredients.get(i).getSugar())/ (double) numPortions;
        }
        nutritionFacts.add(totalSugar);

        double totalFiber=0;
        for(int i=0; i<ingredients.size(); i++) {
            totalFiber += (double) (ingredients.get(i).getFiber())/ (double) numPortions;
        }
        nutritionFacts.add(totalFiber);

    }

    /**
     * converts recipe with ingredients to String
     * @return
     */
    public String toStringRecipe(){
        String ingListStr=" ";
        for(int i=0; i<ingredients.size(); i++){
            ingListStr+=ingredients.get(i).toString()+ " ";
        }
        return name + " serves " + getPortions() + " people," + ingListStr;
    }

    /**
     * converts nutrition facts to String
     * @return
     */
    public String nfToString(){
        return "calories:" + Math.round(nutritionFacts.get(0) * 10) / 10.0  + ", "+ "fat:" + Math.round(nutritionFacts.get(1) * 10) / 10.0 + "g, protein:" + Math.round(nutritionFacts.get(2) * 10) / 10.0 +"g, carbohydrates:"
                + Math.round(nutritionFacts.get(3) * 10) / 10.0+ "g, sugar:" + Math.round(nutritionFacts.get(4) * 10) / 10.0 + "g, fiber:" + Math.round(nutritionFacts.get(5) * 10) / 10.0 + "g";
    }


}
