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
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void postNewMessage(final Context context, final String message, final String uid) {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();

        HashMap<String, String> params = new HashMap<>();
        params.put("key", KEY);
        params.put("info", message);
        params.put("userid", uid);

        JsonObjectRequest req = new JsonObjectRequest(RequestQueueSingleton.UURL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, response.getString("text"));
                            FirebaseDatabase.getInstance()
                                    .getReference()
                                    .push()
                                    .setValue(new ChatMessage(response.getString("text"), context.getString(R.string.yuki),
                                            FirebaseAuth.getInstance().getCurrentUser().getUid()));

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


//        StringRequest sr = new StringRequest(Request.Method.POST, RequestQueueSingleton.UURL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(RequestQueueSingleton.TAG, response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("key", KEY);
//                params.put("info", message);
//
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
////                params.put("Content-type", "text/html");
////                params.put("charset", "utf-8");
//                params.put("Content-Type","application/x-www-form-urlencoded");
//
//                return params;
//            }
//        };
//
//        Log.d(TAG, sr.toString());

//        requestQueue.add(sr);
    }

    public interface PostCommentResponseListener {
        public void requestStarted();
        public void requestCompleted();
        public void requestEndedWithError(VolleyError error);
    }

}
