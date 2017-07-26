package miaoyipu.chatwithyuki;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static miaoyipu.chatwithyuki.Utility.postBotMessage;

/**
 * Created by cy804 on 2017-07-24.
 */

public class RequestQueueSingleton {
    private static final String TAG = "RQS";
    static final String UURL = "http://www.tuling123.com/openapi/api";
    static final String KEY = "2e72cb905f084783b350f492786e2538";
    private static RequestQueueSingleton requestQueueSingleton;
    private RequestQueue requestQueue;
    private static Context context;

    private RequestQueueSingleton(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (requestQueueSingleton == null) {
            requestQueueSingleton = new RequestQueueSingleton(context);
        }
        return requestQueueSingleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static void postNewMessage(final Context context, final DatabaseReference database, String message, final FirebaseUser user) {
        final RequestQueue requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();

        HashMap<String, String> params = new HashMap<>();
        params.put("key", KEY);
        params.put("info", message);
        params.put("userid", user.getUid());

        JsonObjectRequest req = new JsonObjectRequest(RequestQueueSingleton.UURL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, response.getString("text"));
                            postBotMessage(database, response.getString("text"), user.getUid());

                        } catch(JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        requestQueue.add(req);
    }

}
