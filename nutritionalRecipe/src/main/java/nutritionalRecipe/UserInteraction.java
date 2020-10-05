package nutritionalRecipe;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class handles the console interaction with the user
 */
public class UserInteraction {
    private String recipeName;
    private ArrayList<Ingredient> listOfIngredients = new ArrayList<Ingredient>();
    private int numberOfPortions;

    /**
     * This method asks the user for the ingredients in the recipe
     * 
     * @return an object with ingredients, amount and UOM
     */
    public Recipe createRecipe() {
	NutritionApiCaller callApi = new NutritionApiCaller();

	String currentIngredient = "";// Every single ingredient
	Scanner in = new Scanner(System.in);

	System.out.println(
		"***********************************************************************************************************************"
		+ "\n                     KnowCal\n"
		+"***********************************************************************************************************************\n"
		+ "" );
	System.out.println("Please enter the name of a recipe");
	recipeName = in.nextLine();

	// Create recipe object
	Recipe currentRecipe = new Recipe(recipeName);

	// Asks for number of portions
	System.out.println("\nHow many servings/portions are in this recipe?");
	numberOfPortions = in.nextInt();
	in.nextLine();
	currentRecipe.setPortions(numberOfPortions);

	// Asks for ingredient name
	System.out.println("\nNow let's enter the ingredients and the quantities.");
	System.out.println("Enter the ingredient name (type END when done): ");
	currentIngredient = in.nextLine();

	if (currentIngredient.toUpperCase().equals("END") && listOfIngredients.size()==0 ) {
	    System.out.println("No ingredients have been entered. Execution terminated.");
	    System. exit(0); 
	}
	
	Ingredient ing = new Ingredient(currentIngredient);

	// Start entering ingredients
	    
	while (!currentIngredient.toUpperCase().equals("END")) {

	    // Asks for UOM
	    System.out.println(
		    "\nSelect the unit of measure:  Whole, Gram, Kilogram, Liter, Milliliter, Ounce, Pound, Pinch,\n                             "
			    + "Fluid Ounce, Gallon, Pint, Quart, Drop, Cup, Tablespoon, Teaspoon.");
	    String uom = in.nextLine();

	    // Validates UOM
	    while (!uom.toLowerCase().equals("whole") && !uom.toLowerCase().equals("gram") && !uom.toLowerCase().equals("kilogram") && !uom.toLowerCase().equals("liter")
		    && !uom.toLowerCase().equals("Milliliter") && !uom.toLowerCase().equals("Ounce") && !uom.toLowerCase().equals("Pound") && !uom.toLowerCase().equals("Pinch")
		    && !uom.toLowerCase().equals("fluid ounce") && !uom.toLowerCase().equals("gallon") && !uom.toLowerCase().equals("pint")
		    && !uom.toLowerCase().equals("quart") && !uom.toLowerCase().equals("drop") && !uom.toLowerCase().equals("cup") && !uom.toLowerCase().equals("tablespoon")
		    && !uom.toLowerCase().equals("teaspoon")) {
		System.out.println("Incorrect entry. Please try again");
		System.out.println(
			"\nSelect the unit of measure:  Whole, Gram, Kilogram, Liter, Milliliter, Ounce, Pound, Pinch, Fluid Ounce, Gallon, Pint, Quart, Drop, Cup, Tablespoon, Teaspoon");
		uom = in.nextLine();
	    }
	    ing.setUnitOfMeasure(uom);

	    // Asks for quantity
	    System.out.println("\nEnter the quantity: ");
	    double quantity = in.nextDouble();
	    in.nextLine();
	    ing.setAmount(quantity);

	    // Validate ingredient
	    int ingExists = callApi.makeNutritionalCall(ing);

	    while (ingExists == -1) {
		System.out.println("\nI can't find the ingredient. Please check the spelling. You entered: \""
			+ currentIngredient + "\" \nIs this the correct name? (Y/N)");
		String answer = in.nextLine();
		if (answer.equals("N")) {
		    System.out.println("\nEnter the ingredient name: ");
		    currentIngredient = in.nextLine();
		    ingExists = callApi.makeNutritionalCall(ing);
		} else if (answer.equals("Y")) {
		    ingExists = 2;
		} else {
		    System.out.println("\nIncorrect entry. Please try again.");
		}

	    }

	    // These lines only execute if the ingredient is new
	    if (ingExists == 2) {
		System.out.println(
			"**************************************************************************************\n"
				+ "You have entered a new ingredint.\nIn order to calculate the nutrional value of the recipe, "
				+ "we need a bit more information.\n");
		System.out.println("Let's start with calories. How many calories are in " + ing.getAmount() + " "
			+ ing.getUnitOfMeasure() + " of " + ing.getName());
		ing.setCalories(in.nextInt());
		in.nextLine();
		System.out.println("Then, let's look at carbs. How many carbs are in " + ing.getAmount() + " "
			+ ing.getUnitOfMeasure() + " of " + ing.getName());
		ing.setCarbs(in.nextInt());
		in.nextLine();
		System.out.println("Now it's fat turn. How much fat is in " + ing.getAmount() + " "
			+ ing.getUnitOfMeasure() + " of " + ing.getName());
		ing.setFat(in.nextInt());
		in.nextLine();
		System.out.println("How about proteins?. How much protein is in " + ing.getAmount() + " "
			+ ing.getUnitOfMeasure() + " of " + ing.getName());
		ing.setProtein(in.nextInt());
		in.nextLine();
		System.out.println("And sugar?. How much sugar is in " + ing.getAmount() + " " + ing.getUnitOfMeasure()
		+ " of " + ing.getName());
		ing.setSugar(in.nextInt());
		in.nextLine();
		System.out.println("And finally fibers. How much fiber is in " + ing.getAmount() + " "
			+ ing.getUnitOfMeasure() + " of " + ing.getName());
		ing.setFiber(in.nextInt());
		in.nextLine();
	    }

	    // Adds the ingredient to the recipe
	    currentRecipe.addIngredient(ing);

	    // Asks for next ingredient name
	    System.out.println("\nEnter the next ingredient name (type END when done): ");
	    currentIngredient = in.nextLine();

	    // Creates a new instance of Ingredient
	    if (!currentIngredient.toUpperCase().equals("END")) {
		ing = new Ingredient(currentIngredient);
	}

	}
	// Prints list of ingredients and amounts
	System.out.println(
		"***********************************************************************************************************************");
	System.out.printf("%-117s%2s\n",
		"* These are the ingredients entered to prepare " + numberOfPortions + " portions of " + recipeName , "*");
	System.out.printf("%-50s%12s%57s\n", "* Ingredient Name", "Quantity", "*");
	for (Ingredient ingredient : currentRecipe.getIngredients()) {

	    System.out.printf("%-50s%10.2f%-58s%1s\n", "* " + ingredient.getName(), ingredient.getAmount(),
		    " " + ingredient.getUnitOfMeasure(), "*");
	}
	System.out.println(
		"***********************************************************************************************************************");
	
	return currentRecipe;
    }

    /**
     * This method asks the user to guess the calories per portion
     * 
     * @param recipe
     */
    public Recipe guessCalories(Recipe recipe) {
	    recipe.calcNutritionFactsPerPortion();
	    int numberOfPortions = recipe.getPortions();
		double caloriesPerPortion = recipe.getNutritionFacts().get(0);
		int maxCalories = 0;
		Ingredient maxIngredient = recipe.getIngredients().get(0);
	
		// Calculates ingredient with highest calorie count
		for (Ingredient ingredient : recipe.getIngredients()) {
		    if (ingredient.getCalories() >= maxCalories) {
				maxCalories = ingredient.getCalories();
				maxIngredient = ingredient;
		    }
		}
	
		// Asks user to guess the calories
		Scanner in = new Scanner(System.in);
		System.out.println(
			"\n\n\n\n***********************************************************************************************************************");
		System.out.println("Can you guess how many calories your recipe has per portion?.\nEnter the amount: ");
		Double guessedCalories = in.nextDouble();
		in.hasNextLine();
	
		// Compares the answer with the correct answer +/- 2.5% calories
		double tolerance = caloriesPerPortion * 0.025;
		if (Math.abs(caloriesPerPortion - guessedCalories) > tolerance) {
		    System.out.println(
			    "\n***********************************************************************************************************************");
		    System.out.println("You're off by more than 2.5%. The correct amount is: " + caloriesPerPortion
			    + ".\n\nThe ingredient with the highest number of calories is " + maxIngredient.getName() + " with "
			    + maxCalories / numberOfPortions + " calories per portion.\nThat's "
			    + (int)((maxCalories / numberOfPortions / caloriesPerPortion)*100)
			    + "% of the total.\n\n\nWe're going to look for a potential substitute.\n");
		} else {
		    System.out.println(
			    "\n***********************************************************************************************************************"
			    + "\nThat's great! You guessed the correct amount within a 2.5% tolerance. The correct amount is: "
				    + caloriesPerPortion + ".\nThe ingredient with the highest number of calories is "
				    + maxIngredient.getName() + " with " + maxCalories / numberOfPortions
				    + " calories per portion.\nThat's " + maxCalories / numberOfPortions / caloriesPerPortion
				    + "% of the total.\n\n\nWe're going to look for a potential subsitute.");
		}
		NutritionApiCaller callApi = new NutritionApiCaller();
		ArrayList<Ingredient> subs = callApi.getSubstitutions(maxIngredient);
		if (subs == null) {
			System.out.println("Unfortunately we could not find a "
					+ "lower calorie substitute for " + maxIngredient.getName() + ".\n");
		} else {
			double calsPerPortion = 0.0;
			int subCals=0;
			String subString = "";
			int counter = 0;
			for (Ingredient i : subs) {
				counter++;
				subCals = subCals + i.getCalories();
				subString = subString + i.getAmount() + " " + 
				i.getUnitOfMeasure() + " " + 
						i.getName();
				if (counter != subs.size()) {
					subString = subString + " and ";
				}
			}
			calsPerPortion = (double) subCals / (double) recipe.getPortions();
			int calsPerPortionSaved = (int) ((maxIngredient.getCalories() / recipe.getPortions()) - calsPerPortion);
			System.out.println("Using " + subString + 
					" instead of " + maxIngredient.getAmount() + " " + 
					maxIngredient.getUnitOfMeasure() + " " +  maxIngredient.getName() + 
					" would save you " + calsPerPortionSaved + " calories per portion.  Would you like to "
					+ "make this substitution (y/n)");
			String wantToReplace = "";
			while(!wantToReplace.equals("y") && !wantToReplace.equals("n")) {
				wantToReplace = in.nextLine().toLowerCase();
				if (!wantToReplace.equals("y") && !wantToReplace.equals("n") && !wantToReplace.equals("")) {
					System.out.println("We don't understand.  Please type y or n.");
				}
			}
			if (wantToReplace.equals("y")) {
				recipe.getIngredients().remove(maxIngredient);
				for (Ingredient i : subs) {
					recipe.addIngredient(i);
				}
				recipe.calcNutritionFactsPerPortion();
			}
		}
		
		in.close();
		return recipe;

    }
}
