package nutritionalRecipe;

import static org.junit.Assert.*;

import org.junit.Test;

public class NutritionApiCallerTest {

	@Test
	public void testMakeNutritionalCall() {
		Ingredient test = new Ingredient("butter");
		test.setUnitOfMeasure("Gram");
		test.setAmount(300);
		NutritionApiCaller nac = new NutritionApiCaller();
		assertEquals(nac.makeNutritionalCall(test), 1);
	}
	
	@Test
	public void testUrlEncodeString() {
		String s = "hello this is a test string";
		NutritionApiCaller nac = new NutritionApiCaller();
		String temp = nac.urlEncodeString(s);
		assertEquals(temp, "hello%20this%20is%20a%20test%20string");
	}

}
