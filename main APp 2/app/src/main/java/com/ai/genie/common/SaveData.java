package com.ai.genie.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//
//
//  Created by Parbir Kaur,
//
public class SaveData {

	private Context _context;
	private SharedPreferences shared;
	private String SHARED_NAME = "SpinApp";
	private Editor edit;

	public SaveData(Context c) {
		_context = c;
		shared = _context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		edit = shared.edit();
	}

	// ============================================//
	public void save(String key, String value) {
		edit.putString(key, value);
		edit.commit();
	}
    public void save_int(String key, Integer value) {
		edit.putInt(key, value);
		edit.commit();
	}
	public void save(String key, float value) {
		edit.putFloat(key, value);
		edit.commit();
	}

	// ============================================//
	public String get(String key) {
		return shared.getString(key, key);

	}
	public int getInt(String key) {
		return shared.getInt(key, 0);

	}
	public int getAdMessage(String key) {
		return shared.getInt(key, 5);

	}
	public float getFloat(String key) {
		return shared.getFloat(key, 1f);

	}
	// ============================================//
	public boolean isExist(String key) {
		return shared.contains(key);

	}

	// ============================================//
	public void remove(String key) {

		edit.remove(key);
		edit.commit();

	}
//================================================================//
	public Object getString(String keyGurudwaraId) {
		// TODO Auto-generated method stub
		return null;
	}
	//================================================================//

	public void allDataClear(Context c) {
		_context = c;
		shared = _context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		edit = shared.edit();
		edit.clear();
		edit.apply();

	}

}
