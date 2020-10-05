package nutritionalRecipe;



public class Ingredient {
	private String name;
	private double amount;
	private String unitOfMeasure;
	private int calories;
	private int fat;
	private int protein;
	private int carbs;
	private int sugar;
	private int fiber;
	
	public Ingredient(String name) {
		this.name = name;
	}
	
	public Ingredient(String name, double amount, String unitOfMeasure) {
		this.name = name;
		this.amount = amount;
		this.unitOfMeasure = unitOfMeasure;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public int getFat() {
		return fat;
	}

	public void setFat(int fat) {
		this.fat = fat;
	}

	public int getProtein() {
		return protein;
	}

	public void setProtein(int protein) {
		this.protein = protein;
	}

	public int getCarbs() {
		return carbs;
	}

	public void setCarbs(int carbs) {
		this.carbs = carbs;
	}

	public int getSugar() {
		return sugar;
	}

	public void setSugar(int sugar) {
		this.sugar = sugar;
	}

	public int getFiber() {
		return fiber;
	}

	public void setFiber(int fiber) {
		this.fiber = fiber;
	}

	public String toString(){
		return amount + " " + unitOfMeasure + " " + name + " ";
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Ingredient)) {
			return false;
		} else {
			Ingredient comp = (Ingredient) o;
			if (comp.getName() == this.name) {
				return true;
			} else {
				return false;
			}
		}
	}

}
