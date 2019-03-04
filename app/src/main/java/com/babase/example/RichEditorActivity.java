package com.babase.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.babase.lib.widget.BaRichEditor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class RichEditorActivity extends AppCompatActivity {

    private BaRichEditor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_editor);
        mEditor = findViewById(R.id.editor);
//        mEditor.setEditorHeight(150);
        mEditor.setEditorFontSize(22);
//        mEditor.setEditorFontColor(Color.RED);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        //mEditor.setInputEnabled(false);

        findViewById(R.id.action_undo).setOnClickListener(v -> mEditor.undo());
        findViewById(R.id.action_redo).setOnClickListener(v -> mEditor.redo());
        findViewById(R.id.action_bold).setOnClickListener(v -> mEditor.setBold());
        findViewById(R.id.action_italic).setOnClickListener(v -> mEditor.setItalic());
        findViewById(R.id.action_subscript).setOnClickListener(v -> mEditor.setSubscript());
        findViewById(R.id.action_superscript).setOnClickListener(v -> mEditor.setSuperscript());
        findViewById(R.id.action_strikethrough).setOnClickListener(v -> mEditor.setStrikeThrough());
        findViewById(R.id.action_underline).setOnClickListener(v -> mEditor.setUnderline());
        findViewById(R.id.action_heading1).setOnClickListener(v -> mEditor.setHeading(1));
        findViewById(R.id.action_heading2).setOnClickListener(v -> mEditor.setHeading(2));
        findViewById(R.id.action_heading3).setOnClickListener(v -> mEditor.setHeading(3));
        findViewById(R.id.action_heading4).setOnClickListener(v -> mEditor.setHeading(4));
        findViewById(R.id.action_heading5).setOnClickListener(v -> mEditor.setHeading(5));
        findViewById(R.id.action_heading6).setOnClickListener(v -> mEditor.setHeading(6));
        findViewById(R.id.action_indent).setOnClickListener(v -> mEditor.setIndent());
        findViewById(R.id.action_outdent).setOnClickListener(v -> mEditor.setOutdent());
        findViewById(R.id.action_align_left).setOnClickListener(v -> mEditor.setAlignLeft());
        findViewById(R.id.action_align_center).setOnClickListener(v -> mEditor.setAlignCenter());
        findViewById(R.id.action_align_right).setOnClickListener(v -> mEditor.setAlignRight());
        findViewById(R.id.action_blockquote).setOnClickListener(v -> mEditor.setBlockquote());
        findViewById(R.id.action_insert_bullets).setOnClickListener(v -> mEditor.setBullets());
        findViewById(R.id.action_insert_numbers).setOnClickListener(v -> mEditor.setNumbers());
        findViewById(R.id.action_insert_image).setOnClickListener(v -> mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG", "dachshund"));
        findViewById(R.id.action_insert_link).setOnClickListener(v -> mEditor.insertLink("https://github.com/wasabeef", "wasabeef"));
        findViewById(R.id.action_insert_checkbox).setOnClickListener(v -> mEditor.insertTodo());
        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        new Thread(() -> {
            try {
                Document document = Jsoup.connect("https://wapbaike.baidu.com/item/%e4%b8%8a%e6%b5%b7%e8%bf%aa%e5%a3%ab%e5%b0%bc%e4%b9%90%e5%9b%ad/3246958?ms=1&rid=10660413914438046688").get();
                Element element = document.body();

                for (Element e : element.getAllElements()) {
                    if (e.nodeName().contains("script") || e.nodeName().contains("style")) {
                        e.remove();
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String content = element.toString();
                        mEditor.setHtml(content);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        //获取并显示Html
//        mPreview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String result = mEditor.getHtml();
//                Logger.d("html---->" + result);
//
//                String head = "<head>" +
//                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
//                        "<style>html{padding:15px;} body{word-wrap:break-word;font-size:13px;padding:0px;margin:0px} p{padding:0px;margin:0px;font-size:13px;color:#222222;line-height:1.3;} img{padding:0px,margin:0px;max-width:100%; width:auto; height:auto;}</style>" +
//                        "</head>";
//                String data = "<html>" + head + "<body>" + result + "</body></html>";
//
//                baWebView.loadData(data, "text/html;charset=utf-8","utf-8");
//            }
//        });
    }
}