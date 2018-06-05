package com.example.andriod_pan.retrofitrxdome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.andriod_pan.retrofitrxdome.net.MyService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .baseUrl(MyService.BASE_URL)
                .build();
        MyService service = retrofit.create(MyService.class);
         HashMap map = new HashMap();
        service.home(map)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Log.e("pan",responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }
}
