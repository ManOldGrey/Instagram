package com.example.akrysa.instagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class CurrentSession {
	private Context cnt;
	private SharedPreferences sharedPref;

	private static final String SHARED = "Instagram";
	private static final String USERID	= "userid";
	private static final String USERNAME = "username";
	private static final String FULLNAME = "fullname";
	private static final String PROFILPIC = "profilpic";
	private static final String ACCESS_TOKEN = "access_token";

	public CurrentSession(Context context) {
		cnt	= context;
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
	}
	
	public void seveSession(CurrentUser currentUser) {
		Editor editor = sharedPref.edit();
		
		editor.putString(ACCESS_TOKEN,  currentUser.accessToken);
		editor.putString(USERID, 		currentUser.id);
		editor.putString(USERNAME, 		currentUser.username);
		editor.putString(FULLNAME, 		currentUser.fullName);
		editor.putString(PROFILPIC, 	currentUser.profilPicture);
		
		editor.commit();
	}
	
	public void clearSession() {
		Editor editor = sharedPref.edit();
		
		editor.putString(ACCESS_TOKEN, 	"");
		editor.putString(USERID, 		"");
		editor.putString(USERNAME, 		"");
		editor.putString(FULLNAME, 		"");
		editor.putString(PROFILPIC, 	"");
		
		editor.commit();
		
		CookieSyncManager.createInstance(cnt);
		
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}
	
	public CurrentUser getUser() {
		if (sharedPref.getString(ACCESS_TOKEN, "").equals("")) {
			return null;
		}
		
		CurrentUser user 	= new CurrentUser();
		
		user.id				= sharedPref.getString(USERID, "");
		user.username		= sharedPref.getString(USERNAME, "");
		user.fullName		= sharedPref.getString(FULLNAME, "");
		user.profilPicture	= sharedPref.getString(PROFILPIC, "");
		user.accessToken	= sharedPref.getString(ACCESS_TOKEN, "");
		
		return user;
	}
	
	public String getAccessToken() {
		return sharedPref.getString(ACCESS_TOKEN, "");
	}
	
	public boolean isActiveSession() {
		return (sharedPref.getString(ACCESS_TOKEN, "").equals("")) ? false : true;
	}
}