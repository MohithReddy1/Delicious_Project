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

public class burger_fragment extends Fragment {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewItemList;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root = (View) inflater.inflate(R.layout.burger, container, false);
        recyclerViewItem(root);
        return root;
    }

    private void recyclerViewItem(View root) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerViewItemList = root.findViewById(R.id.recyclerview2);
        recyclerViewItemList.setLayoutManager(gridLayoutManager);

        ArrayList<FoodDomain> foodlist = new ArrayList<>();
        foodlist.add(new FoodDomain("Chicken Burger", "burger1", "slices pepperoni ,mozzarella cheese, fresh oregano,  ground black pepper, pizza sauce", 200));
        foodlist.add(new FoodDomain("BK Classic", "burger2", "slices pepperoni ,mozzarella cheese, fresh oregano,  ground black pepper, pizza sauce", 165));
        foodlist.add(new FoodDomain("Aloo Tikki Burger", "burger6", " olive oil, Vegetable oil, pitted Kalamata, cherry tomatoes, fresh oregano, basil", 250));
        foodlist.add(new FoodDomain("Cheese Meltdown", "burger4", "beef, Gouda Cheese, Special sauce, Lettuce, tomato ", 260));
        foodlist.add(new FoodDomain("Veg Burger", "burger5", " olive oil, Vegetable oil, pitted Kalamata, cherry tomatoes, fresh oregano, basil", 155));
        foodlist.add(new FoodDomain("Classic Zinger", "burger3", "beef, Gouda Cheese, Special sauce, Lettuce, tomato ", 210));

        adapter = new FoodAdapter(foodlist);
        recyclerViewItemList.setAdapter(adapter);

    }
}
