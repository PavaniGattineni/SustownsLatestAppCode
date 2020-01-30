package com.sustowns.sustownsapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Adapters.CategoryExpandListAdapter;
import com.sustowns.sustownsapp.Adapters.ThreeLevelListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CategoryExpandListActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    String[] parent = new String[]{"LIVE STOCK"}; // comment this when uncomment bottom
    String[] movies = new String[]{"POULTRY"};
   // String[] games = new String[]{"Fps", "Moba", "Rpg", "Racing"};
        String[] horror = new String[]{"Egg"};
    String[] action = new String[]{"Jon Wick", "Die Hard", "Fast 7", "Avengers"};
    String[] thriller = new String[]{"Imitation Game", "Tinker, Tailer, Soldier, Spy", "Inception", "Manchester by the Sea"};
    String[] fps = new String[]{"CS: GO", "Team Fortress 2", "Overwatch", "Battlefield 1", "Halo II", "Warframe"};
    String[] moba = new String[]{"Dota 2", "League of Legends", "Smite", "Strife", "Heroes of the Storm"};
    String[] rpg = new String[]{"Witcher III", "Skyrim", "Warcraft", "Mass Effect II", "Diablo", "Dark Souls", "Last of Us"};
    String[] racing = new String[]{"NFS: Most Wanted", "Forza Motorsport 3", "EA: F1 2016", "Project Cars"};
    LinkedHashMap<String, String[]> thirdLevelMovies = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> thirdLevelGames = new LinkedHashMap<>();
    List<String[]> secondLevel = new ArrayList<>();
    List<LinkedHashMap<String, String[]>> data = new ArrayList<>();

    CategoryExpandListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ImageView backarrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category_expand_list);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // second level category names (genres)
        secondLevel.add(movies);
       // secondLevel.add(games);
        // secondLevel.add(serials);

        // movies category all data
        thirdLevelMovies.put(movies[0], horror);
       // thirdLevelMovies.put(movies[1], action);
        //thirdLevelMovies.put(movies[2], thriller);


        // games category all data
      /*  thirdLevelGames.put(games[0], fps);
        thirdLevelGames.put(games[1], moba);
        thirdLevelGames.put(games[2], rpg);
        thirdLevelGames.put(games[3], racing);*/


        // serials category all data
      /*  thirdLevelSerials.put(serials[0], crime);
        thirdLevelSerials.put(serials[1], family);
        thirdLevelSerials.put(serials[2], comedy);
*/


        // all data
        data.add(thirdLevelMovies);
        data.add(thirdLevelGames);
        //data.add(thirdLevelSerials);


        // expandable listview
        expandableListView = (ExpandableListView) findViewById(R.id.expandible_listview);

        // parent adapter
        ThreeLevelListAdapter threeLevelListAdapterAdapter = new ThreeLevelListAdapter(this, parent, secondLevel, data);


        // set adapter
        expandableListView.setAdapter( threeLevelListAdapterAdapter );


        // OPTIONAL : Show one list at a time
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });


    }
}

