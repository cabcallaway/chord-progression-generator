package com.example.cpg.Connectors;

import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cpg.model.User;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final String ENDPOINT = "https://api.spotify.com/v1/me";
    private SharedPreferences msharedPreferences;
    private RequestQueue mqueue;
    private User user;
    public UserService(RequestQueue queue, SharedPreferences sharedPreferences) {
        mqueue = queue;
        msharedPreferences = sharedPreferences;
    }

    public User getUser() {
        return user;
    }

    public void get(final VolleyCallBack callBack) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, null, response -> {

            user = new User();
            String spotifyId = "";
            String email = "";
            String name = "";
            try {
                spotifyId = response.getString("id");
                email = response.getString("email");
                name = response.getString("display_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (name != null){
                user.setName(name);
            }
            user.setSpotifyId(spotifyId);
            user.setEmail(email);
            callBack.onSuccess();

        }, error -> get(() -> {


        })) {

            @Override

            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();

                String token = msharedPreferences.getString("token", "");

                String auth = "Bearer " + token;

                headers.put("Authorization", auth);

                return headers;

            }

        };

        mqueue.add(jsonObjectRequest);

    }
}
