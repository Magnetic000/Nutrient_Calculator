package ics4u.ics4u_final_project;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView recipeName;
    public ImageView recipeIcon;
    public TextView ingredientName;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        recipeName = (TextView)itemView.findViewById(R.id.recipe_name);
        recipeIcon = (ImageView)itemView.findViewById(R.id.recipe_icon);
        ingredientName = (TextView)itemView.findViewById(R.id.ingredient_name);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}