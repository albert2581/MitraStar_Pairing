package com.example.kerorodoodesk.mtapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import butterknife.OnClick;


public class RemoteActivity extends ActionBarActivity {
    boolean playAndStopChangeBool = true;
    boolean soundBool = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        final ImageButton playAndStop_btn = (ImageButton) findViewById(R.id.play);
        playAndStop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playAndStopChangeBool == true) {
                    playAndStop_btn.setImageResource(R.drawable.pause);
                    playAndStopChangeBool = false ;
                    AuthTest a = AuthTest.getInstance();
                    try {
                        a.send_msg("Remote", "play");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } //if
                else {
                    playAndStop_btn.setImageResource(R.drawable.play);
                    playAndStopChangeBool = true ;
                    AuthTest a = AuthTest.getInstance();
                    try {
                        a.send_msg("Remote", "pause");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        final ImageButton sound_btn = (ImageButton) findViewById(R.id.sound);
        sound_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundBool == true) {
                    sound_btn.setImageResource(R.drawable.mute);
                    soundBool = false ;
                    AuthTest a = AuthTest.getInstance();
                    try {
                        a.send_msg("Remote", "mute");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } //if
                else {
                    sound_btn.setImageResource(R.drawable.sound);
                    soundBool = true ;
                    AuthTest a = AuthTest.getInstance();
                    try {
                        a.send_msg("Remote", "sound");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.setClass(RemoteActivity.this, VideoActivity.class);
            startActivity(intent);
            RemoteActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }




    public void onClickOne(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "one");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.two)
    public void onClicktwo(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "two");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.three)
    public void onClickthree(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "four");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.four)
    public void onClickfour(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "five");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.five)
    public void onClickfive(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "three");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.six)
    public void onClicksix(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "six");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.seven)
    public void onClickseven(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "seven");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.eight)
    public void onClickeight(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "eight");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.nine)
    public void onClicknine(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "nine");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.zero)
    public void onClickzero(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "zero");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.voiceDown)
    public void onClickvoiceDown(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "voiceDown");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.voiceUp)
    public void onClickvoiceUp(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "voiceUp");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.right_button)
    public void onClickright_button(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "right_button");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.left_button)
    public void onClickleft_button(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "left_button");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.up_button)
    public void onClickup_button(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "up_button");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.down_button)
    public void onClickdown_button(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "down_button");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @OnClick(R.id.enter_button)
    public void onClickenter_button(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "OK");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @OnClick(R.id.mute)
//    public void onClickmute(View view) {
//        AuthTest a = AuthTest.getInstance();
//        try {
//            a.send_msg("Remote", "mute");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @OnClick(R.id.muteUp)
//    public void onClickmuteUp(View view) {
//        AuthTest a = AuthTest.getInstance();
//        try {
//            a.send_msg("Remote", "muteUp");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    @OnClick(R.id.power)
    public void onClickpower(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "power");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    @OnClick(R.id.play)
//    public void onClickplay(View view) {
//        AuthTest a = AuthTest.getInstance();
//        try {
//            a.send_msg("Remote", "play");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    @OnClick(R.id.stop)
//    public void onClickstop(View view) {
//        AuthTest a = AuthTest.getInstance();
//        try {
//            a.send_msg("Remote", "stop");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    @OnClick(R.id.forward)
    public void onClickforward(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "forward");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onClickback(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "back");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.fast)
    public void onClickfast(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "fast");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.channelUp)
    public void onClickchannelUp(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "channelUp");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.channelDown)
    public void onClickchannelDown(View view) {
        AuthTest a = AuthTest.getInstance();
        try {
            a.send_msg("Remote", "channelDown");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remote, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
