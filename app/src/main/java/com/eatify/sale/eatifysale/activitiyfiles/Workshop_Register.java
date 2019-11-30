package com.eatify.sale.eatifysale.activitiyfiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eatify.sale.eatifysale.R;
import com.eatify.sale.eatifysale.Scan;
import com.eatify.sale.eatifysale.globalfiles.GlobalUrl;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Workshop_Register extends AppCompatActivity {
    Button check;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop__register);

        check = findViewById(R.id.check_status);
        final String qr_id = getIntent().getStringExtra("id");

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(Workshop_Register.this);
                progressDialog.setMax(100);
                progressDialog.setMessage("Its loading....");
                progressDialog.show();
                progressDialog.dismiss();
                sendmid(GlobalUrl.Workshop + qr_id);
            }
        });
    }

    private void sendmid(String u) {
        try {
            URL mu = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) mu.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
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
        String result = j.getString("status");

        if (result.equalsIgnoreCase("true")) {
            new SweetAlertDialog(Workshop_Register.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setContentText("Registered Succesfully...")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    startActivity(new Intent(Workshop_Register.this, Scan.class));
                    finish();
                }
            }).show();
        } else {
            new SweetAlertDialog(Workshop_Register.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Registeration Unsuccessfull..")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    startActivity(new Intent(Workshop_Register.this,Scan.class));
                    finish();
                }
            }).show();
        }
    }
}
