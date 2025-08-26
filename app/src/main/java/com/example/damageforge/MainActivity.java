package com.example.damageforge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Space;
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
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private final static String SHARED_PREFERENCES_PREFIX = "MainActivitySharedPreferencesPrefix";
    Spinner selectItem1;
    Spinner selectItem2;
    Spinner selectItem3;
    Spinner selectItem4;
    Spinner selectItem5;
    Spinner selectItem6;
    Button buttonBuild;
    Button buttonNewBuild;
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
    SeekBar seekBarMR;

    LinkedList<Item> itemListFull;
    Item blankItem = new Item(); //for when nothing was selected when choosing an item
    Item gatheredItem = new Item(); //for grouping all the gathered item stats

    int [] itemPositions = new int [6]; // to record every item position when selecting an item from the spinner dropdown
    private String version;
    private static String ITEMS_URL;

    // static values of the calculator, simulating the User's average values
    static final int baseAD = 100;
    static final double baseAttackSpeed = 1.1;
    static final int baseAPFlatDPS = 180;
    static final double baseAPPercentDPS = 0.55;
    static final double baseCritDamage = 1.75;

    double totalDPS, totalAD , totalAttackSpeed, totalArmourPen, totalLethality, totalCritChance, totalAP, totalPercentMagicPen, totalFlatMagicPen , armourDamageReduction, magicResistDamageReduction= 0;
    int enemyArmour = 100;
    int enemyMagicResist = 50;
    int enemyHP = 2000;
    Button buttonSave;
    Button buttonDelete;
    Spinner savedBuilds;
    LinkedList<String> buildsList;
    ArrayAdapter<String> adapterSavedBuilds;
    Space saveDelSpace;
    private void setItemUrl(String version) {
        ITEMS_URL = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/en_US/item.json";
    }

    private static final String VERSIONS_URL = "https://ddragon.leagueoflegends.com/api/versions.json"; //versions list, to use for the other api to get the most recent items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       /* View root = findViewById(android.R.id.content);

        root.post(() -> { //makes this activity animate upon entry
        YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .playOn(root);
        });
*/
        initComponents();

        GestureDetector gestureDetector = new GestureDetector(this,
                new SwipeGestureListener(this, PuzzleActivity.class, CompareActivity.class )); // this, left screen, right screen

        viewStats.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        viewStats2.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
        saveDelSpace.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

    }


    private void initComponents() {
        saveDelSpace = findViewById(R.id.saveDelSpace);
        selectItem1 = findViewById(R.id.selectItem1);
        selectItem2 = findViewById(R.id.selectItem2);
        selectItem3 = findViewById(R.id.selectItem3);
        selectItem4 = findViewById(R.id.selectItem4);
        selectItem5 = findViewById(R.id.selectItem5);
        selectItem6 = findViewById(R.id.selectItem6);

        buttonBuild = findViewById(R.id.buttonBuild);
        buttonNewBuild = findViewById(R.id.buttonClear);
        buttonCompareBuild = findViewById(R.id.buttonCompare);
        buttonPuzzle = findViewById(R.id.buttonPuzzle);
        savedBuilds = findViewById(R.id.savedBuilds);


        labelHealth = findViewById(R.id.labelHealth);
        textViewHP = findViewById(R.id.textViewHP);
        seekBarHP = findViewById(R.id.seekBarHP);

        viewStats = findViewById(R.id.viewStats);
        viewStats2 = findViewById(R.id.viewStats2);

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

        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSavePopup();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBuild(savedBuilds.getSelectedItemPosition());
            }
        });

        itemListFull = new LinkedList<Item>(); //to store all items
        itemListFull.add(blankItem);

        //Default spinner view before everything loads, in case loading takes longer
        /*List<String> loadingList = new ArrayList<>();
        loadingList.add("Select Item");

        ArrayAdapter<String> loadingAdapter = new ArrayAdapter<>(this, R.layout.colour_spinner_layout, loadingList);
        loadingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectItem1.setAdapter(loadingAdapter);
        selectItem2.setAdapter(loadingAdapter);
        selectItem3.setAdapter(loadingAdapter);
        selectItem4.setAdapter(loadingAdapter);
        selectItem5.setAdapter(loadingAdapter);
        selectItem6.setAdapter(loadingAdapter);*/

        //Get all item info from the Item URL, utilizing the URL of the Versions Api to get the latest version
        Api.getJSON(VERSIONS_URL, new ReadDataHandler(){
            @Override
            public void handleMessage(Message msg) {
                String response = getJson();

                try {

                    JSONArray array = new JSONArray(response);

                    version = array.getString(0); //get the first version of the json list of versions (most recent)

                    setItemUrl(version);

                    Api.getJSON(ITEMS_URL, new ReadDataHandler(){

                        @Override
                        public void handleMessage(Message msg) {//get the json api of items and store them for later
                            String response = getJson();

                            try {
                                JSONObject array = new JSONObject(response);
                                LinkedList<Item>items = Item.parseJSONObjectList(array);

                                ArrayList<String> itemNamesList = new ArrayList<>();//for using on the drop down selector, gathering all the names
                                itemNamesList.add("Select Item"); //To put placeholder text

                                for(int i = 0; i < items.size(); i++) {
                                    itemNamesList.add(items.get(i).getName());
                                    itemListFull.add(items.get(i));
                                }
                                runOnUiThread(()->{//updating the ui on the main thread
                                    ArrayAdapter<String> adapterItem = new ArrayAdapter<String>(MainActivity.this, R.layout.colour_spinner_layout, itemNamesList); //this, layout, List
                                    adapterItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    selectItem1.setAdapter(adapterItem);
                                    selectItem2.setAdapter(adapterItem);
                                    selectItem3.setAdapter(adapterItem);
                                    selectItem4.setAdapter(adapterItem);
                                    selectItem5.setAdapter(adapterItem);
                                    selectItem6.setAdapter(adapterItem);
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
        buildsList.add("Saved Builds"); //To put placeholder text / position 0

        adapterSavedBuilds = new ArrayAdapter<String>(MainActivity.this, R.layout.colour_spinner_layout, buildsList); //this, layout, List
        adapterSavedBuilds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        savedBuilds.setAdapter(adapterSavedBuilds); //set the spinner

        selectItem1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//When item1 is selected, do things
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {//if an item is selected and not the default position 0
                    itemPositions[0] = position;
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));

                    if(!buttonSave.isEnabled() && position > 0){ //enabling the SAVE button so it can be touched
                        buttonSave.setEnabled(true);
                        buttonSave.setBackgroundColor(Color.parseColor("#4CAF50")); // green
                        buttonSave.setTextColor(Color.parseColor("#FFFFFF")); // white
                        }
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(blankItem, itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                        itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            }
        });
        selectItem2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//When item2 is selected, do things
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {//if an item is selected and not the default position 0
                    itemPositions[1] = position;//save the item position
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));

                    if(!buttonSave.isEnabled() && position > 0){ //enabling the SAVE button so it can be touched
                        buttonSave.setEnabled(true);
                        buttonSave.setBackgroundColor(Color.parseColor("#4CAF50")); // green
                        buttonSave.setTextColor(Color.parseColor("#FFFFFF")); // white
                        }
                } else {

                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(itemListFull.get(itemPositions[0]), blankItem, itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                        itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            }
        });
        selectItem3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//When item3 is selected, do things
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {//if an item is selected and not the default position 0
                    itemPositions[2] = position;//save the item position
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));

                    if(!buttonSave.isEnabled() && position > 0){ //enabling the SAVE button so it can be touched
                        buttonSave.setEnabled(true);
                        buttonSave.setBackgroundColor(Color.parseColor("#4CAF50")); // green
                        buttonSave.setTextColor(Color.parseColor("#FFFFFF")); // white
                        }
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                        itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            }
        });
        selectItem4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//When item4 is selected, do things
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {//if an item is selected and not the default position 0
                    itemPositions[3] = position;//save the item position
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));

                    if (!buttonSave.isEnabled() && position > 0){ //enabling the SAVE button so it can be touched
                        buttonSave.setEnabled(true);
                        buttonSave.setBackgroundColor(Color.parseColor("#4CAF50")); // green
                        buttonSave.setTextColor(Color.parseColor("#FFFFFF")); // white
                    }
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), blankItem,
                           itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
            }
        });
        selectItem5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//When item5 is selected, do things
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {//if an item is selected and not the default position 0
                    itemPositions[4] = position;//save the item position
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));

                    if (!buttonSave.isEnabled() && position > 0) {//enabling the SAVE button so it can be touched
                        buttonSave.setEnabled(true);
                        buttonSave.setBackgroundColor(Color.parseColor("#4CAF50")); // green
                        buttonSave.setTextColor(Color.parseColor("#FFFFFF")); // white
                    }
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            blankItem, itemListFull.get(itemPositions[5]));
            }
        });
        selectItem6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//When item6 is selected, do things
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0) {//if an item is selected and not the default position 0
                    itemPositions[5] = position; //save the item position
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));

                    if(!buttonSave.isEnabled() && position > 0) { //enabling the SAVE button so it can be touched
                        buttonSave.setEnabled(true);
                        buttonSave.setBackgroundColor(Color.parseColor("#4CAF50")); // green
                        buttonSave.setTextColor(Color.parseColor("#FFFFFF")); // white
                    }
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {//might be not utilized at all
                calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), blankItem);
            }
        });

        savedBuilds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) { //if a build was selected
                    loadData(savedBuilds.getSelectedItem().toString()); //Loads all item positions

                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
                    buttonDelete.setEnabled(true); //enable the delete button so that builds may be deleted now
                    buttonDelete.setBackgroundColor(Color.parseColor("#F44336")); // red
                    buttonDelete.setTextColor(Color.parseColor("#FFFFFF")); // white

                } else {
                    //default position selected returns everything to default
                    selectItem1.setSelection(0); itemPositions[0] = 0;
                    selectItem2.setSelection(0); itemPositions[1] = 0;
                    selectItem3.setSelection(0); itemPositions[2] = 0;
                    selectItem4.setSelection(0); itemPositions[3] = 0;
                    selectItem5.setSelection(0); itemPositions[4] = 0;
                    selectItem6.setSelection(0); itemPositions[5] = 0;
                    calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                            itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
                    //restricts the user from saving/deleting when all default values are present
                    buttonDelete.setEnabled(false);
                    buttonDelete.setBackgroundColor(Color.parseColor("#4DFF80AB")); // different red
                    buttonDelete.setTextColor(Color.parseColor("#000000")); // black

                    buttonSave.setEnabled(false);
                    buttonSave.setBackgroundColor(Color.parseColor("#3181C784")); // different green
                    buttonSave.setTextColor(Color.parseColor("#000000")); // black
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonNewBuild.setOnClickListener(this);
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

            //update dps whenever progress is changed
            calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                    itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
        }
        if(seekBar == seekBarArmour){ //check which seekBar is it
        ((TextView)findViewById(R.id.textViewArmour)).setText(String.valueOf(i)+"0");
            enemyArmour = i * 10;

            //update dps whenever progress is changed
            calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                    itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
        }
        if(seekBar == seekBarMR){ //check which seekBar is it
            ((TextView)findViewById(R.id.textViewMR)).setText(String.valueOf(i)+"0");
            enemyMagicResist = i * 10;

            //update dps whenever progress is changed
            calculateDps(itemListFull.get(itemPositions[0]), itemListFull.get(itemPositions[1]), itemListFull.get(itemPositions[2]), itemListFull.get(itemPositions[3]),
                    itemListFull.get(itemPositions[4]), itemListFull.get(itemPositions[5]));
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData("");
    }

    public void createResultsMessage(){ //for printing all the stats
        String messageStats = "\n" + "DPS: " + (int)totalDPS + "\n";
        messageStats += "Time to kill Enemy: " + String.format("%.2f", (enemyHP / totalDPS)) + 's' + "\n" + "\n";
        messageStats += "Attack Damage: " + (int)totalAD + "\n";
        messageStats += "Attack Speed: " + String.format("%.2f", totalAttackSpeed) + "\n";
        messageStats += "Armor Pen: " + (int)(totalArmourPen*100) + "%" + "\n";
        messageStats += "Lethality: " + (int)totalLethality + "\n" + "\n";
        messageStats += "Enemy Physical Damage Reduction: " + (int)((1-armourDamageReduction)*100) + "%" + "\n";

        String messageStats2 = "\n\n\n\n";
        messageStats2 += "Crit Chance: " + (int)(totalCritChance*100) + "%" + "\n";
        messageStats2 += "Ability Power: " + (int)totalAP + "\n";
        messageStats2 += "Magic Pen: " + (int)(totalPercentMagicPen*100) + "%" + "\n";
        messageStats2 += "Flat Magic Pen: " + (int)totalFlatMagicPen + "\n" + "\n";
        messageStats2 += "Enemy Magical Damage Reduction: " + (int)((1-magicResistDamageReduction)*100) + "%";

        String finalMessageStats = messageStats;
        String finalMessageStats2 = messageStats2;

        runOnUiThread(() -> {
            viewStats.setText(finalMessageStats);
            viewStats2.setText(finalMessageStats2);
        });
    }

    public void calculateDps(Item itemOne, Item itemTwo, Item itemThree, Item itemFour, Item itemFive, Item itemSix){
        //Get all item stats into the combinedItems
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
        }else armourDamageReduction = 100/(100+armourCalc);//calculates damage amplification if values are negative
        if(magicResistCalc >= 0){
            magicResistDamageReduction = 1-(magicResistCalc/(100+magicResistCalc));//calculate damage reduction
        }else magicResistDamageReduction = 100/(100+magicResistCalc);//calculates damage amplification if values are negative

        totalPhysicalDPS = totalAttackSpeed * ((totalAD+(totalAD*totalCritChance*totalCritDamage))*armourDamageReduction + ((1+combinedItems.getMultiplyOnHit()) *
                (combinedItems.getOnHitAD()*armourDamageReduction + combinedItems.getOnHitAP()*magicResistDamageReduction + (combinedItems.getPercentOnHitAP()*totalAP)*magicResistDamageReduction + (combinedItems.getMaxHPOnHitAD()*enemyHP)*armourDamageReduction))) ;
        totalPhysicalDPS *= (1+combinedItems.getMultiplyTotal());

        totalMagicDPS = ((combinedItems.getAPDps() + baseAPFlatDPS) + ((combinedItems.getPercentAPDps()+baseAPPercentDPS)*totalAP) + combinedItems.getMaxHPDpsAP()*enemyHP)*magicResistDamageReduction + (combinedItems.getADDps()*armourDamageReduction) ;
        totalMagicDPS *= (1+combinedItems.getMultiplyTotal());
        totalDPS = totalMagicDPS + totalPhysicalDPS;

        createResultsMessage(); //calls the printing method
    }
    public Item gatherSelectedItemInfo(Item itemOne, Item itemTwo, Item itemThree, Item itemFour, Item itemFive, Item itemSix){
        //Collects all the stats of all six items and essentially makes "one item" for easy processing.
        //Collection is done one stat at a time

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

    private void showSavePopup() { //displays the save popup and handles it

        View popupView = getLayoutInflater().inflate(R.layout.window_save_name, null);

        EditText nameInput = popupView.findViewById(R.id.editSaveName); //get input

        AlertDialog builder = new AlertDialog.Builder(this) //the popup dialog
                .setView(popupView)
                .setPositiveButton("Save", (dialogInterface, which) -> {
                    String name = nameInput.getText().toString().trim();
                    if (!name.isEmpty()) {
                        if(!buildsList.contains(name)){ //check if name does not exist, so the build saves
                            saveData(name);
                            Toast.makeText(this, "Build saved", Toast.LENGTH_SHORT).show();
                        }
                        else { // if name exists, so saving fails
                            Toast.makeText(this, "Save Failed! \nPlease enter a unique name", Toast.LENGTH_SHORT).show();
                        }


                    }else { //if no input was given
                        Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        builder.setOnShowListener(dialogInterface -> { //colour the buttons
            Button positive = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negative = builder.getButton(AlertDialog.BUTTON_NEGATIVE);

            positive.setTextColor(Color.GREEN);
            negative.setTextColor(Color.RED);
        });
        builder.show();
    }
    private void deleteBuild(int selectedPosition) { //delete the build of the selected position from the saved builds
        if (selectedPosition <= 0) { // No Build is selected
            Toast.makeText(this, "Cannot delete default selection.", Toast.LENGTH_SHORT).show();

        }else {
            String buildNameToDelete = buildsList.get(selectedPosition);

            SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_PREFIX, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.remove(buildNameToDelete); // removes the saved build

            // removing from the build_names set
            Set<String> savedNames = prefs.getStringSet("build_names", new HashSet<>());
            savedNames.remove(buildNameToDelete);
            editor.putStringSet("build_names", savedNames); // update stored list
            editor.apply();

            adapterSavedBuilds.notifyDataSetChanged();
            Toast.makeText(this, "Build deleted!", Toast.LENGTH_SHORT).show();

            buildsList.remove(selectedPosition);

            //reset the UI elements to make it look like things was removed as well
            savedBuilds.setSelection(0);
            selectItem1.setSelection(0);
            selectItem2.setSelection(0);
            selectItem3.setSelection(0);
            selectItem4.setSelection(0);
            selectItem5.setSelection(0);
            selectItem6.setSelection(0);
        }
    }
    private void saveData(String name){ //save the data with the given name inputted
        if(!name.equals("")) { //check if name is not empty, input was given
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

    private void loadData(String name){ //Gets the name from shared pref and the Json array which has the item positions, and updates the UI
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_PREFIX, 0);
        String buildJson = sharedPreferences.getString(name, null);

        if(buildJson != null){
            try {
                JSONArray itemsArray = new JSONArray(buildJson);

                for(int i = 0; i < itemsArray.length(); i++) {
                    itemPositions[i] = itemsArray.getInt(i);
                }

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        if(name.equals("")) {
            Set <String> savedNames = sharedPreferences.getStringSet("build_names", new HashSet<>());
            // adding each name to the list
            for (String buildName : savedNames) {
                buildsList.add(buildName);
            }

            // updating the spinner
            adapterSavedBuilds = new ArrayAdapter<>(MainActivity.this, R.layout.colour_spinner_layout, buildsList);
            adapterSavedBuilds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            savedBuilds.setAdapter(adapterSavedBuilds);

            adapterSavedBuilds.notifyDataSetChanged();
        }
        //updating positions
        selectItem1.setSelection(itemPositions[0]);
        selectItem2.setSelection(itemPositions[1]);
        selectItem3.setSelection(itemPositions[2]);
        selectItem4.setSelection(itemPositions[3]);
        selectItem5.setSelection(itemPositions[4]);
        selectItem6.setSelection(itemPositions[5]);

    }

    public void setAllSpinnerPositionsToDefault(){ //To reset all data to the default positions
        selectItem1.setSelection(0);
        selectItem2.setSelection(0);
        selectItem3.setSelection(0);
        selectItem4.setSelection(0);
        selectItem5.setSelection(0);
        selectItem6.setSelection(0);

        savedBuilds.setSelection(0);

        seekBarHP.setProgress(20);
        seekBarArmour.setProgress(10);
        seekBarMR.setProgress(5);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonClear:
                setAllSpinnerPositionsToDefault(); //To reset all data to the default positions
                Toast.makeText(this, "Cleared", Toast.LENGTH_LONG).show();
                break;

            case R.id.buttonBuild:
                break;

            case R.id.buttonCompare:
                Intent intentCompareBuild = new Intent(this, CompareActivity.class);

                View root = findViewById(android.R.id.content);
                root.post(() -> {
                    YoYo.with(Techniques.SlideOutLeft) //To animate this activity "leaving"
                            .duration(200)
                            .withListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    // Start the next activity after the animation ends
                                    startActivity(intentCompareBuild);

                                    // Optional: removing transition to avoid conflict
                                    overridePendingTransition(0, 0);
                                }
                            })
                            .playOn(root);
                });
                break;

            case R.id.buttonPuzzle:
                Intent intentPuzzle = new Intent(this, PuzzleActivity.class);
                View root2 = findViewById(android.R.id.content);
                root2.post(() -> {
                    YoYo.with(Techniques.SlideOutRight) //To animate this activity "leaving"
                            .duration(200)
                            .withListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    // Start the next activity after the animation ends
                                    startActivity(intentPuzzle);

                                    // Optional: removing transition to avoid conflict
                                    overridePendingTransition(0, 0);
                                }
                            })
                            .playOn(root2);
                });

                break;
        }
    }


}