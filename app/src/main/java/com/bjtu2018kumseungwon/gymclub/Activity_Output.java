package com.bjtu2018kumseungwon.gymclub;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Activity_Output extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        //initialize variables
        TextView usernameText = findViewById(R.id.nameText);
        TextView passwordText = findViewById(R.id.passwordText);

        TextView welcomeMessage = findViewById(R.id.welcomeMessage);
        Button managementButton = findViewById(R.id.manageButton);


        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userPassword = intent.getStringExtra("userPassword");
        String msg = "Welcome" +" "+ userName;

        usernameText.setText(userName);
        passwordText.setText(userPassword);
        welcomeMessage.setText(msg);

        //admin 계정이 아니면 버튼 안보이게 함
        if (!userName.equals("admin")) {
            //managementButton.setEnabled(false);
            managementButton.setVisibility(View.GONE);
        }

        //MANAGE버튼이 눌리면 여기로 옴
        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask().execute();
            }
        });
    }

    //모든회원에 대한 정보를 가져오기 위한 쓰레드
    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "http://10.0.2.2:8080/Gym/List_Gym.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();

                //한줄씩 읽어서 stringBuilder에 저장함
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(Activity_Output.this, Activity_Management.class);
            intent.putExtra("userList", result);//파싱한 값을 넘겨줌
            Activity_Output.this.startActivity(intent);//ManagementActivity로 넘어감

        }

    }

}