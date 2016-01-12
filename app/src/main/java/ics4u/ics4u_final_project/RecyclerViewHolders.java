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
    public static boolean openMeasure = false;
    public static boolean openRecipe = false;
    private final Context context;
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
        if (IngredientSelectionActivity.onIngredient) {
            Intent intent = new Intent(this.context,MeasureSelectionActivity.class);
            context.startActivity(intent);
        } else {
            openRecipe = true;
            Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}