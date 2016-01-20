/*
this is that class that holds all the cards
 */
/*
Copyright (C) 2016 Isaac Wismer & Andrew Xu

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ics4u.ics4u_final_project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static int location = 0;
    static boolean edit;
    private final Context context;
    public TextView recipeName;
    public ImageView recipeIcon;
    public TextView ingredientName;

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
        //if the card is on the search screen
        if (IngredientSelectionActivity.onIngredient) {
            if (IngredientSelectionActivity.searchCompleted) {
                Intent intent = new Intent(this.context, MeasureSelectionActivity.class);
                context.startActivity(intent);
                IngredientSelectionActivity.fa.finish();
                IngredientSelectionActivity.onIngredient = false;
                //if the card is an ingredient on a recipe
            } else {
                Toast t = Toast.makeText(context, "Please search for an ingredient", Toast.LENGTH_LONG);
                t.show();
            }
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
            //if the card clicked is a recipe to be edited
        } else {
            edit = true;
            Intent intent = new Intent(this.context, RecipeCreateActivity.class);
            context.startActivity(intent);
        }
    }
}