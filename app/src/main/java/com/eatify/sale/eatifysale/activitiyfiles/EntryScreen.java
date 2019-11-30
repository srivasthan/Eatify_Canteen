package com.eatify.sale.eatifysale.activitiyfiles;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.ScientificNumberFormatter;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eatify.sale.eatifysale.HomeScreen;
import com.eatify.sale.eatifysale.R;
import com.eatify.sale.eatifysale.Scan;
import com.eatify.sale.eatifysale.globalfiles.GlobalUrl;
import com.eatify.sale.eatifysale.globalfiles.SessionManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EntryScreen extends AppCompatActivity {
    Button submit;
    TextView id;
    String qr_id;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_screnn);
        submit = findViewById(R.id.button_submit);
        id = findViewById(R.id.id_box);

        qr_id = getIntent().getStringExtra("id");
        id.setText(qr_id);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDoalog = new ProgressDialog(EntryScreen.this);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Please Wait....");
                progressDoalog.show();
                String get_id = id.getText().toString();
                sendmid(GlobalUrl.QRActvity+get_id);
            }
        });
    }

    private void sendmid(String u) {
        try {
            URL mu = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) mu.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            // Toast.makeText(details.this,"Request Sent",Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getJSON(u);
    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONObject j = new JSONObject(json);

        if (j.getString("is_user").equalsIgnoreCase("true")) {
            if (j.getString("is_workshop").equalsIgnoreCase("true")) {
                if (j.getString("is_paid").equalsIgnoreCase("true")) {
                    new SweetAlertDialog(EntryScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("User Already Paid..")
                            .setConfirmText("Ok")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    progressDoalog.dismiss();
                                    startActivity(new Intent(EntryScreen.this,Scan.class));
                                    finish();
                                }
                            }).show();
                } else {
                    new SweetAlertDialog(EntryScreen.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("User Not Paid..")
                            .setConfirmText("Pay")
                            .setCancelText("Cancel")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    progressDoalog.dismiss();
                                    startActivity(new Intent(EntryScreen.this,Scan.class));
                                    finish();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    progressDoalog.dismiss();
                                    sent_request(GlobalUrl.Pay + id.getText().toString());
                                }
                            }).show();
                }
            } else {
                new SweetAlertDialog(EntryScreen.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("User Not Registered for Workshop..")
                        .setConfirmText("Register")
                        .setCancelText("Cancel")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                progressDoalog.dismiss();
                                startActivity(new Intent(EntryScreen.this,Scan.class));
                                finish();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                progressDoalog.dismiss();
                                register_request(GlobalUrl.Pay + id.getText().toString());
                            }
                        }).show();
            }
        } else {
            new SweetAlertDialog(EntryScreen.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("User Not Registered..")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            progressDoalog.dismiss();
                            startActivity(new Intent(EntryScreen.this,Scan.class));
                            finish();
                        }
                    }).show();
        }
    }

    private void sent_request (String u){
        try {
            URL mu = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) mu.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            get_pay(GlobalUrl.Check_pay+id.getText().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getJSON(u);
    }

    private void register_request (String u){
        try {
            URL mu = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) mu.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            get_register(GlobalUrl.Workshop+id.getText().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getJSON(u);
    }

    private void get_pay(String url) {
        try {
            URL mu = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mu.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            // Toast.makeText(details.this,"Request Sent",Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getJSONB(url);
    }

    private void getJSONB(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String a) {
                super.onPostExecute(a);
                // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadListView(a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadListView(String json) throws JSONException {
        JSONObject j = new JSONObject(json);

        if (j.getString("is_paid").equalsIgnoreCase("true")){
            new SweetAlertDialog(EntryScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setContentText("Paid Succesfully...")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            startActivity(new Intent(EntryScreen.this,Scan.class));
                            finish();
                        }
                    }).show();
        } else {
            new SweetAlertDialog(EntryScreen.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Payment Failure..")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            startActivity(new Intent(EntryScreen.this,Scan.class));
                            finish();
                        }
                    }).show();
        }
    }

    private void get_register(String r) {
        try {
            URL mu = new URL(r);
            HttpURLConnection conn = (HttpURLConnection) mu.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            // Toast.makeText(details.this,"Request Sent",Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getJSONA(r);
    }

    private void getJSONA(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String b) {
                super.onPostExecute(b);
                // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadView(b);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadView(String json) throws JSONException {
        JSONObject j = new JSONObject(json);

        if (j.getString("is_paid").equalsIgnoreCase("true")){
            new SweetAlertDialog(EntryScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setContentText("Registered Succesfully...")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            startActivity(new Intent(EntryScreen.this,Scan.class));
                            finish();
                        }
                    }).show();
        } else {
            new SweetAlertDialog(EntryScreen.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Registration Failure..")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            startActivity(new Intent(EntryScreen.this,Scan.class));
                            finish();
                        }
                    }).show();
        }
    }
        }
