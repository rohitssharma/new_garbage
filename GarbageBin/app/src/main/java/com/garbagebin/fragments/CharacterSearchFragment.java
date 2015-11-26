package com.garbagebin.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.garbagebin.adapters.CharactersAdapter;
import com.garbagebin.models.CharactersModel;

import java.util.ArrayList;

import garbagebin.com.garbagebin.CharacterDetailFragment;
import garbagebin.com.garbagebin.R;
import garbagebin.com.garbagebin.Timeline;

/**
 * Created by rohit on 21/11/15.
 */
public class CharacterSearchFragment extends Fragment {

    GridView gridView_characters;
    ArrayList<CharactersModel> al_characters = new ArrayList<>();  //Sent From SearchFragment
    TextView tv_nocharactersfound;
    Context context;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.characters_search_layout,container,false);
        context = getActivity();
        activity = getActivity();

        initializeViews(view);

        //    if(al_characters!=null){
//        al_characters = getArguments().getParcelableArrayList("charcters_al");
        al_characters = SearchFragement1.al_characters;
      //  Toast.makeText(context,"onCreateView",Toast.LENGTH_SHORT).show();

        if(al_characters.size() == 0){
            gridView_characters.setVisibility(View.GONE);
            tv_nocharactersfound.setVisibility(View.VISIBLE);
        }
        else{
            tv_nocharactersfound.setVisibility(View.GONE);
            gridView_characters.setVisibility(View.VISIBLE);

            CharactersAdapter adapter = new CharactersAdapter(context,activity,al_characters);
            gridView_characters.setAdapter(adapter);


            gridView_characters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String character_id = al_characters.get(i).getBlog_id();

                    Fragment fr = new CharacterDetailFragment();
                    FragmentTransaction ft = Timeline.fm.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("Character_id", character_id);
                    // ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    fr.setArguments(bundle);
                    ft.replace(R.id.content_frame, fr);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

        }
        //  }


        return  view;
    }

    public void initializeViews(View view){
        gridView_characters = (GridView)(view.findViewById(R.id.gridView_characters));
        tv_nocharactersfound = (TextView)(view.findViewById(R.id.tv_nocharactersfound));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("resume","resumecahra");
    }
}
