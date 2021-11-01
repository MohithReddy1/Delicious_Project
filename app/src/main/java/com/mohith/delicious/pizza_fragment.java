package com.mohith.delicious;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohith.delicious.Adapter.FoodAdapter;
import com.mohith.delicious.Domain.FoodDomain;

import java.util.ArrayList;

public class pizza_fragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewItemList;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root = (View) inflater.inflate(R.layout.pizza, container, false);
        recyclerViewItem(root);
        return root;
    }

    private void recyclerViewItem(View root) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerViewItemList = root.findViewById(R.id.recyclerview1);
        recyclerViewItemList.setLayoutManager(gridLayoutManager);

        ArrayList<FoodDomain> foodlist = new ArrayList<>();
        foodlist.add(new FoodDomain("Pepperoni Pizza", "pizza1", "slices pepperoni ,mozzarella cheese, fresh oregano,  ground black pepper, pizza sauce", 210));
        foodlist.add(new FoodDomain("Farm House", "pizza2", "slices pepperoni ,mozzarella cheese, fresh oregano,  ground black pepper, pizza sauce", 165));
        foodlist.add(new FoodDomain("Chicken Supreme", "pizza3", "beef, Gouda Cheese, Special sauce, Lettuce, tomato ", 300));
        foodlist.add(new FoodDomain("Margarita", "pizza4", "beef, Gouda Cheese, Special sauce, Lettuce, tomato ", 260));
        foodlist.add(new FoodDomain("Vegetarian", "pizza5", " olive oil, Vegetable oil, pitted Kalamata, cherry tomatoes, fresh oregano, basil", 155));
        foodlist.add(new FoodDomain("Mexican Green", "pizza6", " olive oil, Vegetable oil, pitted Kalamata, cherry tomatoes, fresh oregano, basil", 155));

        adapter = new FoodAdapter(foodlist);
        recyclerViewItemList.setAdapter(adapter);

    }
}
