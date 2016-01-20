/*
This is the class for any cards that show a recipe
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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<Recipe> itemList;
    private Context context;

    /**
     * Constructor for adapter
     *
     * @param context  the context of the activity
     * @param itemList the array to be passed through the adapter
     */
    public RecipeAdapter(Context context, List<Recipe> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        //Sends data into card containers which are located within the recycler view containers
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_mainactivity, null);
        return new RecyclerViewHolders(layoutView, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        //Populate card items with corresponding information
        holder.recipeName.setText(itemList.get(position).getTitle());
        holder.recipeIcon.setImageResource(itemList.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}