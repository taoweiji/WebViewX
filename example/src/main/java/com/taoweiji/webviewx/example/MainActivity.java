package com.taoweiji.webviewx.example;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taoweiji.navigation.ViewUtils;
import com.taoweiji.webviewx.example.program.CopyMiniProgram;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView list = new ListView(this);
        setContentView(list);

        ListAdapter adapter = new ListAdapter();
        adapter.add("异步回调API", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, WebViewXBridgeActivity.class));
            }
        });
        adapter.add("X5", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, X5WebViewXActivity.class));
            }
        });

        adapter.add("网页离线运行：2048", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, OfflineWebViewActivity.class)
                        .putExtra("url", "http://2048.com/index.html"));
            }
        });
        adapter.add("网页离线运行：flappy-bird", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, OfflineWebViewActivity.class)
                        .putExtra("url", "http://flappy-bird.com/index.html"));
            }
        });
        adapter.add("网页离线运行：俄罗斯方块", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, OfflineWebViewActivity.class)
                        .putExtra("url", "http://tetris.com/index.html"));
            }
        });

        adapter.add("模拟小程序架构", new Runnable() {
            @Override
            public void run() {
                CopyMiniProgram.start(MainActivity.this
                        , "file:///android_asset/taoweiji.com/"
                        , "http://taoweiji.com/"
                        , "http://taoweiji.com/index.html?userId=1");
            }
        });
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> adapter.tasks.get(position).run());

        long start = System.currentTimeMillis();
        WebView webView = new WebView(this);
        Log.e("Main", String.valueOf((System.currentTimeMillis() - start)));
    }

    static class ListAdapter extends BaseAdapter {
        List<String> items = new ArrayList<>();
        List<Runnable> tasks = new ArrayList<>();

        void add(String title, Runnable runnable) {
            items.add(title);
            tasks.add(runnable);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String item = items.get(position);
            TextView textView = (TextView) convertView;
            if (convertView == null) {
                textView = new TextView(parent.getContext());
                int dp = ViewUtils.dp2px(parent.getContext(), 15);
                textView.setPadding(dp, dp, dp, dp);
                textView.setTextSize(16);
            }
            textView.setText(item);
            return textView;
        }
    }
}
