package com.home.onetothree_countrys_currency_convert;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Converter_Page extends AppCompatActivity {

    public Spinner spnChoose;
    public EditText edWriteMoney;
    public Spinner spnConv1;
    public Spinner spnConv2;
    public Spinner spnConv3;
    public Button btnResult;

    public TextView txtChoose1;
    public TextView txtChoose2;
    public TextView txtChoose3;
    public TextView txtUpdate;

    public TextView txtCountry1;
    public TextView txtCountry2;
    public TextView txtCountry3;
    String choose1;
    String conv1;
    String conv2;
    String conv3;

    String ShowChoose,ShowConv1,ShowConv2,ShowConv3;
    Double ifUSDUSD;
    Double toUSD;
    Double convTo1;
    Double convTo2;
    Double convTo3;
    String nowdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conveter_page);
        findView();
    }

    private void catchData() {
        //////////////////////////////////////////////////////////////////////
        String catchData = "https://tw.rter.info/capi.php";
        //  API來源 ： ©RTER.info 2023
        new Thread(()->{
            try {
                URL url = new URL(catchData);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = in.readLine();
                StringBuffer json = new StringBuffer();
                while (line != null) {
                    json.append(line);
                    line = in.readLine();
                }
                String jsonStr = json.toString();//呼叫json資料轉成文字
                //JSONObject jsonAu = new JSONObject(jsonStr).getJSONObject("USDAUD");
                JSONObject jsonToUSD = new JSONObject(jsonStr).getJSONObject(choose1);
                JSONObject jsonConv1 = new JSONObject(jsonStr).getJSONObject(conv1);
                JSONObject jsonConv2 = new JSONObject(jsonStr).getJSONObject(conv2);
                JSONObject jsonConv3 = new JSONObject(jsonStr).getJSONObject(conv3);
                JSONObject jsonDate = new JSONObject(jsonStr).getJSONObject("USDTWD");
                JSONObject jsonUSDUSD = new JSONObject(jsonStr).getJSONObject("USDTWD");
                toUSD = Math.round(jsonToUSD.getDouble("Exrate")*1000.0)/1000.0;
                convTo1 = Math.round(jsonConv1.getDouble("Exrate")*1000.0)/1000.0;
                convTo2 = Math.round(jsonConv2.getDouble("Exrate")*1000.0)/1000.0;
                convTo3 = Math.round(jsonConv3.getDouble("Exrate")*1000.0)/1000.0;
                ifUSDUSD = Math.round(jsonUSDUSD.getDouble("Exrate")*1000.0)/1000.0;
                nowdate = jsonDate.getString("UTC")+"";
                //////////////////////////////////////////////////////////////////////////

                String edMoney = edWriteMoney.getText().toString();
                Double yourMoney = Double.valueOf(edMoney);
                if (yourMoney >0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //txtCountry1.setText(ShowConv1+",參考匯率:"+convTo1);
                            //txtCountry2.setText(ShowConv2+",參考匯率:"+convTo2);
                            //txtCountry3.setText(ShowConv3+",參考匯率:"+convTo3);
                            show_currency(choose1,conv1,txtCountry1,ShowConv1,convTo1);
                            show_currency(choose1,conv2,txtCountry2,ShowConv2,convTo2);
                            show_currency(choose1,conv3,txtCountry3,ShowConv3,convTo3);
                            txtUpdate.setText("匯率更新時間："+nowdate);
                            converting(choose1,conv1,yourMoney,txtChoose1,convTo1);
                            converting(choose1,conv2,yourMoney,txtChoose2,convTo2);
                            converting(choose1,conv3,yourMoney,txtChoose3,convTo3);
                        }
                    });
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Converter_Page.this);
                    builder.setTitle("輸入錯誤");
                    builder.setMessage("數值不得少於0");
                    builder.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                }
                //////////////////////////////////////////////////////////////////////////
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
    void show_currency(String chosing, String target_convName,TextView txtCountry,String ShowConv,Double convTo){
        if (chosing.equals("USDTWD") && target_convName.equals("USDUSD")){
            txtCountry.setText(ShowConv+",參考匯率:"+toUSD);
        }else if (chosing.equals("USDTWD") && target_convName.equals("USDTWD")){
            txtCountry.setText(ShowConv+",參考匯率:"+"1.0");
        }else if (chosing.equals("USDUSD") && target_convName.equals("USDUSD")){
            txtCountry.setText(ShowConv+",參考匯率:"+ifUSDUSD);
        }else if (chosing.equals("USDUSD") && target_convName.equals("USDTWD")) {
            txtCountry.setText(ShowConv + ",參考匯率:"+"1.0");
        }else {
            txtCountry.setText(ShowConv + ",參考匯率:" + convTo);
        }
    }
    void converting(String chosing, String target_convName, Double yourMoney, TextView txtChoose,Double convTo){
        Double Result;
        if (chosing.equals(target_convName)){
            Result = yourMoney;
            txtChoose.setText(Result.toString());
        }
        if (chosing.equals("USDUSD") && !target_convName.equals("USDUSD")){
            Result = Math.round((yourMoney*convTo)*10000.0)/10000.0;
            txtChoose.setText(Result.toString());
        }
        if (!chosing.equals("USDUSD") && target_convName.equals("USDUSD")){
            Result = Math.round((yourMoney/toUSD)*10000.0)/10000.0;
            txtChoose.setText(Result.toString());
        }
        if (!chosing.equals("USDUSD") && !target_convName.equals("USDUSD")){
            Double toUSDfirst = yourMoney/toUSD;
            Result = Math.round((toUSDfirst*convTo)*10000.0)/10000.0;
            txtChoose.setText(Result.toString());
        }
    }

    private void findView() {
        spnChoose = findViewById(R.id.spnChoose);
        edWriteMoney = findViewById(R.id.edWriteMoney);
        spnConv1 = findViewById(R.id.spnConv1);
        spnConv2 = findViewById(R.id.spnConv2);
        spnConv3 = findViewById(R.id.spnConv3);
        btnResult = findViewById(R.id.btnResult);
        txtChoose1 = findViewById(R.id.txtChoose1);
        txtChoose2 = findViewById(R.id.txtChoose2);
        txtChoose3 = findViewById(R.id.txtChoose3);
        txtUpdate = findViewById(R.id.txtUpdate);

        txtCountry1 = findViewById(R.id.txtCountry1);
        txtCountry2 = findViewById(R.id.txtCountry2);
        txtCountry3 = findViewById(R.id.txtCountry3);

        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,R.array.choose_countrys, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChoose.setAdapter(adapter);
        spnConv1.setAdapter(adapter);
        spnConv2.setAdapter(adapter);
        spnConv3.setAdapter(adapter);

        spnChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();
                ShowChoose = result;//choose1
                choose1 = itemSelectedView(ShowChoose);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ShowChoose = "TWD(台幣)";
                choose1 = itemSelectedView(ShowChoose);
            }
        });

        spnConv1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result1 = parent.getItemAtPosition(position).toString();
                ShowConv1 = result1;
                conv1 = itemSelectedView(ShowConv1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ShowConv1 = "TWD(台幣)";//conv1
                conv1 = itemSelectedView(ShowConv1);
            }
        });

        spnConv2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result2 = parent.getItemAtPosition(position).toString();
                ShowConv2 = result2;
                conv2 = itemSelectedView(ShowConv2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ShowConv2 = "TWD(台幣)";//conv2
                conv2 = itemSelectedView(ShowConv2);
            }
        });

        spnConv3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result3 = parent.getItemAtPosition(position).toString();
                ShowConv3 = result3;//conv3
                conv3 = itemSelectedView(ShowConv3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ShowConv3 = "TWD(台幣)";
                conv3 = itemSelectedView(ShowConv3);
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edWriteMoney.getText().toString().equals("")){
                    if(!edWriteMoney.getText().toString().equals("0"))
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                catchData();
                            }
                        });
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Converter_Page.this);
                    builder.setTitle("請輸入數字");
                    builder.setMessage("請先輸入數字");
                    builder.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        });
    }
    String itemSelectedView(String choosingItem){
        String toView;
        if (choosingItem.equals("USD(美金)")){
            toView = "USDUSD";
        }else {
            toView = "USD"+choosingItem.substring(0,3);
        }
        return toView;
    }


}