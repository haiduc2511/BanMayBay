package com.example.banmaybay2;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String PREF_NAME = "Preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveData(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getData(String key) {
        return sharedPreferences.getString(key, "");
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }


    public void saveBulletsPerShot(String bulletsPerShot) {
        this.saveData("Bullets per shot", bulletsPerShot);
    }

    public String getBulletsPerShot() {
        if (!contains("Bullets per shot")) {
            return "1"; // Default value
        }
        return this.getData("Bullets per shot");
    }

    public void saveMyPlaneHealth(String myPlaneHealth) {
        this.saveData("My plane health", myPlaneHealth);
    }

    public String getMyPlaneHealth() {
        if (!contains("My plane health")) {
            return "4"; // Default value
        }
        return this.getData("My plane health");
    }

    public void saveEnemyPlaneHealth(String enemyPlaneHealth) {
        this.saveData("Enemy plane health", enemyPlaneHealth);
    }

    public String getEnemyPlaneHealth() {
        if (!contains("Enemy plane health")) {
            return "6"; // Default value
        }
        return this.getData("Enemy plane health");
    }

    public void saveBulletSpeed(String bulletSpeed) {
        this.saveData("Bullet speed", bulletSpeed);
    }

    public String getBulletSpeed() {
        if (!contains("Bullet speed")) {
            return "10"; // Default value
        }
        return this.getData("Bullet speed");
    }

    public void saveLevel(String level) {
        this.saveData("Level", level);
    }

    public String getLevel() {
        if (!contains("Level")) {
            return "1"; // Default value
        }
        return this.getData("Level");
    }
}
