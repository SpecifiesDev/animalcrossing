package me.specifies.AnimalCrossing.Exceptions;

/**
 * When registering recipes, they can't exceed 9. Create a logging error for doing so.
 *
 */
public class RecipeExceedsLimit extends Exception {
	
	private static final long serialVersionUID = 506940123864117985L;

	public RecipeExceedsLimit(String string) {
		super(string);
	}

}
