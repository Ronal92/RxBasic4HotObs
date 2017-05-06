package com.jinwoo.android.rxbasic4hotobs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    ListView listView1, listView2;

    List<String> data1 = new ArrayList<>();
    List<String> data2 = new ArrayList<>();

    ArrayAdapter<String> adapter1;
    ArrayAdapter<String > adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView1 = (ListView)findViewById(R.id.listView1);
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data1);
        listView1.setAdapter(adapter1);

        listView2 = (ListView)findViewById(R.id.listView2);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2);
        listView2.setAdapter(adapter2);

        Observable<String> hotObserverable = Observable.create( emitter -> {
            for(int i = 0; i< 10; i++){
                emitter.onNext("Item = " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            emitter.onComplete();
        });


        // 위에 작성한 Observable(발행자)에서 바로 발행을 시작한다.
        hotObserverable.subscribeOn(Schedulers.io()).publish();

        hotObserverable
                .subscribe(
                        result -> {
                            Log.w("DATA1", result);
                        }
                );

        // 다시 5초후에 두번재 구독자를 등록
        hotObserverable
                .subscribe(
                        result -> {
                            Log.w("DATA2", result);
                        }
                );
    }
}
