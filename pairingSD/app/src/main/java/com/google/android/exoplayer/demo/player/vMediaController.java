package com.google.android.exoplayer.demo.player;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;

import com.example.kerorodoodesk.mtapp.AuthTest;
import com.example.kerorodoodesk.mtapp.HTTPServer;
import com.example.kerorodoodesk.mtapp.ListVideoActivity;
import com.example.kerorodoodesk.mtapp.R;
import com.google.android.exoplayer.demo.PlayerActivity;

import java.io.File;


/**
 * Created by Me on 2015/11/18.
 */
public class vMediaController extends MediaController implements MediaController.MediaPlayerControl {

    private boolean mUseFastForward;
    private boolean mFromXml;
    private MediaPlayerControl mPlayer;
    private static final int sDefaultTimeout = 3000;
    private PlayerActivity mContext;
    private Button vExecPause;
    private Button fastTwobutton;
    private Button fastFourbutton;
    private Button backTwobutton;
    private Button backFourbutton;
    private Button fastSixteenbutton;
    private Button fastThirtyTwobutton;
    private Button backSixteenbutton;
    private Button backThirtyTwobutton;
    private ImageButton mPauseButton;
    private CharSequence mPauseDescription;
    private CharSequence mPlayDescription;
    private MediaController classAInstance;
    private View mRoot;
    private boolean toChangeAnotherStream = false;
    private boolean fasterchangeStream = false;

    Thread mThreadForFastForward = null;
    private FastForwordLoop mThreadForFastForwardrunnable = null;


    public vMediaController(PlayerActivity context, boolean useFastForward) {
        super(context, useFastForward);
        //super(context, useFastForward);
        classAInstance = new MediaController(context, useFastForward);
        mContext = context;
        mUseFastForward = useFastForward;

    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
        mPlayer = player;
    }

    @Override
    public void start() {
    }

    @Override
    public void pause() {
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        pos = pos - 5000;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        vExecPause = (Button) view.findViewById(R.id.Start);
        vExecPause.setOnClickListener(vExecPauseOnClickListener);
        fastTwobutton = (Button) view.findViewById(R.id.two);
        fastTwobutton.setOnClickListener(fastTwobuttonOnClickListener);
        fastFourbutton = (Button) view.findViewById(R.id.four);
        fastFourbutton.setOnClickListener(fastFourbuttonOnClickListener);
        fastSixteenbutton = (Button) view.findViewById(R.id.sixteen);
        fastSixteenbutton.setOnClickListener(fastSixteenbuttonOnClickListener);
        fastThirtyTwobutton = (Button) view.findViewById(R.id.thirtyTwo);
        fastThirtyTwobutton.setOnClickListener(fastThirtyTwobuttonOnClickListener);
        backTwobutton = (Button) view.findViewById(R.id.backTwo);
        backTwobutton.setOnClickListener(backTwobuttonOnClickListener);
        backFourbutton = (Button) view.findViewById(R.id.backFour);
        backFourbutton.setOnClickListener(backFourbuttonOnClickListener);
        backSixteenbutton = (Button) view.findViewById(R.id.backSixteen);
        backSixteenbutton.setOnClickListener(backSixteenbuttonOnClickListener);
        backThirtyTwobutton = (Button) view.findViewById(R.id.backThirtyTwo);
        backThirtyTwobutton.setOnClickListener(backThirtyTwobuttonOnClickListener);

    }

    public Button.OnClickListener vExecPauseOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!stopFastForward()) {  // if mode fast forward to stop it
                if (mPlayer.isPlaying())  // if mode is play to stop it
                    mPlayer.pause();
                else                      // if mode is pause to start it
                    mPlayer.start();
            }
        }
    };

    public class FastForwordLoop implements Runnable {
        private MediaPlayerControl mPlayer;
        private int multipleSpeed = 1;
        private boolean isVideoEnd = false;
        private int perForwardTime = 33;

        public FastForwordLoop(MediaPlayerControl mPlayer, int multipleSpeed) { // set player and multiple Speed
            this.mPlayer = mPlayer;
            this.multipleSpeed = multipleSpeed;
        }

        public void stopView() {       //  before stop the thread, let run's while leave
            this.isVideoEnd = true;
            this.multipleSpeed = 1;    // initial the multiple speed back to 1
        }

        public int howMultipleSpeed() {  // let the vExecFastForward class know current thread's multiple speed
            return multipleSpeed;
        }

        public int updateCurrentPosition() {
            if (mPlayer != null)
                return mPlayer.getCurrentPosition();
            else
                return 0;
        }

        public void run() {
            int pos = mPlayer.getCurrentPosition();
            while (!isVideoEnd) {
                if (multipleSpeed == 2 || multipleSpeed == 4 || multipleSpeed == -2 || multipleSpeed == -4)
                    perForwardTime = 66;
                pos += perForwardTime * multipleSpeed; // milliseconds
                if (pos >= mPlayer.getDuration())  // if current position bigger or equal than video length, set position equal to video length
                    pos = mPlayer.getDuration();
                else if (pos < 0) {
                    pos = 0;
                }

                mPlayer.seekTo(pos);
                updateCurrentPosition();

                try {
                    Thread.sleep(perForwardTime);  // how long should video forward one second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!mPlayer.isPlaying() || pos == mPlayer.getDuration() || pos == 0) // if current position is equal to video end or zero or press stop,stop fastforward
                    isVideoEnd = true;

            }

            mThreadForFastForward.interrupt();      // stop the thread
            mThreadForFastForward = null;  // initial
            mThreadForFastForwardrunnable = null; // initial
        }
    }

    public Button.OnClickListener fastTwobuttonOnClickListener = new Button.OnClickListener() {
        final private int multipleSpeed = 2;

        @Override
        public void onClick(View v) {

            if (toChangeAnotherStream == true) {
                HTTPServer.getInstance().stopServer();
                mContext.releasePlayer();
                String filename = ListVideoActivity.videoName + ".ts";
                AuthTest a = AuthTest.getInstance();
                try {
                    Thread.sleep(5000);
                    HTTPServer.getInstance().openServer();
                    mContext.setURI(Uri.parse(vPlaySegment(ListVideoActivity.videoName+".mpd")));
                    a.send_msg("Stream", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toChangeAnotherStream = false;
                mContext.preparePlayer();
                vExecFastForward(multipleSpeed);
                vExecFastForward(2);
            }
            else
                vExecFastForward(multipleSpeed);

        }
    };


    public Button.OnClickListener fastFourbuttonOnClickListener = new Button.OnClickListener() {
        final private int multipleSpeed = 4;

        @Override
        public void onClick(View v) {

            if (toChangeAnotherStream == true) {
                HTTPServer.getInstance().stopServer();
                mContext.releasePlayer();
                String filename = ListVideoActivity.videoName + ".ts";
                AuthTest a = AuthTest.getInstance();
                try {
                    Thread.sleep(5000);
                    HTTPServer.getInstance().openServer();
                    mContext.setURI(Uri.parse(vPlaySegment(ListVideoActivity.videoName+".mpd")));
                    a.send_msg("Stream", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toChangeAnotherStream = false;
                mContext.preparePlayer();
                vExecFastForward(multipleSpeed);
                vExecFastForward(4);
            }
            else
                vExecFastForward(multipleSpeed);

        }
    };

    public Button.OnClickListener fastSixteenbuttonOnClickListener = new Button.OnClickListener() {
        final private int multipleSpeed = 16;
        //send playback string (change all I mp4)

        @Override
        public void onClick(View v) {
            if (fasterchangeStream == false) {
                HTTPServer.getInstance().stopServer();
                mContext.releasePlayer();
                String filename = ListVideoActivity.videoName + "_o.ts";
                AuthTest a = AuthTest.getInstance();
                try {
                    Thread.sleep(5000);
                    HTTPServer.getInstance().openServer();
                    mContext.setURI(Uri.parse(vPlaySegment(ListVideoActivity.videoName + "_o.mpd")));
                    //mContext.startService(new Intent(mContext,HTTPService.class));
                    a.send_msg("PlayBackCtrl", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toChangeAnotherStream = true;
                fasterchangeStream = true;
//            Intent intent = mContext.getIntent();
//            intent.setData(Uri.parse(vPlaySegment(ListVideoActivity.videoName + "_o.mpd")));
//            mContext.overridePendingTransition(0, 0);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            mContext.finish();
//            mContext.overridePendingTransition(0, 0);
//            mContext.startActivity(intent);

                //mContext.startService(new Intent(mContext,HTTPService.class));
                mContext.preparePlayer();
                vExecFastForward(multipleSpeed);
                vExecFastForward(16);
            } //if
            else {
                vExecFastForward(multipleSpeed);
            }
        }

    };

    public Button.OnClickListener fastThirtyTwobuttonOnClickListener = new Button.OnClickListener() {
        final private int multipleSpeed = 32;
        //send playback string (change all I mp4)

        @Override
        public void onClick(View v) {
            if (fasterchangeStream==false) {
                HTTPServer.getInstance().stopServer();
                mContext.releasePlayer();
                String filename = ListVideoActivity.videoName + "_o.ts";
                AuthTest a = AuthTest.getInstance();
                try {
                    Thread.sleep(5000);
                    HTTPServer.getInstance().openServer();
                    mContext.setURI(Uri.parse(vPlaySegment(ListVideoActivity.videoName + "_o.mpd")));
                    a.send_msg("PlayBackCtrl", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toChangeAnotherStream = true;
                fasterchangeStream = true;
                mContext.preparePlayer();
                vExecFastForward(multipleSpeed);
                vExecFastForward(32);
            } // if
            else
                vExecFastForward(multipleSpeed);
        }
    };

    public Button.OnClickListener backTwobuttonOnClickListener = new Button.OnClickListener() {
        final private int multipleSpeed = 2;

        @Override
        public void onClick(View v) {
            if (toChangeAnotherStream == true) {
                HTTPServer.getInstance().stopServer();
                mContext.releasePlayer();
                String filename = ListVideoActivity.videoName + ".ts";
                AuthTest a = AuthTest.getInstance();
                try {
                    Thread.sleep(5000);
                    HTTPServer.getInstance().openServer();
                    mContext.setURI(Uri.parse(vPlaySegment(ListVideoActivity.videoName+".mpd")));
                    a.send_msg("Stream", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toChangeAnotherStream = false;
                mContext.preparePlayer();
                vExecFastForward(multipleSpeed);
                vExecFastForward(2);
            }
            else
                vExecFastForward(multipleSpeed);
        }
    };

    public Button.OnClickListener backFourbuttonOnClickListener = new Button.OnClickListener() {
        final private int multipleSpeed = 4;

        @Override
        public void onClick(View v) {
            if (toChangeAnotherStream == true) {
                HTTPServer.getInstance().stopServer();
                mContext.releasePlayer();
                String filename = ListVideoActivity.videoName + ".ts";
                AuthTest a = AuthTest.getInstance();
                try {
                    Thread.sleep(5000);
                    HTTPServer.getInstance().openServer();
                    mContext.setURI(Uri.parse(vPlaySegment(ListVideoActivity.videoName+".mpd")));
                    a.send_msg("Stream", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toChangeAnotherStream = false;
                mContext.preparePlayer();
                vExecFastForward(multipleSpeed);
                vExecFastForward(4);
            }
            else
                vExecFastForward(multipleSpeed);
        }
    };

    public Button.OnClickListener backSixteenbuttonOnClickListener = new Button.OnClickListener() {
        final private int multipleSpeed = 16;
        //send playback string (change all I mp4)

        @Override
        public void onClick(View v) {
            if (fasterchangeStream == false) {
                HTTPServer.getInstance().stopServer();
                mContext.releasePlayer();
                String filename = ListVideoActivity.videoName + "_o.ts";
                AuthTest a = AuthTest.getInstance();
                try {
                    Thread.sleep(5000);
                    HTTPServer.getInstance().openServer();
                    mContext.setURI(Uri.parse(vPlaySegment(ListVideoActivity.videoName + "_o.mpd")));
                    a.send_msg("PlayBackCtrl", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toChangeAnotherStream = true;
                mContext.preparePlayer();
                vExecFastForward(multipleSpeed);
                vExecFastForward(16);
            }//if
            else
                vExecFastForward(multipleSpeed);
        } // onClick
    };

    public Button.OnClickListener backThirtyTwobuttonOnClickListener = new Button.OnClickListener() {
        final private int multipleSpeed = 32;
        //send playback string (change all I mp4)

        @Override
        public void onClick(View v) {
            if (fasterchangeStream == false) {
                HTTPServer.getInstance().stopServer();
                mContext.releasePlayer();
                String filename = ListVideoActivity.videoName + "_o.ts";
                AuthTest a = AuthTest.getInstance();
                try {
                    Thread.sleep(5000);
                    HTTPServer.getInstance().openServer();
                    mContext.setURI(Uri.parse(vPlaySegment(ListVideoActivity.videoName + "_o.mpd")));
                    a.send_msg("PlayBackCtrl", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toChangeAnotherStream = true;
                mContext.preparePlayer();
                vExecFastForward(multipleSpeed);
                vExecFastForward(32);
            }
            else
                vExecFastForward(multipleSpeed);
        }
    };

    public void vExecFastForward(int multipleSpeed) {   // do fast forward and input is what speed you want
        mPlayer.start();
        if (mThreadForFastForwardrunnable == null) {  // if thread is null and new the thread than start
            setFastForwardSpeed(multipleSpeed);
        } else if (mThreadForFastForwardrunnable.howMultipleSpeed() != multipleSpeed) {  // compare is thread want to new is equal to current thread
            stopFastForward();
            while (mThreadForFastForwardrunnable != null) ;  // confirm that the old thread is stop
            setFastForwardSpeed(multipleSpeed);
        }
    }

    public void vExecRewind(int multipleSpeed) {   // do back forward and input is what speed you want
        multipleSpeed = multipleSpeed * -1;
        mPlayer.start();
        if (mThreadForFastForwardrunnable == null) {  // if thread is null and new the thread than start
            setFastForwardSpeed(multipleSpeed);
        } else if (mThreadForFastForwardrunnable.howMultipleSpeed() != multipleSpeed) {  // compare is thread want to new is equal to current thread
            stopFastForward();
            while (mThreadForFastForwardrunnable != null) ;  // confirm that the old thread is stop
            setFastForwardSpeed(multipleSpeed);
        }

    }

    public void setFastForwardSpeed(int multipleSpeed) {
        mThreadForFastForwardrunnable = new FastForwordLoop(mPlayer, multipleSpeed);
        mThreadForFastForward = new Thread(mThreadForFastForwardrunnable);
        mThreadForFastForward.start();
    }

    public boolean stopFastForward() {  // to stop the thread
        if (mThreadForFastForward != null) {  // avoid to stop a thread which is not exist
            mThreadForFastForwardrunnable.stopView();
            return true;
        }

        return false;
    }


    private static String vPlaySegment(String fileName) {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/mtapp");
        return dir + "/" + fileName;
    }
}