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

    public RecipeAdapter(Context context, List<Recipe> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_mainactivity, null);
        return new RecyclerViewHolders(layoutView, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.recipeName.setText(itemList.get(position).getTitle());
        System.out.println(itemList.get(position).getPhoto() + "photo");
        holder.recipeIcon.setImageResource(itemList.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}