package com.example.studylog;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactApiService {
    private static ContactApiService instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    // Mock API endpoint - replace with real endpoint
    private static final String CONTACT_API_URL = "https://jsonplaceholder.typicode.com/posts";

    private ContactApiService(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ContactApiService getInstance(Context context) {
        if (instance == null) {
            instance = new ContactApiService(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public interface ContactSubmissionListener {
        void onSuccess(String response);
        void onError(String error);
    }

    public void submitContactForm(ContactModel contact, ContactSubmissionListener listener) {
        JSONObject contactData = new JSONObject();
        try {
            contactData.put("name", contact.getName());
            contactData.put("email", contact.getEmail());
            contactData.put("subject", contact.getSubject());
            contactData.put("message", contact.getMessage());
            contactData.put("title", "Contact Form: " + contact.getSubject());
            contactData.put("body", contact.getMessage());
            contactData.put("userId", 1);
        } catch (JSONException e) {
            listener.onError("Error formatting request: " + e.getMessage());
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.POST,
            CONTACT_API_URL,
            contactData,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    listener.onSuccess("Message sent successfully!");
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMsg = "Failed to send message";
                    if (error.getMessage() != null) {
                        errorMsg += ": " + error.getMessage();
                    }
                    listener.onError(errorMsg);
                }
            }
        );

        getRequestQueue().add(request);
    }
}
