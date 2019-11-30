package com.eatify.sale.eatifysale;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eatify.sale.eatifysale.activitiyfiles.EntryScreen;
import com.eatify.sale.eatifysale.activitiyfiles.New_payment;
import com.eatify.sale.eatifysale.activitiyfiles.ScanQrPage;
import com.eatify.sale.eatifysale.activitiyfiles.Workshop_Register;
import com.eatify.sale.eatifysale.globalfiles.AbsRuntimePermission;
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

public class Scan extends AbsRuntimePermission {
    Button scan_qr;
    private static final int REQUEST_PERMISSION = 10;
    ProgressDialog progressDoalog;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scan_qr = findViewById(R.id.scan);

        requestAppPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,}, R.string.msg, REQUEST_PERMISSION);

       editText=findViewById(R.id.id_box);
        Button submit=findViewById(R.id.button_submit);

        if (editText.isInTouchMode()){
            editText.setCursorVisible(true);
        }else
            editText.setCursorVisible(false);

        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDoalog = new ProgressDialog(Scan.this);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Its loading....");
                progressDoalog.show();
                startActivity(new Intent(Scan.this, ScanQrPage.class));
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDoalog = new ProgressDialog(Scan.this);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Its loading....");
                progressDoalog.show();
                String get_id = editText.getText().toString();
                //if (editText.)
                sendmid(GlobalUrl.QRActvity+get_id);
            }
        });

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

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
                    new SweetAlertDialog(Scan.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("User Already Paid..")
                            .setConfirmText("Ok")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            progressDoalog.dismiss();
                            editText.setText("");
                        }
                    }).show();
                } else {
                    new SweetAlertDialog(Scan.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("User Not Paid..")
                            .setConfirmText("Pay")
                            .setCancelText("Cancel")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    progressDoalog.dismiss();
                                    editText.setText("");
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    progressDoalog.dismiss();
                                    sent_request(GlobalUrl.Pay + editText.getText().toString());
                                }
                            }).show();
                }
            } else {
                new SweetAlertDialog(Scan.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("User Not Registered for Workshop..")
                        .setConfirmText("Register")
                        .setCancelText("Cancel")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                progressDoalog.dismiss();
                                editText.setText("");
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                progressDoalog.dismiss();
                                register_request(GlobalUrl.Pay + editText.getText().toString());
                            }
                        }).show();
            }
        } else {
            new SweetAlertDialog(Scan.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("User Not Registered..")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            progressDoalog.dismiss();
                            editText.setText("");
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
            get_pay(GlobalUrl.Check_pay+editText.getText().toString());
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
            get_register(GlobalUrl.Workshop+editText.getText().toString());
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
            new SweetAlertDialog(Scan.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setContentText("Paid Succesfully...")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            editText.setText("");
                        }
                    }).show();
        } else {
            new SweetAlertDialog(Scan.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Payment Failure..")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            editText.setText("");
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
            new SweetAlertDialog(Scan.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setContentText("Registered Succesfully...")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            editText.setText("");
                        }
                    }).show();
        } else {
            new SweetAlertDialog(Scan.this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Registration Failure..")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            editText.setText("");
                        }
                    }).show();
        }
    }

    }
