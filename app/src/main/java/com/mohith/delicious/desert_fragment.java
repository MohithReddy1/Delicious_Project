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

public class desert_fragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewItemList;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root = (View) inflater.inflate(R.layout.desert, container, false);
        recyclerViewItem(root);
        return root;
    }

    private void recyclerViewItem(View root) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerViewItemList = root.findViewById(R.id.recyclerview3);
        recyclerViewItemList.setLayoutManager(gridLayoutManager);

        ArrayList<FoodDomain> foodlist = new ArrayList<>();
        foodlist.add(new FoodDomain("Chocolate cake", "chocolate_cake", "slices pepperoni ,mozzarella cheese, fresh oregano,  ground black pepper, pizza sauce", 100));
        foodlist.add(new FoodDomain("Custard Fruit Salad", "custard", "slices pepperoni ,mozzarella cheese, fresh oregano,  ground black pepper, pizza sauce", 200));
        foodlist.add(new FoodDomain("Choco Ice cream", "chocolateicecream", "beef, Gouda Cheese, Special sauce, Lettuce, tomato ", 150));
        foodlist.add(new FoodDomain("Strawberry Cake", "strawberry", "beef, Gouda Cheese, Special sauce, Lettuce, tomato ", 100));
        foodlist.add(new FoodDomain("Cup Cake", "cupcake", " olive oil, Vegetable oil, pitted Kalamata, cherry tomatoes, fresh oregano, basil", 75));
        foodlist.add(new FoodDomain("Vanilla Ice Cream", "vanilla", " olive oil, Vegetable oil, pitted Kalamata, cherry tomatoes, fresh oregano, basil", 110));

        adapter = new FoodAdapter(foodlist);
        recyclerViewItemList.setAdapter(adapter);

    }
}
