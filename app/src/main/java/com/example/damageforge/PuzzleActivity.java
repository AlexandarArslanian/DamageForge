package com.example.damageforge;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class PuzzleActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final static String SHARED_PREFERENCES_PREFIX = "MainActivitySharedPreferencesPrefix";

    Spinner selectItem1;
    Spinner selectItem2;
    Spinner selectItem3;
    Spinner selectItem4;
    Spinner selectItem5;
    Spinner selectItem6;
    Spinner selectItem1_2;
    Spinner selectItem2_2;
    Spinner selectItem3_2;
    Spinner selectItem4_2;
    Spinner selectItem5_2;
    Spinner selectItem6_2;

    Button buttonBuild;
    Button buttonGenerate;
    Button buttonCompareBuild;
    Button buttonPuzzle;


    TextView labelHealth;
    TextView textViewHP;
    SeekBar seekBarHP;

    TextView labelArmour;
    TextView textViewArmour;
    SeekBar seekBarArmour;

    TextView labelMR;
    TextView textViewMR;

    TextView viewStats;
    TextView viewStats2;

    TextView viewHiddenDPSText;
    TextView viewHiddenDPSText2;
    LinearLayout viewHiddenDPSLayout;

    SeekBar seekBarMR;

    LinkedList<Item> itemListFull;
    Item blankItem = new Item(); //for when nothing was selected when choosing an item
    Item gatheredItem = new Item(); //for grouping all the gathered item stats

    int [] itemPositions = new int [6]; // to record every item position when selecting an item from the spinner dropdown
    int [] itemPositions2 = new int [6]; // to record every item position when selecting an item from the spinner dropdown

    private String version;
    private static String ITEMS_URL;
    static final int baseAD = 100;
    static final double baseAttackSpeed = 1.1;
    static final int baseAPFlatDPS = 180;
    static final double baseAPPercentDPS = 0.55;
    static final double baseCritDamage = 1.75;

    double totalDPS, totalAD , totalAttackSpeed, totalArmourPen, totalLethality, totalCritChance, totalAP, totalPercentMagicPen, totalFlatMagicPen , armourDamageReduction, magicResistDamageReduction= 0;
    double totalDPS2, totalAD2 , totalAttackSpeed2, totalArmourPen2, totalLethality2, totalCritChance2, totalAP2, totalPercentMagicPen2, totalFlatMagicPen2 , armourDamageReduction2, magicResistDamageReduction2= 0;
    int enemyArmour = 100;
    int enemyMagicResist = 50;
    int enemyHP = 2000;
    Button buttonPickA;

    Button buttonPickB;

    LinkedList<String> buildsList;
    ArrayAdapter<String> adapterSavedBuilds;
    private void setItemUrl(String version) {
        ITEMS_URL = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/en_US/item.json";
    }
    private static final String VERSIONS_URL = "https://ddragon.leagueoflegends.com/api/versions.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        /*View root = findViewById(android.R.id.content);

        root.post(() -> {
        YoYo.with(Techniques.SlideInLeft)
                .duration(500)
                .playOn(root);
        });
*/
        initComponents();

        GestureDetector gestureDetector = new GestureDetector(this,
                new SwipeGestureListener(this, null, MainActivity.class )); // this, left screen, right screen

        viewStats.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        viewStats2.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
        viewHiddenDPSLayout.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }
    private void initComponents() {
        viewHiddenDPSLayout = findViewById(R.id.viewHiddenDPSLayout);
        selectItem1 = findViewById(R.id.selectItem1);
        selectItem2 = findViewById(R.id.selectItem2);
        selectItem3 = findViewById(R.id.selectItem3);
        selectItem4 = findViewById(R.id.selectItem4);
        selectItem5 = findViewById(R.id.selectItem5);
        selectItem6 = findViewById(R.id.selectItem6);

        selectItem1_2 = findViewById(R.id.selectItem1_2);
        selectItem2_2 = findViewById(R.id.selectItem2_2);
        selectItem3_2 = findViewById(R.id.selectItem3_2);
        selectItem4_2 = findViewById(R.id.selectItem4_2);
        selectItem5_2 = findViewById(R.id.selectItem5_2);
        selectItem6_2 = findViewById(R.id.selectItem6_2);

        buttonBuild = findViewById(R.id.buttonBuild);
        buttonGenerate = findViewById(R.id.buttonGenerate);
        buttonCompareBuild = findViewById(R.id.buttonCompare);
        buttonPuzzle = findViewById(R.id.buttonPuzzle);
        buttonPickA = findViewById(R.id.buttonPickA);
        buttonPickB = findViewById(R.id.buttonPickB);

        labelHealth = findViewById(R.id.labelHealth);
        textViewHP = findViewById(R.id.textViewHP);
        seekBarHP = findViewById(R.id.seekBarHP);

        viewStats = findViewById(R.id.viewStats);
        viewStats2 = findViewById(R.id.viewStats2);

        viewHiddenDPSText = findViewById(R.id.viewHiddenDPSText);
        viewHiddenDPSText2 = findViewById(R.id.viewHiddenDPSText2);

        runOnUiThread(() -> {
            ((TextView)findViewById(R.id.labelHealth)).setText("Health");});

        labelArmour = findViewById(R.id.labelArmour);
        textViewArmour = findViewById(R.id.textViewArmour);
        seekBarArmour = findViewById(R.id.seekBarArmour);
        runOnUiThread(() -> {
            ((TextView)findViewById(R.id.labelArmour)).setText("Armour");});

        labelMR = findViewById(R.id.labelMR);
        textViewMR = findViewById(R.id.textViewMR);
        seekBarMR = findViewById(R.id.seekBarMR);
        runOnUiThread(() -> {
            ((TextView)findViewById(R.id.labelMR)).setText("Magic Resist");});

        itemListFull = new LinkedList<Item>();
        itemListFull.add(blankItem);

        //Default spinner view before everything loads, in case loading takes longer
       /* List<String> loadingList = new ArrayList<>();
        loadingList.add("No Item");

        ArrayAdapter<String> loadingAdapter = new ArrayAdapter<>(this, R.layout.colour_spinner_layout, loadingList);
        loadingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectItem1.setAdapter(loadingAdapter);
        selectItem2.setAdapter(loadingAdapter);
        selectItem3.setAdapter(loadingAdapter);
        selectItem4.setAdapter(loadingAdapter);
        selectItem5.setAdapter(loadingAdapter);
        selectItem6.setAdapter(loadingAdapter);

        selectItem1_2.setAdapter(loadingAdapter);
        selectItem2_2.setAdapter(loadingAdapter);
        selectItem3_2.setAdapter(loadingAdapter);
        selectItem4_2.setAdapter(loadingAdapter);
        selectItem5_2.setAdapter(loadingAdapter);
        selectItem6_2.setAdapter(loadingAdapter);*/

        //Get all item info from the Item URL, utilizing the URL of the Versions Api to get the latest version
        Api.getJSON(VERSIONS_URL, new ReadDataHandler(){
            @Override
            public void handleMessage(Message msg) {
                String response = getJson();

                try {

                    JSONArray array = new JSONArray(response);

                    version = array.getString(0);

                    setItemUrl(version);

                    Api.getJSON(ITEMS_URL, new ReadDataHandler(){
                        // @SuppressLint("HandlerLeak")
                        @Override
                        public void handleMessage(Message msg) {
                            String response = getJson();

                            try {
                                JSONObject array = new JSONObject(response);
                                LinkedList<Item>items = Item.parseJSONObjectList(array);

                                ArrayList<String> itemNamesList = new ArrayList<>();//for using on the drop down selector, gathering all the names
                                itemNamesList.add("No Item"); //To put placeholder text

                                for(int i = 0; i < items.size(); i++) {
                                    itemNamesList.add(items.get(i).getName());
                                    itemListFull.add(items.get(i));
                                }
                                runOnUiThread(()->{//updating the ui on the main thread
                                    ArrayAdapter<String> adapterItem = new ArrayAdapter<String>(PuzzleActivity.this, R.layout.colour_spinner_layout, itemNamesList); //this, layout, List
                                    adapterItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    selectItem1.setAdapter(adapterItem);
                                    selectItem2.setAdapter(adapterItem);
                                    selectItem3.setAdapter(adapterItem);
                                    selectItem4.setAdapter(adapterItem);
                                    selectItem5.setAdapter(adapterItem);
                                    selectItem6.setAdapter(adapterItem);
                                    selectItem1_2.setAdapter(adapterItem);
                                    selectItem2_2.setAdapter(adapterItem);
                                    selectItem3_2.setAdapter(adapterItem);
                                    selectItem4_2.setAdapter(adapterItem);
                                    selectItem5_2.setAdapter(adapterItem);
                                    selectItem6_2.setAdapter(adapterItem);
                                });

                            } catch(Exception e) {
                                Log.e("readItemError", "Exception", e);
                            }

                        }
                    });

                } catch(Exception e) {
                    Log.e("readVersionError", String.valueOf(e));
                }

            }
        });
        buildsList = new LinkedList<String>();
        buildsList.add("Saved Builds"); //To put placeholder text

        buttonPickA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalDPS > totalDPS2) {
                    buttonPickA.setText("✅");
                    buttonPickA.setBackgroundColor(Color.parseColor("#4CAF50")); // green
                } else {
                    buttonPickA.setText("❌");
                    buttonPickA.setBackgroundColor(Color.parseColor("#F44336")); // red
                }
                // disable buttons to prevent re-picking
                buttonPickA.setEnabled(false);

                viewHiddenDPSText.setVisibility(View.VISIBLE);
                viewHiddenDPSText2.setVisibility(View.VISIBLE);
            }
        });

        buttonPickB.setOnClickListener(v -> {//lambda expression doing the same as above (the "v->" replaces new View.OnClickListener() and @Override public void onClick(View view) {
            if (totalDPS < totalDPS2) {
                buttonPickB.setText("✅");
                buttonPickB.setBackgroundColor(Color.parseColor("#4CAF50")); // green
            } else {
                buttonPickB.setText("❌");
                buttonPickB.setBackgroundColor(Color.parseColor("#F44336")); // red
            }
            buttonPickB.setEnabled(false);

            viewHiddenDPSText.setVisibility(View.VISIBLE);
            viewHiddenDPSText2.setVisibility(View.VISIBLE);
        });


        //set listeners for which item has been selected, to find the position of the spinner
        selectItem1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {

                    itemPositions[0] = position;
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(blankItem, itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                        itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            }
        });
        selectItem2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {

                    itemPositions[1] = position;
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(itemListFull.get(itemPositions[0]), blankItem, itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                        itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            }
        });
        selectItem3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {
                    itemPositions[2] = position;
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                        itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            }
        });
        selectItem4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {

                    itemPositions[3] = position;
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));

                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), blankItem,
                        itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            }
        });
        selectItem5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {

                    itemPositions[4] = position;
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                        blankItem, itemListFull.get(itemPositions[5]));
            }
        });
        selectItem6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {

                    itemPositions[5] = position;
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                        itemListFull.get(itemPositions[4]), blankItem);
            }
        });

        //set listeners for which item has been selected, to find the position of the spinner
        selectItem1_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {
                    Log.d("itemselectedpos", String.valueOf(position));
                    Log.d("itemposmatcher", itemListFull.get(position).getName());
                    itemPositions2[0] = position;
                    calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                            itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps2(blankItem, itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                        itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
            }
        });
        selectItem2_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {

                    itemPositions2[1] = position;
                    calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                            itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps2(itemListFull.get(itemPositions2[0]), blankItem, itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                        itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
            }
        });
        selectItem3_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {
                    itemPositions2[2] = position;
                    calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                            itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                        itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
            }
        });
        selectItem4_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {
                    itemPositions2[3] = position;
                    calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                            itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));

                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), blankItem,
                        itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
            }
        });
        selectItem5_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {

                    itemPositions2[4] = position;
                    calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                            itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                        blankItem, itemListFull.get(itemPositions2[5]));
            }
        });
        selectItem6_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {

                    itemPositions2[5] = position;
                    calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                            itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                        itemListFull.get(itemPositions2[4]), blankItem);
            }
        });


        buttonGenerate.setOnClickListener(this);
        buttonBuild.setOnClickListener(this);
        buttonCompareBuild.setOnClickListener(this);
        buttonPuzzle.setOnClickListener(this);

        seekBarHP.setOnSeekBarChangeListener(this);//setting initial value to print
        ((TextView)findViewById(R.id.textViewHP)).setText(String.valueOf(seekBarHP.getProgress()*100));
        enemyHP = seekBarHP.getProgress()*100;

        seekBarArmour.setOnSeekBarChangeListener(this);//setting initial value to print
        ((TextView)findViewById(R.id.textViewArmour)).setText(String.valueOf(seekBarArmour.getProgress()*10));
        enemyArmour = seekBarArmour.getProgress()*10;

        seekBarMR.setOnSeekBarChangeListener(this);//setting initial value to print
        ((TextView)findViewById(R.id.textViewMR)).setText(String.valueOf(seekBarMR.getProgress()*10));
        enemyMagicResist = seekBarMR.getProgress()*10;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {//To print the progress of the Seekbar

        if(seekBar == seekBarHP){ //check which seekBar is it
            ((TextView)findViewById(R.id.textViewHP)).setText(String.valueOf(i)+"00");
            enemyHP = i * 100;
            calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                    itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                    itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
        }
        if(seekBar == seekBarArmour){ //check which seekBar is it
            ((TextView)findViewById(R.id.textViewArmour)).setText(String.valueOf(i)+"0");
            enemyArmour = i * 10;

            calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                    itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                    itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
        }
        if(seekBar == seekBarMR){ //check which seekBar is it
            ((TextView)findViewById(R.id.textViewMR)).setText(String.valueOf(i)+"0");
            enemyMagicResist = i * 10;

            calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                    itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            calculateDps2(itemListFull.get(itemPositions2[0]), itemListFull.get(itemPositions2[1]), itemListFull.get(itemPositions2[2]), itemListFull.get(itemPositions2[3]),
                    itemListFull.get(itemPositions2[4]), itemListFull.get(itemPositions2[5]));
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBarHP) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBarHP) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData("");
        loadData2("");
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData("");
    }
    public void createResultsMessage(){
        String messageStats;
        String hiddenMessageStats; //for DPS parts

        hiddenMessageStats = "DPS: " + (int)totalDPS + "\n";
        hiddenMessageStats += "Time to kill Enemy: " + String.format("%.2f", (enemyHP / totalDPS)) + 's' + "\n";

        messageStats = "\n" + "Attack Damage: " + (int)totalAD + "\n";// + "                                          " + "Crit Chance: " + (int)(totalCritChance*100) + "%" + "\n";
        messageStats += "Attack Speed: " + String.format("%.2f", totalAttackSpeed) + "\n";// + "                                " + "Ability Power: " + (int)totalAP + "\n";
        messageStats += "Crit Chance: " + (int)(totalCritChance*100) + "%" + "\n";
        messageStats += "Armor Pen: " + (int)(totalArmourPen*100) + "%" + "\n";// + "                       " + "Magic Pen: " + (int)(totalPercentMagicPen*100) + "%" + "\n";
        messageStats += "Lethality: " + (int)totalLethality + "\n" + "\n";// + "                          " + "Flat Magic Pen: " + (int)totalFlatMagicPen + "\n";
        messageStats += "Enemy Physical Damage Reduction: " + (int)((1-armourDamageReduction)*100) + "%" + "\n\n";

        messageStats += "Ability Power: " + (int)totalAP + "\n";
        messageStats += "Magic Pen: " + (int)(totalPercentMagicPen*100) + "%" + "\n";
        messageStats += "Flat Magic Pen: " + (int)totalFlatMagicPen + "\n" + "\n";
        messageStats += "Enemy Magical Damage Reduction: " + (int)((1-magicResistDamageReduction)*100) + "%";

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        String messageStats2;
        String hiddenMessageStats2; //for DPS parts

        hiddenMessageStats2 =  "DPS: " + (int)totalDPS2 + "\n";
        hiddenMessageStats2 += "Time to kill Enemy: " + String.format("%.2f", (enemyHP / totalDPS2)) + 's' + "\n";

        messageStats2 = "\n" +"Attack Damage: " + (int)totalAD2 + "\n";// + "                                          " + "Crit Chance: " + (int)(totalCritChance*100) + "%" + "\n";
        messageStats2 += "Attack Speed: " + String.format("%.2f", totalAttackSpeed2) + "\n";// + "                                " + "Ability Power: " + (int)totalAP + "\n";
        messageStats2 += "Crit Chance: " + (int)(totalCritChance2*100) + "%" + "\n";
        messageStats2 += "Armor Pen: " + (int)(totalArmourPen2*100) + "%" + "\n";// + "                       " + "Magic Pen: " + (int)(totalPercentMagicPen*100) + "%" + "\n";
        messageStats2 += "Lethality: " + (int)totalLethality2 + "\n" + "\n";// + "                          " + "Flat Magic Pen: " + (int)totalFlatMagicPen + "\n";
        messageStats2 += "Enemy Physical Damage Reduction: " + (int)((1-armourDamageReduction2)*100) + "%" + "\n\n";

        messageStats2 += "Ability Power: " + (int)totalAP2 + "\n";
        messageStats2 += "Magic Pen: " + (int)(totalPercentMagicPen2*100) + "%" + "\n";
        messageStats2 += "Flat Magic Pen: " + (int)totalFlatMagicPen2 + "\n" + "\n";
        messageStats2 += "Enemy Magical Damage Reduction: " + (int)((1-magicResistDamageReduction2)*100) + "%";

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        SpannableString spannableMsg1 = new SpannableString(hiddenMessageStats);
        SpannableString spannableMsg2 = new SpannableString(hiddenMessageStats2);

        String dpsString = String.valueOf((int)totalDPS); //to colour the DPS number
        int startDps = hiddenMessageStats.indexOf(dpsString); //get starting index
        int endDps = startDps + dpsString.length(); //get ending index

        String dpsString2 = String.valueOf((int)totalDPS2); //to colour the DPS number
        int startDps2 = hiddenMessageStats2.indexOf(dpsString2); //get starting index
        int endDps2 = startDps2 + dpsString2.length(); //get ending index

        String ttkString = String.format("%.2f", (enemyHP / totalDPS)); //to colour the time to kill number
        int startTtk = hiddenMessageStats.indexOf(ttkString);
        int endTtk = startTtk + ttkString.length();

        String ttkString2 = String.format("%.2f", (enemyHP / totalDPS2)); //to colour the time to kill number
        int startTtk2 = hiddenMessageStats2.indexOf(ttkString2);
        int endTtk2 = startTtk2 + ttkString2.length();

        if(totalDPS > totalDPS2){
            spannableMsg1.setSpan(new ForegroundColorSpan(Color.GREEN), startDps, endDps, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableMsg1.setSpan(new ForegroundColorSpan(Color.GREEN), startTtk, endTtk, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableMsg2.setSpan(new ForegroundColorSpan(Color.RED), startDps2, endDps2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableMsg2.setSpan(new ForegroundColorSpan(Color.RED), startTtk2, endTtk2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (totalDPS < totalDPS2){
            spannableMsg1.setSpan(new ForegroundColorSpan(Color.RED), startDps, endDps, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableMsg1.setSpan(new ForegroundColorSpan(Color.RED), startTtk, endTtk, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableMsg2.setSpan(new ForegroundColorSpan(Color.GREEN), startDps2, endDps2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableMsg2.setSpan(new ForegroundColorSpan(Color.GREEN), startTtk2, endTtk2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        String finalMessageStats = messageStats;
        String finalMessageStats2 = messageStats2;

        runOnUiThread(() -> {
            viewStats.setText(finalMessageStats);
            viewStats2.setText(finalMessageStats2);

            viewHiddenDPSText.setText(spannableMsg1);
            viewHiddenDPSText2.setText(spannableMsg2);
        });
    }

    public void calculateDps(Item itemOne, Item itemTwo, Item itemThree, Item itemFour, Item itemFive, Item itemSix){
        Item combinedItems = gatherSelectedItemInfo(itemOne, itemTwo, itemThree, itemFour, itemFive, itemSix);
        totalDPS = totalAD = totalAttackSpeed = totalArmourPen = totalLethality = totalCritChance = totalAP = totalPercentMagicPen = totalFlatMagicPen = 0; //globals for printing

        //make base character
        double totalMagicDPS = 0; double totalPhysicalDPS = 0; double totalCritDamage = 0; double armourCalc = 0; double magicResistCalc = 0;

        totalArmourPen = combinedItems.getPercentArmourPen();
        totalLethality = combinedItems.getLethality();
        totalCritChance = combinedItems.getCritChance();
        totalPercentMagicPen = combinedItems.getPercentMagicPen();
        totalFlatMagicPen = combinedItems.getFlatMagicPen();

        totalAD = baseAD + combinedItems.getAttackDamage();
        totalAttackSpeed = baseAttackSpeed * (1+combinedItems.getAttackSpeed());
        totalAP = combinedItems.getAbilityPower()*(1+combinedItems.getMultiplyTotalAp());
        totalCritDamage = baseCritDamage + combinedItems.getMultiplyCritDamage();

        armourCalc = (enemyArmour*(1-combinedItems.getPercentArmourPen())) - combinedItems.getLethality();
        magicResistCalc = (enemyMagicResist*(1-combinedItems.getPercentMagicPen())) - combinedItems.getFlatMagicPen();

        if(armourCalc >= 0){
            armourDamageReduction = 1-(armourCalc/(100+armourCalc));//calculate damage reduction, 1- the result so that the value can just be multiplied for calculating
        }else armourDamageReduction = 100/(100+armourCalc);//calculate damage amplification if values are negative
        if(magicResistCalc >= 0){
            magicResistDamageReduction = 1-(magicResistCalc/(100+magicResistCalc));//calculate damage reduction
        }else magicResistDamageReduction = 100/(100+magicResistCalc);//calculate damage amplification if values are negative

        totalPhysicalDPS = totalAttackSpeed * ((totalAD+(totalAD*totalCritChance*totalCritDamage))*armourDamageReduction + ((1+combinedItems.getMultiplyOnHit()) *
                (combinedItems.getOnHitAD()*armourDamageReduction + combinedItems.getOnHitAP()*magicResistDamageReduction + (combinedItems.getPercentOnHitAP()*totalAP)*magicResistDamageReduction + (combinedItems.getMaxHPOnHitAD()*enemyHP)*armourDamageReduction))) ;
        totalPhysicalDPS *= (1+combinedItems.getMultiplyTotal());

        totalMagicDPS = ((combinedItems.getAPDps() + baseAPFlatDPS) + ((combinedItems.getPercentAPDps()+baseAPPercentDPS)*totalAP) + combinedItems.getMaxHPDpsAP()*enemyHP)*magicResistDamageReduction + (combinedItems.getADDps()*armourDamageReduction) ;
        totalMagicDPS *= (1+combinedItems.getMultiplyTotal());
        totalDPS = totalMagicDPS + totalPhysicalDPS;

        createResultsMessage();
    }

    public void calculateDps2(Item itemOne, Item itemTwo, Item itemThree, Item itemFour, Item itemFive, Item itemSix){
        Item combinedItems = gatherSelectedItemInfo(itemOne, itemTwo, itemThree, itemFour, itemFive, itemSix);
        totalDPS2 = totalAD2 = totalAttackSpeed2 = totalArmourPen2 = totalLethality2 = totalCritChance2 = totalAP2 = totalPercentMagicPen2 = totalFlatMagicPen2 = 0; //globals for printing

        //make base character
        double totalMagicDPS = 0; double totalPhysicalDPS = 0; double totalCritDamage = 0; double armourCalc = 0; double magicResistCalc = 0;

        totalArmourPen2 = combinedItems.getPercentArmourPen();
        totalLethality2 = combinedItems.getLethality();
        totalCritChance2 = combinedItems.getCritChance();
        totalPercentMagicPen2 = combinedItems.getPercentMagicPen();
        totalFlatMagicPen2 = combinedItems.getFlatMagicPen();

        totalAD2 = baseAD + combinedItems.getAttackDamage();
        totalAttackSpeed2 = baseAttackSpeed * (1+combinedItems.getAttackSpeed());
        totalAP2 = combinedItems.getAbilityPower()*(1+combinedItems.getMultiplyTotalAp());
        totalCritDamage = baseCritDamage + combinedItems.getMultiplyCritDamage();

        armourCalc = (enemyArmour*(1-combinedItems.getPercentArmourPen())) - combinedItems.getLethality();
        magicResistCalc = (enemyMagicResist*(1-combinedItems.getPercentMagicPen())) - combinedItems.getFlatMagicPen();

        if(armourCalc >= 0){
            armourDamageReduction2 = 1-(armourCalc/(100+armourCalc));//calculate damage reduction, 1- the result so that the value can just be multiplied for calculating
        }else armourDamageReduction2 = 100/(100+armourCalc);//calculate damage amplification if values are negative
        if(magicResistCalc >= 0){
            magicResistDamageReduction2 = 1-(magicResistCalc/(100+magicResistCalc));//calculate damage reduction
        }else magicResistDamageReduction2 = 100/(100+magicResistCalc);//calculate damage amplification if values are negative

        totalPhysicalDPS = totalAttackSpeed2 * ((totalAD2+(totalAD2*totalCritChance2*totalCritDamage))*armourDamageReduction2 + ((1+combinedItems.getMultiplyOnHit()) *
                (combinedItems.getOnHitAD()*armourDamageReduction2 + combinedItems.getOnHitAP()*magicResistDamageReduction2 + (combinedItems.getPercentOnHitAP()*totalAP2)*magicResistDamageReduction2 + (combinedItems.getMaxHPOnHitAD()*enemyHP)*armourDamageReduction2))) ;
        totalPhysicalDPS *= (1+combinedItems.getMultiplyTotal());

        totalMagicDPS = ((combinedItems.getAPDps() + baseAPFlatDPS) + ((combinedItems.getPercentAPDps()+baseAPPercentDPS)*totalAP2) + combinedItems.getMaxHPDpsAP()*enemyHP)*magicResistDamageReduction2 + (combinedItems.getADDps()*armourDamageReduction2) ;
        totalMagicDPS *= (1+combinedItems.getMultiplyTotal());
        totalDPS2 = totalMagicDPS + totalPhysicalDPS;

        createResultsMessage();
    }
    public Item gatherSelectedItemInfo(Item itemOne, Item itemTwo, Item itemThree, Item itemFour, Item itemFive, Item itemSix){

        gatheredItem.setAttackDamage(itemOne.getAttackDamage()+itemTwo.getAttackDamage()+itemThree.getAttackDamage()+
                itemFour.getAttackDamage()+itemFive.getAttackDamage()+itemSix.getAttackDamage());
        gatheredItem.setCritChance(itemOne.getCritChance()+itemTwo.getCritChance()+itemThree.getCritChance()+
                itemFour.getCritChance()+itemFive.getCritChance()+itemSix.getCritChance());
        gatheredItem.setAttackSpeed(itemOne.getAttackSpeed()+itemTwo.getAttackSpeed()+itemThree.getAttackSpeed()+
                itemFour.getAttackSpeed()+itemFive.getAttackSpeed()+itemSix.getAttackSpeed());
        gatheredItem.setPercentArmourPen(itemOne.getPercentArmourPen()+itemTwo.getPercentArmourPen()+itemThree.getPercentArmourPen()+
                itemFour.getPercentArmourPen()+itemFive.getPercentArmourPen()+itemSix.getPercentArmourPen());
        gatheredItem.setLethality(itemOne.getLethality()+itemTwo.getLethality()+itemThree.getLethality()+
                itemFour.getLethality()+itemFive.getLethality()+itemSix.getLethality());
        gatheredItem.setAbilityPower(itemOne.getAbilityPower()+itemTwo.getAbilityPower()+itemThree.getAbilityPower()+
                itemFour.getAbilityPower()+itemFive.getAbilityPower()+itemSix.getAbilityPower());
        gatheredItem.setFlatMagicPen(itemOne.getFlatMagicPen()+itemTwo.getFlatMagicPen()+itemThree.getFlatMagicPen()+
                itemFour.getFlatMagicPen()+itemFive.getFlatMagicPen()+itemSix.getFlatMagicPen());
        gatheredItem.setPercentMagicPen(itemOne.getPercentMagicPen()+itemTwo.getPercentMagicPen()+itemThree.getPercentMagicPen()+
                itemFour.getPercentMagicPen()+itemFive.getPercentMagicPen()+itemSix.getPercentMagicPen());
        gatheredItem.setMultiplyTotalAp(itemOne.getMultiplyTotalAp()+itemTwo.getMultiplyTotalAp()+itemThree.getMultiplyTotalAp()+
                itemFour.getMultiplyTotalAp()+itemFive.getMultiplyTotalAp()+itemSix.getMultiplyTotalAp());
        gatheredItem.setMultiplyTotal(itemOne.getMultiplyTotal()+itemTwo.getMultiplyTotal()+itemThree.getMultiplyTotal()+
                itemFour.getMultiplyTotal()+itemFive.getMultiplyTotal()+itemSix.getMultiplyTotal());
        gatheredItem.setMaxHPDpsAP(itemOne.getMaxHPDpsAP()+itemTwo.getMaxHPDpsAP()+itemThree.getMaxHPDpsAP()+
                itemFour.getMaxHPDpsAP()+itemFive.getMaxHPDpsAP()+itemSix.getMaxHPDpsAP());
        gatheredItem.setAPDps(itemOne.getAPDps()+itemTwo.getAPDps()+itemThree.getAPDps()+
                itemFour.getAPDps()+itemFive.getAPDps()+itemSix.getAPDps());
        gatheredItem.setPercentAPDps(itemOne.getPercentAPDps()+itemTwo.getPercentAPDps()+itemThree.getPercentAPDps()+
                itemFour.getPercentAPDps()+itemFive.getPercentAPDps()+itemSix.getPercentAPDps());
        gatheredItem.setADDps(itemOne.getADDps()+itemTwo.getADDps()+itemThree.getADDps()+
                itemFour.getADDps()+itemFive.getADDps()+itemSix.getADDps());
        gatheredItem.setMultiplyOnHit(itemOne.getMultiplyOnHit()+itemTwo.getMultiplyOnHit()+itemThree.getMultiplyOnHit()+
                itemFour.getMultiplyOnHit()+itemFive.getMultiplyOnHit()+itemSix.getMultiplyOnHit());
        gatheredItem.setOnHitAD(itemOne.getOnHitAD()+itemTwo.getOnHitAD()+itemThree.getOnHitAD()+
                itemFour.getOnHitAD()+itemFive.getOnHitAD()+itemSix.getOnHitAD());
        gatheredItem.setMaxHPOnHitAD(itemOne.getMaxHPOnHitAD()+itemTwo.getMaxHPOnHitAD()+itemThree.getMaxHPOnHitAD()+
                itemFour.getMaxHPOnHitAD()+itemFive.getMaxHPOnHitAD()+itemSix.getMaxHPOnHitAD());
        gatheredItem.setOnHitAP(itemOne.getOnHitAP()+itemTwo.getOnHitAP()+itemThree.getOnHitAP()+
                itemFour.getOnHitAP()+itemFive.getOnHitAP()+itemSix.getOnHitAP());
        gatheredItem.setPercentOnHitAP(itemOne.getPercentOnHitAP()+itemTwo.getPercentOnHitAP()+itemThree.getPercentOnHitAP()+
                itemFour.getPercentOnHitAP()+itemFive.getPercentOnHitAP()+itemSix.getPercentOnHitAP());
        gatheredItem.setMultiplyCritDamage(itemOne.getMultiplyCritDamage()+itemTwo.getMultiplyCritDamage()+itemThree.getMultiplyCritDamage()+
                itemFour.getMultiplyCritDamage()+itemFive.getMultiplyCritDamage()+itemSix.getMultiplyCritDamage());

        return gatheredItem;
    }

    private void saveData(String name){

        if(!name.equals("")) {
            buildsList.add(name);
            adapterSavedBuilds.notifyDataSetChanged();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_PREFIX, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        JSONArray itemsArray = new JSONArray();
        itemsArray.put(itemPositions[0]);
        itemsArray.put(itemPositions[1]);
        itemsArray.put(itemPositions[2]);
        itemsArray.put(itemPositions[3]);
        itemsArray.put(itemPositions[4]);
        itemsArray.put(itemPositions[5]);

        editor.putString(name, itemsArray.toString()); //looks like name, 0, 1, 2, 3, 4, 5
        if(!name.equals("")) {
            // Save the name to the set of build names
            Set<String> savedNames = sharedPreferences.getStringSet("build_names", new HashSet<>());
            Set<String> updatedNames = new HashSet<>(savedNames); // Cloning to avoid modifying a live reference
            updatedNames.add(name);
            editor.putStringSet("build_names", updatedNames);
        }
        editor.apply();
    }

    private void loadData(String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_PREFIX, 0);
        String buildJson = sharedPreferences.getString(name, null);

        if (buildJson != null) {
            try {
                JSONArray itemsArray = new JSONArray(buildJson);

                for (int i = 0; i < itemsArray.length(); i++) {
                    itemPositions[i] = itemsArray.getInt(i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (name.equals("")) {
            Set<String> savedNames = sharedPreferences.getStringSet("build_names", new HashSet<>());
            // adding each name to the list
            for (String buildName : savedNames) {
                buildsList.add(buildName);
            }

        }

        selectItem1.setSelection(itemPositions[0]);
        selectItem2.setSelection(itemPositions[1]);
        selectItem3.setSelection(itemPositions[2]);
        selectItem4.setSelection(itemPositions[3]);
        selectItem5.setSelection(itemPositions[4]);
        selectItem6.setSelection(itemPositions[5]);
    }
    private void loadData2(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_PREFIX, 0);
        String buildJson = sharedPreferences.getString(name, null);

        if (buildJson != null) {
            try {
                JSONArray itemsArray = new JSONArray(buildJson);

                for (int i = 0; i < itemsArray.length(); i++) {
                    itemPositions2[i] = itemsArray.getInt(i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (name.equals("")) {

        }

        selectItem1_2.setSelection(itemPositions2[0]);
        selectItem2_2.setSelection(itemPositions2[1]);
        selectItem3_2.setSelection(itemPositions2[2]);
        selectItem4_2.setSelection(itemPositions2[3]);
        selectItem5_2.setSelection(itemPositions2[4]);
        selectItem6_2.setSelection(itemPositions2[5]);
    }

    private void generateAll() { //generates the entire screen
        //reset buttons
        buttonPickA.setText("PICK A");
        buttonPickB.setText("PICK B");
        //reset background colours
        buttonPickA.setBackgroundColor(Color.parseColor("#31C3FFC5"));
        buttonPickB.setBackgroundColor(Color.parseColor("#31C3FFC5"));
        //set text colours
        buttonPickA.setTextColor(Color.parseColor("#FFFFFF")); // white
        buttonPickB.setTextColor(Color.parseColor("#FFFFFF")); // white
        // enable both buttons again
        buttonPickA.setEnabled(true);
        buttonPickB.setEnabled(true);


        // remove / reset the visibility of the now visible dps text
        viewHiddenDPSText.setVisibility(View.GONE);
        viewHiddenDPSText2.setVisibility(View.GONE);


        //0 for all ad, 1 for all AP, 2 is for fully random build
        Random buildType = new Random();
        generateRandomBuild(buildType.nextInt(2));

        //Generating random enemy stats
        Random seekBarRandom = new Random();
        seekBarHP.setProgress(seekBarRandom.nextInt(seekBarHP.getMax()+1));
        seekBarArmour.setProgress(seekBarRandom.nextInt(seekBarArmour.getMax()+1));
        seekBarMR.setProgress(seekBarRandom.nextInt(seekBarMR.getMax()+1));
    }

    private void generateRandomBuild(int buildType) { //generates a random build based on the random build type selected
        int[] itemsSet = new int[0];
        switch (buildType){
            case (0)://AD
                itemsSet = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39};
                break;
            case (1)://AP
                itemsSet = new int[]{0, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60};
                break;
            case (2)://ALL
                itemsSet = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60};
                break;
            default:
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                break;
        }

        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            itemPositions[i] = itemsSet[random.nextInt(itemsSet.length)]; // random item positions for left side
            itemPositions2[i] = itemsSet[random.nextInt(itemsSet.length)]; // random item positions for right side
        }
        setAllSpinnerPositions();
    }
    public void setAllSpinnerPositions(){
        selectItem1.setSelection(itemPositions[0]);
        selectItem2.setSelection(itemPositions[1]);
        selectItem3.setSelection(itemPositions[2]);
        selectItem4.setSelection(itemPositions[3]);
        selectItem5.setSelection(itemPositions[4]);
        selectItem6.setSelection(itemPositions[5]);

        selectItem1_2.setSelection(itemPositions2[0]);
        selectItem2_2.setSelection(itemPositions2[1]);
        selectItem3_2.setSelection(itemPositions2[2]);
        selectItem4_2.setSelection(itemPositions2[3]);
        selectItem5_2.setSelection(itemPositions2[4]);
        selectItem6_2.setSelection(itemPositions2[5]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGenerate:
                Toast.makeText(this, "Generated", Toast.LENGTH_LONG).show();
                generateAll();

                break;
            case R.id.buttonBuild:
                Intent intentBuild = new Intent(this, MainActivity.class);
                View root = findViewById(android.R.id.content);
                root.post(() -> {
                    YoYo.with(Techniques.SlideOutLeft)
                            .duration(200)
                            .withListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    // Start the next activity after the animation ends
                                    startActivity(intentBuild);
                                    // Optional: remove transition to avoid conflict
                                    overridePendingTransition(0, 0);
                                }
                            })
                            .playOn(root);
                });

                break;
            case R.id.buttonCompare:

                Intent intentCompareBuild = new Intent(this, CompareActivity.class);

                View root2 = findViewById(android.R.id.content);
                root2.post(() -> {
                    YoYo.with(Techniques.SlideOutLeft)
                            .duration(200)
                            .withListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    // Start the next activity after the animation ends
                                    startActivity(intentCompareBuild);
                                    // Optional: remove transition to avoid conflict
                                    overridePendingTransition(0, 0);
                                }
                            })
                            .playOn(root2);
                });

                break;
            case R.id.buttonPuzzle:

                break;

        }
    }


}
