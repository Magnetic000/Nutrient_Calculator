package ics4u.ics4u_final_project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView recipeName;
    public ImageView recipeIcon;
    public TextView ingredientName;
    private final Context context;
    public static int location = 0;
    static boolean edit;

    public RecyclerViewHolders(View itemView, Context context) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.context = context;
        recipeName = (TextView) itemView.findViewById(R.id.recipe_name);
        recipeIcon = (ImageView) itemView.findViewById(R.id.recipe_icon);
        ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name);
    }

    @Override
    public void onClick(View view) {
        location = this.getLayoutPosition();
        edit = false;
        if (IngredientSelectionActivity.onIngredient && IngredientSelectionActivity.searchCompleted) {
            Intent intent = new Intent(this.context, MeasureSelectionActivity.class);
            context.startActivity(intent);
            IngredientSelectionActivity.fa.finish();
            IngredientSelectionActivity.onIngredient=false;
        } else if (RecipeCreateActivity.onRecipe) {
            System.out.println("added " + RecipeCreateActivity.addedIngred);
            if (RecipeCreateActivity.recipe.getIngredients().size() > 0 && RecipeCreateActivity.addedIngred) {
                RecipeCreateActivity.onRecipe = false;
                Intent intent = new Intent(this.context, MeasureSelectionActivity.class);
                context.startActivity(intent);
            } else {
                Toast t = Toast.makeText(context, "Please create a new ingredient", Toast.LENGTH_LONG);
                t.show();
            }
        } else {
            edit = true;
            Intent intent = new Intent(this.context, RecipeCreateActivity.class);
            context.startActivity(intent);
            //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}