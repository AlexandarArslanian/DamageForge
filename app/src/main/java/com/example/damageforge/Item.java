package com.example.damageforge;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;

public class Item implements Serializable {
    private String name = "";
    private double attackDamage = 0;
    private double critChance = 0; //for Yun Tal Wildarrows + 25%
    private double attackSpeed = 0; //for Yun Tal Wildarrows + 30%
    private double percentArmourPen = 0; //for Black Cleaver 30%, Mortal Reminder 30%, Serylda's Grudge 30%, Lord Dominik's Regards 35%, Terminus 30%
    private double lethality = 0; //for Axiom Arc, Edge of Night, Hubris, Profane Hydra, Voltaic Cyclosword, Youmuu's Ghostblade, The Collector, Serpent's Fang, Opportunity, Umbral Glaive
    private double abilityPower = 0; //for Seraph's Embrace +25, Rod of Ages +30
    private double flatMagicPen = 0; //for Shadowflame 15, Malignance 10, Stormsurge 15,
    private double percentMagicPen = 0; //for Terminus 30%, Cryptbloom 30%, Void Staff 40%, Bloodletter's Curse
    private double multiplyTotalAp = 0; //for Rabadon's Deathcap + Blackfire Torch
    private double multiplyTotal = 0; //for Spear of Shojin + Riftmaker + Horizon Focus + Shadowflame + Liandry's Torment
    private double maxHPDpsAP = 0; //for Liandry's Torment
    private double APDps = 0; //for Statikk Shiv + Blackfire Torch + Malignance + Luden's Companion + Stormsurge
    private double PercentAPDps = 0; //for Blackfire Torch + Lich Bane + Malignance + Luden's Companion + Stormsurge
    private double ADDps = 0; //for Trinity Force + Lich Bane
    private double multiplyOnHit = 0; //for Guinsoo's Rageblade
    private double onHitAD = 0; //for Kraken Slayer
    private double maxHPOnHitAD = 0; //for Blade of the Ruined King
    private double onHitAP = 0; //for Terminus + Wit's End + Guinsoo's Rageblade + Nashor's Tooth
    private double PercentOnHitAP = 0; //for Nashor's Tooth
    private double multiplyCritDamage = 0; //for Infinity Edge
    public Item(){

    }
    public Item(String name, double attackDamage , double critChance, double attackSpeed, double percentArmourPen, double lethality, double abilityPower, double flatMagicPen, double percentMagicPen,
                double multiplyTotalAp, double multiplyTotal, double maxHPDpsAP, double APDps, double PercentAPDps, double ADDps, double multiplyOnHit,
                double onHitAD, double maxHPOnHitAD, double onHitAP, double PercentOnHitAP, double multiplyCritDamage) {
        this.name = name;
        this.attackDamage = attackDamage;
        this.critChance = critChance;
        this.attackSpeed = attackSpeed;
        this.percentArmourPen = percentArmourPen;
        this.lethality = lethality;
        this.abilityPower = abilityPower;
        this.flatMagicPen = flatMagicPen;
        this.percentMagicPen = percentMagicPen;
        this.multiplyTotalAp = multiplyTotalAp;
        this.multiplyTotal = multiplyTotal;
        this.maxHPDpsAP = maxHPDpsAP;
        this.APDps = APDps;
        this.PercentAPDps = PercentAPDps;
        this.ADDps = ADDps;
        this.multiplyOnHit = multiplyOnHit;
        this.onHitAD = onHitAD;
        this.maxHPOnHitAD = maxHPOnHitAD;
        this.onHitAP = onHitAP;
        this.PercentOnHitAP = PercentOnHitAP;
        this.multiplyCritDamage = multiplyCritDamage;

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getAttackDamage() {
        return attackDamage;
    }
    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }
    public double getCritChance() {
        return critChance;
    }
    public void setCritChance(double critChance) {
        this.critChance = critChance;
    }
    public double getAttackSpeed() {
        return attackSpeed;
    }
    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
    public double getPercentArmourPen() {
        return percentArmourPen;
    }
    public void setPercentArmourPen(double percentArmourPen) {this.percentArmourPen = percentArmourPen; }
    public double getLethality() {
        return lethality;
    }
    public void setLethality(double lethality) {
        this.lethality = lethality;
    }
    public double getAbilityPower() {
        return abilityPower;
    }
    public void setAbilityPower(double abilityPower) {
        this.abilityPower = abilityPower;
    }
    public double getFlatMagicPen() {
        return flatMagicPen;
    }
    public void setFlatMagicPen(double flatMagicPen) {
        this.flatMagicPen = flatMagicPen;
    }
    public double getPercentMagicPen() {
        return percentMagicPen;
    }
    public void setPercentMagicPen(double percentMagicPen) {this.percentMagicPen = percentMagicPen; }
    public double getMultiplyTotalAp() {
        return multiplyTotalAp;
    }
    public void setMultiplyTotalAp(double multiplyTotalAp) {this.multiplyTotalAp = multiplyTotalAp; }
    public double getMultiplyTotal() {
        return multiplyTotal;
    }
    public void setMultiplyTotal(double multiplyTotal) {this.multiplyTotal = multiplyTotal; }
    public double getMaxHPDpsAP() {
        return maxHPDpsAP;
    }
    public void setMaxHPDpsAP(double maxHPDpsAP) {this.maxHPDpsAP = maxHPDpsAP; }
    public double getAPDps() {
        return APDps;
    }
    public void setAPDps(double APDps) {this.APDps = APDps; }
    public double getPercentAPDps() {
        return PercentAPDps;
    }
    public void setPercentAPDps(double PercentAPDps) {this.PercentAPDps = PercentAPDps; }
    public double getADDps() {return ADDps; }
    public void setADDps(double ADDps) {this.ADDps = ADDps; }
    public double getMultiplyOnHit() {
        return multiplyOnHit;
    }
    public void setMultiplyOnHit(double multiplyOnHit) {this.multiplyOnHit = multiplyOnHit; }
    public double getOnHitAD() {
        return onHitAD;
    }
    public void setOnHitAD(double onHitAD) {this.onHitAD = onHitAD; }
    public double getMaxHPOnHitAD() {
        return maxHPOnHitAD;
    }
    public void setMaxHPOnHitAD(double maxHPOnHitAD) {this.maxHPOnHitAD = maxHPOnHitAD; }
    public double getOnHitAP() {
        return onHitAP;
    }
    public void setOnHitAP(double onHitAP) {this.onHitAP = onHitAP; }
    public double getPercentOnHitAP() {
        return PercentOnHitAP;
    }
    public void setPercentOnHitAP(double PercentOnHitAP) {this.PercentOnHitAP = PercentOnHitAP; }
    public double getMultiplyCritDamage() {
        return multiplyCritDamage;
    }
    public void setMultiplyCritDamage(double multiplyCritDamage) {this.multiplyCritDamage = multiplyCritDamage; }
    public void setYunTalExtras(double attackSpeed, double critChance){this.attackSpeed = this.attackSpeed + attackSpeed;this.critChance = this.critChance + critChance;}
    public void setGuinsooExtras(double attackSpeed){this.attackSpeed = this.attackSpeed + attackSpeed;}
    public void setAPExtras(double abilityPower){this.abilityPower = this.abilityPower + abilityPower;}

    public String toString() {

        return getName() + " " + getAttackDamage() + " " + getCritChance() + " " + getAttackSpeed() + " " + getPercentArmourPen() + " " + getLethality() + " " + getAbilityPower() + " " + getFlatMagicPen() + " " + getPercentMagicPen()
                + "\n" + getMultiplyTotalAp() + " " + getMultiplyTotal() + " " + getMaxHPDpsAP() + " " + getAPDps() + " " + getPercentAPDps() + " " + getADDps() + "\n" + " " +
                getMultiplyOnHit() + " " + getOnHitAD() + " " + getMaxHPOnHitAD() + " " + getOnHitAP() + " " + getPercentOnHitAP() + " " + getMultiplyCritDamage();
    }

    public static Item parseJSONObject (JSONObject object) {
        Item item = new Item();
        try {//get all needed variables of the item

            if (object.has("name")) {
                item.setName(object.getString("name"));
            }
            if (object.getJSONObject("stats").has("FlatPhysicalDamageMod")) {
                item.setAttackDamage(object.getJSONObject("stats").getDouble("FlatPhysicalDamageMod"));
            }
            if (object.getJSONObject("stats").has("FlatCritChanceMod")) {
                item.setCritChance(object.getJSONObject("stats").getDouble("FlatCritChanceMod"));
            }
            if (object.getJSONObject("stats").has("PercentAttackSpeedMod")) {
                item.setAttackSpeed(object.getJSONObject("stats").getDouble("PercentAttackSpeedMod"));
            }
            if (object.getJSONObject("stats").has("FlatMagicDamageMod")) {
                item.setAbilityPower(object.getJSONObject("stats").getDouble("FlatMagicDamageMod"));
            }

            //Set lethality values
            if (item.getName().equals("Axiom Arc") || item.getName().equals("Hubris") || item.getName().equals("Profane Hydra") || item.getName().equals("Voltaic Cyclosword") || item.getName().equals("Youmuu's Ghostblade")){ item.setLethality(18);}
            if (item.getName().equals("Edge of Night") || item.getName().equals("Serpent's Fang") || item.getName().equals("Umbral Glaive")){ item.setLethality(15);}
            if (item.getName().equals("Opportunity")){ item.setLethality(22);} if (item.getName().equals("The Collector")){ item.setLethality(10);}

            //Set Percent Armour pen items
            if (item.getName().equals("Black Cleaver") || item.getName().equals("Terminus")){ item.setPercentArmourPen(0.30);}
            if (item.getName().equals("Mortal Reminder") || item.getName().equals("Serylda's Grudge")){ item.setPercentArmourPen(0.35);}
            if (item.getName().equals("Lord Dominik's Regards")){ item.setPercentArmourPen(0.40);}

            //Set maxHPOnHitAD for Blade of the Ruined King
            if (item.getName().equals("Blade of The Ruined King")){ item.setMaxHPOnHitAD(0.0425);}

            //Set Yun Tal Wildarrows
            if (item.getName().equals("Yun Tal Wildarrows")){ item.setYunTalExtras(0.30, 0.25);}

            //Set onHitAD
            if (item.getName().equals("Kraken Slayer")){ item.setOnHitAD(75);}

            //Set ADDps
            if (item.getName().equals("Trinity Force")){ item.setOnHitAD(60);} if (item.getName().equals("Lich Bane")){ item.setOnHitAD(18);}

            //Set onHitAP
            if (item.getName().equals("Terminus") || item.getName().equals("Guinsoo's Rageblade")){ item.setOnHitAP(30);} if (item.getName().equals("Wit's End")){ item.setOnHitAP(45);}
            if (item.getName().equals("Nashor's Tooth")){ item.setOnHitAP(15); item.setPercentOnHitAP(0.15);}

            //Set Guinsoo's Rageblade's extra Attack speed and Multiplier for on-hit
            if (item.getName().equals("Guinsoo's Rageblade")){ item.setGuinsooExtras(0.32); item.setMultiplyOnHit(0.30);}

            //Set APDps
            if (item.getName().equals("Statikk Shiv")){ item.setAPDps(18);} if (item.getName().equals("Stormsurge")){ item.setAPDps(12.5);} if (item.getName().equals("Blackfire Torch")){ item.setAPDps(20); item.setPercentAPDps(0.02);}
            if (item.getName().equals("Malignance")){ item.setAPDps(40);} if (item.getName().equals("Luden's Companion")){ item.setAPDps(15);}

            //Set PercentAPDps Stormsurge Blackfire Torch
            if (item.getName().equals("Stormsurge") || item.getName().equals("Luden's Companion")){ item.setPercentAPDps(0.01);} if (item.getName().equals("Lich Bane")){ item.setPercentAPDps(0.12);}
            if (item.getName().equals("Malignance")){ item.setPercentAPDps(0.04);}

            //Set multiplyTotal
            if (item.getName().equals("Spear of Shojin") || item.getName().equals("Liandry's Torment")){ item.setPercentAPDps(0.06);} if (item.getName().equals("Riftmaker") || item.getName().equals("Shadowflame")){ item.setPercentAPDps(0.08);}
            if (item.getName().equals("Horizon Focus")){ item.setPercentAPDps(0.1);}

            //Set multiplyTotalAp
            if (item.getName().equals("Blackfire Torch")){ item.setMultiplyTotalAp(0.08);} if (item.getName().equals("Rabadon's Deathcap")){ item.setMultiplyTotalAp(0.3);}

            //Set flatMagicPen
            if (item.getName().equals("Stormsurge") || item.getName().equals("Shadowflame")){ item.setFlatMagicPen(15);} if (item.getName() == "Malignance" ){ item.setFlatMagicPen(10);}

            //Set Extra AP
            if (item.getName().equals("Seraph's Embrace")){ item.setAPExtras(25);} if (item.getName().equals("Rod of Ages")){ item.setAPExtras(30);}

            //Set percentMagicPen
            if (item.getName().equals("Terminus") || item.getName().equals("Cryptbloom") || item.getName().equals("Bloodletter's Curse")){ item.setPercentMagicPen(0.3);}
            if (item.getName().equals("Void Staff")){ item.setPercentMagicPen(0.4);}

            //Set maxHPDpsAP
            if (item.getName().equals("Liandry's Torment")){ item.setMaxHPDpsAP(0.02);}

            //Set multiplyCritDamage
            if (item.getName().equals("Infinity Edge")){ item.setMultiplyCritDamage(0.4);}

        } catch(Exception e) {
            Log.e("parseJSONObjectError", "Exception", e);
        }
        return item;
    }

    public static LinkedList<Item> parseJSONObjectList (JSONObject array) {//gets the item based on their id for processing
        LinkedList<Item> items = new LinkedList<>();

        try{
                if(array.getJSONObject("data").has("3032")) {//Yun Tal Wildarrows
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3032")));
                }
                if(array.getJSONObject("data").has("3031")) {//Infinity Edge
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3031")));
                }
                if(array.getJSONObject("data").has("6673")) {//Immortal Shieldbow
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6673")));
                }
                if(array.getJSONObject("data").has("6676")) {//The Collector
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6676")));
                }
                if(array.getJSONObject("data").has("3033")) {//Mortal Reminder
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3033")));
                }
                if(array.getJSONObject("data").has("3036")) {//Lord Dominik's Regards
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3036")));
                }
                if(array.getJSONObject("data").has("3072")) {//Bloodthirster
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3072")));
                }
                if(array.getJSONObject("data").has("3046")) {//Phantom Dancer
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3046")));
                }
                if(array.getJSONObject("data").has("6675")) {//Navori Flickerblade
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6675")));
                }
                if(array.getJSONObject("data").has("3508")) {//Essence Reaver
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3508")));
                }
                if(array.getJSONObject("data").has("6696")) {//Axiom Arc
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6696")));
                }
                if(array.getJSONObject("data").has("3071")) {//Black Cleaver
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3071")));
                }
                if(array.getJSONObject("data").has("6697")) {//Hubris
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6697")));
                }
                if(array.getJSONObject("data").has("6698")) {//Profane Hydra
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6698")));
                }
                if(array.getJSONObject("data").has("6699")) {//Voltaic Cyclosword
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6699")));
                }
                if(array.getJSONObject("data").has("3142")) {//Youmuu's Ghostblade
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3142")));
                }
                if(array.getJSONObject("data").has("6695")) {//Serpent's Fang
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6695")));
                }
                if(array.getJSONObject("data").has("6694")) {//Serylda's Grudge
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6694")));
                }
                if(array.getJSONObject("data").has("6701")) {//Opportunity
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6701")));
                }
                if(array.getJSONObject("data").has("3179")) {//Umbral Glaive
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3179")));
                }
                if(array.getJSONObject("data").has("6609")) {//Chempunk Chainsword
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6609")));
                }
                if(array.getJSONObject("data").has("3026")) {//Guardian Angel
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3026")));
                }
                if(array.getJSONObject("data").has("6692")) {//Eclipse
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6692")));
                }
                if(array.getJSONObject("data").has("6333")) {//Death's Dance
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6333")));
                }
                if(array.getJSONObject("data").has("3139")) {//Mercurial Scimitar
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3139")));
                }
                if(array.getJSONObject("data").has("3156")) {//Maw of Malmortius
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3156")));
                }
                if(array.getJSONObject("data").has("3074")) {//Ravenous Hydra
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3074")));
                }
                if(array.getJSONObject("data").has("3078")) {//Trinity Force
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3078")));
                }
                if(array.getJSONObject("data").has("6631")) {//Stridebreaker
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6631")));
                }
                if(array.getJSONObject("data").has("3087")) {//Statikk Shiv
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3087")));
                }
                if(array.getJSONObject("data").has("3161")) {//Spear of Shojin
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3161")));
                }

                if(array.getJSONObject("data").has("3094")) {//Rapid Firecannon
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3094")));
                }
                if(array.getJSONObject("data").has("3085")) {//Runaan's Hurricane
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3085")));
                }
                if(array.getJSONObject("data").has("6672")) {//Kraken Slayer
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6672")));
                }
                if(array.getJSONObject("data").has("3091")) {//Wit's End
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3091")));
                }
                if(array.getJSONObject("data").has("3153")) {//Blade of the Ruined King
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3153")));
                }
                if(array.getJSONObject("data").has("3302")) {//Terminus
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3302")));
                }
                if(array.getJSONObject("data").has("3124")) {//Guinsoo's Rageblade
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3124")));
                }
    //===============================MAGE ITEMS===========================================================================================
                if(array.getJSONObject("data").has("3118")) {//Malignance
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3118")));
                }
                if(array.getJSONObject("data").has("6655")) {//Luden's Companion
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6655")));
                }
                if(array.getJSONObject("data").has("3137")) {//Cryptbloom
                        items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3137")));
                    }
                if(array.getJSONObject("data").has("3089")) {//Rabadon's Deathcap
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3089")));
                }
                if(array.getJSONObject("data").has("4645")) {//Shadowflame
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("4645")));
                }
                if(array.getJSONObject("data").has("3135")) {//Void Staff
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3135")));
                }
                if(array.getJSONObject("data").has("8010")) {//Bloodletter's Curse
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("8010")));
                }
                if(array.getJSONObject("data").has("4646")) {//Stormsurge
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("4646")));
                }
                if(array.getJSONObject("data").has("4633")) {//Riftmaker
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("4633")));
                }
                if(array.getJSONObject("data").has("3040")) {//Seraph's Embrace
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3040")));
                }
                if(array.getJSONObject("data").has("2503")) {//Blackfire Torch
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("2503")));
                }
                if(array.getJSONObject("data").has("4629")) {//Cosmic Drive
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("4629")));
                }
                if(array.getJSONObject("data").has("3152")) {//Hextech Rocketbelt
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3152")));
                }
                if(array.getJSONObject("data").has("4628")) {//Horizon Focus
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("4628")));
                }
                if(array.getJSONObject("data").has("6653")) {//Liandry's Torment
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6653")));
                }
                if(array.getJSONObject("data").has("6657")) {//Rod of Ages
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("6657")));
                }
                if(array.getJSONObject("data").has("3116")) {//Rylai's Crystal Scepter
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3116")));
                }

                if(array.getJSONObject("data").has("3100")) {//Lich Bane
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3100")));
                }
                if(array.getJSONObject("data").has("3115")) {//Nashor's Tooth
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3115")));
                }
                if(array.getJSONObject("data").has("3165")) {//Morellonomicon
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3165")));
                }
                if(array.getJSONObject("data").has("3102")) {//Banshee's Veil
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3102")));
                }
                if(array.getJSONObject("data").has("3157")) {//Zhonya's Hourglass
                    items.add(parseJSONObject(array.getJSONObject("data").getJSONObject("3157")));
                }


        }catch(Exception e) {
            Log.e("parseJSONObjectListErr", "Exception", e);
        }

        return items;
    }



}
