package com.example.akrysa.instagram;

import android.os.AsyncTask;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetTokenInstagram {

    private DialogUsers dialogUsers;

    public String strAuthUrl;
    public String strRedirectUri;
    public String strClientId;
    public String strClientSecret;
    public CurrentSession currentSession;
    public MainActivity cont;

    public GetTokenInstagram(MainActivity context) {
        cont = context;
        strClientId = "72db96229b694b7ab25f15576ba4d895";
        strClientSecret = "3b2c984102024d4a90c92c1b00b97e6b";
        strRedirectUri = "http://zadanieschool.esy.es/";

        String StrURL = "https://api.instagram.com/oauth/authorize/?";
        strAuthUrl = StrURL + "client_id=" + strClientId + "&redirect_uri=" + strRedirectUri + "&response_type=code";
        currentSession = new CurrentSession(cont);

        dialogUsers = new DialogUsers(context, strAuthUrl, strRedirectUri, this);
    }

    public void authorize() {
        dialogUsers.show();
    }

    public void setErrorRedirect(String strError) {
        Toast.makeText(cont, strError, Toast.LENGTH_LONG).show();
    }

    public void setCodeRedirect(String code) {
        new AccessTokenTask(code).execute();
    }

    public class AccessTokenTask extends AsyncTask<URL, Integer, Long> {
        CurrentUser user;
        String code;
        String ACCESS_TOKEN_URL = "https://api.instagram.com/oauth/access_token";

        public AccessTokenTask(String code) {
            this.code = code;
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>(5);

                params.add(new BasicNameValuePair("client_id", strClientId));
                params.add(new BasicNameValuePair("client_secret", strClientSecret));
                params.add(new BasicNameValuePair("grant_type", "authorization_code"));
                params.add(new BasicNameValuePair("redirect_uri", strRedirectUri));
                params.add(new BasicNameValuePair("code", code));

                WebPost request = new WebPost();
                String response = request.post(ACCESS_TOKEN_URL, params);

                if (!response.equals("")) {
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
                    JSONObject jsonUser = jsonObj.getJSONObject("user");

                    user = new CurrentUser();
                    user.accessToken = jsonObj.getString("access_token");
                    user.id = jsonUser.getString("id");
                    user.username = jsonUser.getString("username");
                    user.fullName = jsonUser.getString("full_name");
                    user.profilPicture = jsonUser.getString("profile_picture");
                }
            } catch (Exception e) {
                setErrorRedirect(e.getMessage());
            }
            return result;
        }

        protected void onPostExecute(Long result) {
            if (user != null) {
                currentSession.seveSession(user);
                cont.refreshActivity();
            } else {

            }
        }
    }
}