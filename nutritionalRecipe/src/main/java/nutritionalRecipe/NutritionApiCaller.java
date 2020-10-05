package nutritionalRecipe;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

public class NutritionApiCaller {
	private OkHttpClient client = new OkHttpClient();
	private String nutritionURLBase = "https://api.edamam.com/api/food-database/";
	private String appId = "a18ca0db";
	private String appKey = "d055692ab758629576da744a74a9cfb4";
	public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
	
	public NutritionApiCaller() {
		
	}
	
	/**
	 * Gets nutritiional information for an ingredient
	 * @param ingredient the ingredient you want nutritional information for (must have an amount and unit of measure)
	 * @return -1 if the api call failed and 1 if the call succeeded
	 */
	public int makeNutritionalCall(Ingredient ingredient) {
		// Prepare the api request to get the food id
		String name = urlEncodeString(ingredient.getName());
		String requestUrl = nutritionURLBase + "parser?ingr=" + name +
				"&app_id=" + appId + "&app_key="  + appKey; 
		Request request = new Request.Builder()
				.url(requestUrl)
				.build();
		try {
			// Get the response with the food id
			Response response = client.newCall(request).execute();
			// Use the parser to extract the food id from the response
			JSONParser parser = new JSONParser();
			try {
				JSONObject json = (JSONObject) parser.parse(response.body().string());
				JSONArray temp = (JSONArray) json.get("hints");
				if (temp.size() == 0) {
					return -1;
				}
				JSONObject currItem = (JSONObject) temp.get(0);
				JSONObject currItemInfo = (JSONObject) currItem.get("food");
				String foodId = (String) currItemInfo.get("foodId");
				
				// Prepare the post request body to get nutritional information 
				String ingredientJSON = makePostRequestBody(ingredient.getUnitOfMeasure(),
						foodId);
				
				// Execute the post request to get nutritional information
				String nutritionalInfoJSONString = doPostRequest(ingredientJSON);
				
				if (nutritionalInfoJSONString == null) {
					return -1;
				}
				if (parsePostResponse(ingredient, nutritionalInfoJSONString) == 1) {
					return 1;
				} else {
					return -1;
				}		
			} catch (ParseException e) {
				return -1;
			}
			
		} catch (IOException e) {
			return -1;
		}
		
	}
	
	/**
	 * Populates the nutritional info for ingredient with data in api response
	 * @param ingredient The ingredient you want to populate the nutritional info for
	 * @param JSONstring The string containing the response from the api request
	 * @return -1 if post response could not be parsed and 1 if it could
	 */
	private int parsePostResponse(Ingredient ingredient, String JSONstring) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject) parser.parse(JSONstring);
			if (json.containsKey("totalNutrients")) {
				json = (JSONObject) json.get("totalNutrients");
			} else {
				return -1;
			}
			
			// Get and set calories
			JSONObject currJson = (JSONObject) json.get("ENERC_KCAL");
			if (currJson != null) {
				double currVal = (Double) currJson.get("quantity");
				ingredient.setCalories((int) (currVal * ingredient.getAmount())); 
			} else {
				ingredient.setCalories(0);
			}
			
			// Get and set carbs
			currJson = (JSONObject) json.get("CHOCDF");
			if (currJson != null) {
				double currVal = (Double) currJson.get("quantity");
				ingredient.setCarbs((int) (currVal * ingredient.getAmount())); 
			} else {
				ingredient.setCarbs(0);
			}
			
			// Get and set protein
			currJson = (JSONObject) json.get("PROCNT");
			if (currJson != null) {
				double currVal = (Double) currJson.get("quantity");
				ingredient.setProtein((int) (currVal * ingredient.getAmount())); 
			} else {
				ingredient.setProtein(0);
			}
			
			// Get and set fat
			currJson = (JSONObject) json.get("FAT");
			if (currJson != null) {
				double currVal = (Double) currJson.get("quantity");
				ingredient.setFat((int) (currVal * ingredient.getAmount())); 
			} else {
				ingredient.setFat(0);
			}
			
			// Get and set sugar
			currJson = (JSONObject) json.get("SUGAR");
			if (currJson != null) {
				double currVal = (Double) currJson.get("quantity");
				ingredient.setSugar((int) (currVal * ingredient.getAmount())); 
			} else {
				ingredient.setSugar(0);
			}
			
			// Get and set fiber
			currJson = (JSONObject) json.get("FIBTG");
			if (currJson != null) {
				double currVal = (Double) currJson.get("quantity");
				ingredient.setFiber((int) (currVal * ingredient.getAmount())); 
			} else {
				ingredient.setFiber(0);
			}
			
			return 1;
		} catch (ParseException e) {
			return -1;
		}
		
	}
	
	/**
	 * Makes the post request to get the nutritional info
	 * @param ingredientJSON the JSON string to send in the request body
	 * @return String containing the response with nutritional info or null if there's an error
	 */
	private String doPostRequest(String ingredientJSON) {
		RequestBody body = RequestBody.create(JSON, ingredientJSON);
		String requestUrl = nutritionURLBase + "nutrients?app_id=" + appId + "&app_key="  + appKey;
		Request request = new Request.Builder()
				.url(requestUrl)
				.post(body)
				.build();
		Response response;
		try {
			response = client.newCall(request).execute();
			return response.body().string();
		} catch (IOException e) {
			return null;
		}	
	}
	
	/**
	 * Creates the body for the post request to get nutritional info
	 * @param unitOfMeasure the unit of measure for the ingredient
	 * @param foodId the id of the food
	 * @return String with the body for the post request.
	 */
	public String makePostRequestBody(String unitOfMeasure, String foodId) {
		JSONObject currItemInfo = new JSONObject();
		currItemInfo.put("quantity", 1);
		if (unitOfMeasure.equals("Whole")) {
			unitOfMeasure = "unit";
		} else {
			// if the unit of measure starts with an upper case letter 
			// then change it to lower case per api docs
			char[] temp = unitOfMeasure.toCharArray();
			if (temp[0] >= 65 && temp[0] <= 90) {
				temp[0] = (char) (temp[0] + 32);
				unitOfMeasure = new String(temp);
			}
		}
		String measureURI = "http://www.edamam.com/ontologies/edamam.owl#Measure_";
		currItemInfo.put("measureURI", measureURI + unitOfMeasure);
		currItemInfo.put("foodId", foodId);
		
		JSONArray ingredients = new JSONArray();
		ingredients.add(currItemInfo);
		
		JSONObject returnObject = new JSONObject();
		returnObject.put("ingredients", ingredients);
		
		return returnObject.toString();
		
	}

	/**
	 * URL encodes a string by changing spaces to "%20"
	 * @param s the string to URL encode
	 * @return the URL encoded string
	 */
	public String urlEncodeString(String s) {
		StringBuilder builder = new StringBuilder();
		char[] sNew = s.toCharArray();
		
		for (int i=0; i<sNew.length; i++) {
			if (sNew[i] == ' ') {
				builder.append('%');
				builder.append('2');
				builder.append('0');
			} else {
				builder.append(sNew[i]);
			}
		}
		
		return builder.toString();
	}
	
	/**
	 * gets suggestions for lower calorie substitutions for an ingredient 
	 * (because of free api version we only check 1 possible substitution)
	 * @param ingredient the ingredient you want to get substitutions for
	 * @return returns an ArrayList with the ingredients that should be 
	 * added or null if no lower calorie substitutions exist
	 */
	public ArrayList<Ingredient> getSubstitutions(Ingredient ingredient) {
		String requestUrl = "https://api.spoonacular.com/food/ingredients/substitutes?ingredientName="
				+  ingredient.getName() + "&apiKey=30af85d59d6d42c89b9f5be1b21037b6";
		Request request = new Request.Builder()
				.url(requestUrl)
				.build();
		try {
			Response response = client.newCall(request).execute();
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response.body().string());
			JSONArray substitutes;
			
			// Check if the api has found valid substitutions
			if (json.containsKey("substitutes")) {
				substitutes = (JSONArray) json.get("substitutes");
			} else {
				return null;
			}
			
			// Only get 1 of the possible substitutes because we are using free tier of the API
			String substitute = (String) substitutes.get(substitutes.size()-1);
			
			// Separate out the ingredients in the possible substitution
			ArrayList<Ingredient> newIngredients = parseSubstitutionString(ingredient, substitute);
			if (newIngredients == null) {
				return null;
			}
			
			// Check if the substitution ingredients have fewer calories than the original ingredient
			boolean goodSub = shouldMakeSubstitution(ingredient, newIngredients);
			if (goodSub == true) {
				return newIngredients;
			} else {
				return null;
			}
			
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * checks if a substitution should be made based on calorie count
	 * @param ingredient the ingredient you're checking to see if it should be removed
	 * @param newIngredients list of ingredients that would be replacing ingredient
	 * @return true if the substitution has fewer calories and false if otherwise
	 */
	private boolean shouldMakeSubstitution(Ingredient ingredient, ArrayList<Ingredient> newIngredients) {
		int subCals = 0;
		int temp;
		for (int i=0; i<newIngredients.size(); i++) {
			temp = makeNutritionalCall(newIngredients.get(i));
			if (temp == -1) {
				return false;
			} else {
				subCals = subCals + newIngredients.get(i).getCalories();
			}
		}
		if (subCals < ingredient.getCalories()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * gets relevant info from the string returned from ingredient sub api call
	 * @param ingredient the ingredient you're looking for substitutions for
	 * @param substitute the string returned from ingredient sub api call with relevant info to be parsed
	 * @return returns an Arraylist of substitute ingredients if info can be parsed and null otherwise
	 */
	public ArrayList<Ingredient> parseSubstitutionString(Ingredient ingredient, String substitute) {
		String[] parts = substitute.split("\\s*=\\s*");
		double amountOrig;
		String unitsOrig;
		
		// Get all of the info for the original ingredient in the substitution string
		// Get amount
		amountOrig = getAmountIngFromString(parts[0]);
		// Get units
		unitsOrig = checkGetValidUnits(getIngUnitsFromString(parts[0]));
		if (unitsOrig == null) {
			return null;
		}
		
		
		// Get all of the info for the substitute ingredients in the substitution string
		
		// Figure out how many different ingredients are in this substitution
		String sepSeq = null;
		String[] substitutes;
		if (parts[1].contains("\\sand\\s")) {
			sepSeq = "and";
		} else if (parts[1].contains("+")) {
			sepSeq = "+";
		} else if (parts[1].contains("&")) {
			sepSeq = "&";
		}
		
		if (sepSeq == null) {
			substitutes = new String[]{parts[1]};
		} else {
			substitutes = parts[1].split(sepSeq);
		}
		
		// Get info for each ingredient in the substitution
		ArrayList<Ingredient> returnList = new ArrayList<Ingredient>();
		double currAmount;
		String currUnits;
		String currName;
		for (int i=0; i<substitutes.length; i++) {
			
			// get amount for the substitute ingredient
			currAmount = getAmountIngFromString(substitutes[i]);
			if (currAmount == 0.0) {
				return null;
			}
			double ratio = currAmount / amountOrig;
			currAmount = ratio * ingredient.getAmount();
			
			// get units for the substitute ingredient
			currUnits = checkGetValidUnits(getIngUnitsFromString(substitutes[i]));
			if (currUnits == null) {
				return null;
			}
			
			// check to make sure we are working in same units as original ingredient 
			// so we can make a valid substitution
			if (!unitsOrig.equals(currUnits)) {
				return null;
			}
			currUnits = ingredient.getUnitOfMeasure();
			
			// get name of substitute ingredient
			currName = getIngNameFromString(substitutes[i]);
			if (currName == null) {
				return null;
			}
			
			returnList.add(new Ingredient(currName, currAmount , currUnits));
			
		}
		
		return returnList;
	}
	
	/**
	 * checks if a string representing units is valid
	 * @param s String that represents a unit of measurement
	 * @return the proper version of the units or null if it doesn't exist
	 */
	public String checkGetValidUnits(String s) {
		HashMap<String, String> map = makeValidUnitsMap();
		return map.get(s.toLowerCase());
	}
	
	/**
	 * makes a hashmap containing valid units of measurement for an ingredient
	 * @return A hashmap containing the valid units of measurement 
	 */
	public HashMap<String, String> makeValidUnitsMap() {
		String[] validKeys = new String[] {"gram", "g", "kilogram", "kg", "liter", "l", 
				"milliliter", "ml", "ounce", "oz", "pound", "lb", "pinch", "fluid ounce", 
				"gallon", "gal", "pint", "p", "quart", "qt", "cup", "c", "tablespoon", 
				"tbsp", "teaspoon", "tsp"};
		
		String[] validValues = new String[] {"gram", "gram", "kilogram", "kilogram", "liter", "liter", 
				"milliliter", "milliliter", "ounce", "ounce", "pound", "pound", "pinch", "fluid ounce", 
				"gallon", "gallon", "pint", "pint", "quart", "quart", "cup", "cup", "tablespoon", 
				"tablespoon", "teaspoon", "teaspoon"};
		
		HashMap<String, String> returnMap = new HashMap<String, String>();
		for (int i=0; i<validKeys.length; i++) {
			returnMap.put(validKeys[i], validValues[i]);
		}
				
		return returnMap;
	}
	
	/**
	 * extracts the ingredient name from a string that comes from substitution api call
	 * @param s the string to extract the ingredient name from
	 * @return a String representing the name of the ingredient or null if it could not be extracted
	 */
	public String getIngNameFromString(String s) {
		Pattern p = Pattern.compile("\\d\\s+\\w+\\s+(.+)");
		Matcher m = p.matcher(s);
		if (m.find()) {
			return m.group(1);
		} else {
			return null;
		}
	}
	
	/**
	 * extracts the amount of an ingredient from a string that comes from substitution api call
	 * @param s the string to extract the ingredient amount from
	 * @return a double representing the amount of the ingredient or 0.0 if info could not be extracted
	 */
	public double getAmountIngFromString(String s) {
		if (s.contains("/")) {
			Pattern p = Pattern.compile("([0-9]+)(\\s*/\\s*)([0-9]+)");
			Matcher m = p.matcher(s);
			if (m.find()) {
				double val1 = Double.parseDouble(m.group(1));
				double val2 = Double.parseDouble(m.group(3));
				return val1 / val2;
			} else {
				return 0.0;
			}
		} else {
			Pattern p = Pattern.compile("([0-9]+)");
			Matcher m = p.matcher(s);
			if (m.find()) {
				return Double.parseDouble(m.group(1));
			} else {
				return 0.0;
			}
		}
	}
	
	/**
	 * extracts the ingredient units from a string that comes from substitution api call
	 * @param s the string to extract the ingredient units of measurement from
	 * @return a String representing the name of the ingredient or null if it could not be extracted
	 */
	public String getIngUnitsFromString(String s) {
		Pattern p = Pattern.compile("\\d\\s+(\\w+)\\s*");
		Matcher m = p.matcher(s);
		if (m.find()) {
			return m.group(1);
		} else {
			return null;
		}
	}

	// THIS IS JSUT FOR TESTING PURPOSES.  DELETE EVENTUALLY
	public static void main (String[] args) {
		Ingredient test = new Ingredient("butter");
		test.setUnitOfMeasure("Gram");
		test.setAmount(300);
		NutritionApiCaller nac = new NutritionApiCaller();
//		String sub = "1/2 cup = 1 cup plain yogurt";
//		nac.parseSubstitutionString(test, sub);
		nac.makeNutritionalCall(test);
		nac.getSubstitutions(test);
		System.out.println(test);
	}
}
