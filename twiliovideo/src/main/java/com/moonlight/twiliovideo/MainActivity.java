package com.moonlight.twiliovideo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.twilio.common.TwilioAccessManager;
import com.twilio.common.TwilioAccessManagerFactory;
import com.twilio.common.TwilioAccessManagerListener;
import com.twilio.conversations.AudioOutput;
import com.twilio.conversations.AudioTrack;
import com.twilio.conversations.CameraCapturer;
import com.twilio.conversations.CameraCapturerFactory;
import com.twilio.conversations.CapturerErrorListener;
import com.twilio.conversations.CapturerException;
import com.twilio.conversations.Conversation;
import com.twilio.conversations.ConversationCallback;
import com.twilio.conversations.ConversationListener;
import com.twilio.conversations.ConversationsClient;
import com.twilio.conversations.ConversationsClientListener;
import com.twilio.conversations.IncomingInvite;
import com.twilio.conversations.LocalMedia;
import com.twilio.conversations.LocalMediaFactory;
import com.twilio.conversations.LocalMediaListener;
import com.twilio.conversations.LocalVideoTrack;
import com.twilio.conversations.LocalVideoTrackFactory;
import com.twilio.conversations.MediaTrack;
import com.twilio.conversations.OutgoingInvite;
import com.twilio.conversations.Participant;
import com.twilio.conversations.ParticipantListener;
import com.twilio.conversations.TwilioConversations;
import com.twilio.conversations.TwilioConversationsException;
import com.twilio.conversations.VideoRendererObserver;
import com.twilio.conversations.VideoTrack;
import com.twilio.conversations.VideoViewRenderer;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String mAccessToken;
    private static final String TAG = MainActivity.class.getName();
    /*
    * Android application UI elements
    */
    private FrameLayout previewFrameLayout;
    private ViewGroup localContainer;
    private ViewGroup participantContainer;
    private FloatingActionButton callActionFab;
    private OkHttpClient client = new OkHttpClient();

    private TwilioAccessManager accessManager;
    private ConversationsClient conversationsClient;
    private CameraCapturer cameraCapturer;

    private Conversation conversation;
    private OutgoingInvite outgoingInvite;
    private Context mContext;

    /*
    * A VideoViewRenderer receives frames from a local or remote video track and renders the frames to a provided view
    */
    private VideoViewRenderer participantVideoRenderer;
    private VideoViewRenderer localVideoRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mContext = this.getApplicationContext();
   /*
    * Check camera and microphone permissions. Needed in Android M.
    */
        if (!checkPermissionForCameraAndMicrophone()) {
            requestPermissionForCameraAndMicrophone();
        }

   /*
    * Load views from resources
    */
        callActionFab = findViewById(R.id.fab);
        previewFrameLayout = findViewById(R.id.previewFrameLayout);
        localContainer = findViewById(R.id.localContainer);
        participantContainer = findViewById(R.id.participantContainer);

        getCapabilityToken();
    }

    private void getCapabilityToken() {
        if(1+2==3) {
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzMyZTg4YzY2OGY3ZDQ5YmY2M2UzZjYwNmM2NDNjNzVlLTE1MjM4Mzk5ODYiLCJpc3MiOiJTSzMyZTg4YzY2OGY3ZDQ5YmY2M2UzZjYwNmM2NDNjNzVlIiwic3ViIjoiQUM3MDgxZjI3YTQ2MWIzN2I1ODUwZWFjMWZlNDRlNjI0MCIsImV4cCI6MTUyMzg0MzU4NiwiZ3JhbnRzIjp7ImlkZW50aXR5IjoibW9vbmxpZ2h0MDQxNjA4NTIiLCJ2aWRlbyI6eyJyb29tIjoibW9vbmxpZ2h0X3Jvb20ifX19.DRkcGVdgennLJyWB0PDwSQJDp0ZaqCQhyRW1bh1DcoE";
            initializeTwilioSdk(token);
            return;
        }
        try {
            run("http://d9b70014.ngrok.io/token.php", new Callback() {
//            run("http://[your-ngrok-url].ngrok.io/token.php", new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String token = response.body().string();
                        JSONObject obj = new JSONObject(token);
                        mAccessToken = obj.getString("token");
                        Log.d(TAG, token);
                        initializeTwilioSdk(mAccessToken);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initializeTwilioSdk(final String accessToken) {
        TwilioConversations.setLogLevel(TwilioConversations.LogLevel.DEBUG);

        if (!TwilioConversations.isInitialized()) {
            TwilioConversations.initialize(this.mContext, new TwilioConversations.InitListener() {
                @Override
                public void onInitialized() {
                    accessManager = TwilioAccessManagerFactory.createAccessManager(accessToken, accessManagerListener());
                    conversationsClient = TwilioConversations.createConversationsClient(accessManager, conversationsClientListener());
                    // Specify the audio output to use for this conversation client
                    conversationsClient.setAudioOutput(AudioOutput.SPEAKERPHONE);

                    // Initialize the camera capturer and start the camera preview
                    cameraCapturer = CameraCapturerFactory.createCameraCapturer(MainActivity.this, CameraCapturer.CameraSource.CAMERA_SOURCE_FRONT_CAMERA, previewFrameLayout, capturerErrorListener());
                    startPreview();
                    // Register to receive incoming invites
                    conversationsClient.listen();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(MainActivity.this,
                            "Failed to initialize the Twilio Conversations SDK",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void startPreview() {
        cameraCapturer.startPreview();
    }

    private void stopPreview() {
        if (cameraCapturer != null && cameraCapturer.isPreviewing()) {
            cameraCapturer.stopPreview();
        }
    }

    private void reset() {
        if (participantVideoRenderer != null) {
            participantVideoRenderer = null;
        }
        localContainer.removeAllViews();
        participantContainer.removeAllViews();

        if (conversation != null) {
            conversation.dispose();
            conversation = null;
        }
        outgoingInvite = null;

        if (conversationsClient != null) {
            conversationsClient.setAudioOutput(AudioOutput.HEADSET);
        }
        setCallAction();
        startPreview();
    }

    private void hangup() {
        if (conversation != null) {
            conversation.disconnect();
        } else if (outgoingInvite != null) {
            outgoingInvite.cancel();
        }
    }

    private void setHangupAction() {
        callActionFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.holo_red_dark)));
        callActionFab.show();
        callActionFab.setOnClickListener(hangupActionFabClickListener());
    }

    private LocalMedia setupLocalMedia() {
        LocalMedia localMedia = LocalMediaFactory.createLocalMedia(localMediaListener());
        LocalVideoTrack localVideoTrack = LocalVideoTrackFactory.createLocalVideoTrack(cameraCapturer);
        localMedia.addLocalVideoTrack(localVideoTrack);
        return localMedia;
    }

    private void setCallAction() {
        callActionFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.holo_green_dark)));
        callActionFab.show();
        callActionFab.setOnClickListener(callActionFabClickListener());
    }

    /*
    * UTILITY FUNCTIONS
    */

    private boolean checkPermissionForCameraAndMicrophone() {
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return (resultCamera == PackageManager.PERMISSION_GRANTED) && (resultMic == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this, "Camera and Microphone permissions needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private Call run(String url, Callback callback) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call response = client.newCall(request);
        response.enqueue(callback);
        return response;
    }

    /*
    * EVENT LISTENERS
    */
    private TwilioAccessManagerListener accessManagerListener() {
        return new TwilioAccessManagerListener() {
            @Override
            public void onAccessManagerTokenExpire(TwilioAccessManager twilioAccessManager) {
            }

            @Override
            public void onTokenUpdated(TwilioAccessManager twilioAccessManager) {
            }

            @Override
            public void onError(TwilioAccessManager twilioAccessManager, String s) {
            }
        };
    }

    private ConversationsClientListener conversationsClientListener() {
        return new ConversationsClientListener() {
            @Override
            public void onStartListeningForInvites(ConversationsClient conversationsClient) {
            }

            @Override
            public void onStopListeningForInvites(ConversationsClient conversationsClient) {
            }

            @Override
            public void onFailedToStartListening(ConversationsClient conversationsClient, TwilioConversationsException e) {
            }

            @Override
            public void onIncomingInvite(ConversationsClient conversationsClient, IncomingInvite incomingInvite) {
                if (conversation == null) {
                    LocalMedia localMedia = setupLocalMedia();
                    incomingInvite.accept(localMedia, new ConversationCallback() {
                        @Override
                        public void onConversation(Conversation conversation, TwilioConversationsException e) {
                            if (e == null) {
                                MainActivity.this.conversation = conversation;
                                conversation.setConversationListener(conversationListener());
                            } else {
                                Log.e(TAG, e.getMessage());
                                hangup();
                                reset();
                            }
                        }
                    });
                    setHangupAction();
                } else {
                    Log.w(TAG, String.format("Conversation in progress. Invite from %s ignored", incomingInvite.getInvitee()));
                }
            }


            @Override
            public void onIncomingInviteCancelled(ConversationsClient conversationsClient, IncomingInvite incomingInvite) {

            }
        };
    }

    private CapturerErrorListener capturerErrorListener() {
        return new CapturerErrorListener() {
            @Override
            public void onError(CapturerException e) {
                Log.e(TAG, "Camera capturer error:" + e.getMessage());
            }
        };
    }

    private View.OnClickListener hangupActionFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hangup();
                setCallAction();
            }
        };
    }

    private ConversationListener conversationListener() {
        return new ConversationListener() {
            @Override
            public void onFailedToConnectParticipant(Conversation conversation, Participant participant, TwilioConversationsException e) {

            }

            @Override
            public void onConversationEnded(Conversation conversation, TwilioConversationsException e) {
                reset();
            }

            @Override
            public void onParticipantConnected(Conversation conversation, Participant participant) {
                Log.d(TAG, "onParticipantConnected: Participant connected");
                participant.setParticipantListener(participantListener());
            }

            @Override
            public void onParticipantDisconnected(Conversation conversation, Participant participant) {
                reset();
            }
        };
    }

    private ParticipantListener participantListener() {

        return new ParticipantListener() {
            @Override
            public void onVideoTrackAdded(Conversation conversation, Participant participant, VideoTrack videoTrack) {
                Log.i(TAG, "onVideoTrackAdded " + participant.getIdentity());

                // Remote participant
                participantVideoRenderer = new VideoViewRenderer(MainActivity.this, participantContainer);
                participantVideoRenderer.setObserver(new VideoRendererObserver() {

                    @Override
                    public void onFirstFrame() {
                        Log.i(TAG, "Participant onFirstFrame");
                    }

                    @Override
                    public void onFrameDimensionsChanged(int width, int height, int i2) {
                        Log.i(TAG, "Participant onFrameDimensionsChanged " + width + " " + height);
                    }

                });
                videoTrack.addRenderer(participantVideoRenderer);
            }

            @Override
            public void onVideoTrackRemoved(Conversation conversation, Participant participant, VideoTrack videoTrack) {
                participantContainer.removeAllViews();

            }

            @Override
            public void onAudioTrackAdded(Conversation conversation, Participant participant, AudioTrack audioTrack) {

            }

            @Override
            public void onAudioTrackRemoved(Conversation conversation, Participant participant, AudioTrack audioTrack) {

            }

            @Override
            public void onTrackEnabled(Conversation conversation, Participant participant, MediaTrack mediaTrack) {

            }

            @Override
            public void onTrackDisabled(Conversation conversation, Participant participant, MediaTrack mediaTrack) {

            }
        };
    }

    private LocalMediaListener localMediaListener() {
        return new LocalMediaListener() {
            @Override
            public void onLocalVideoTrackAdded(LocalMedia localMedia, LocalVideoTrack localVideoTrack) {
                localVideoRenderer = new VideoViewRenderer(MainActivity.this, localContainer);
                localVideoTrack.addRenderer(localVideoRenderer);
            }

            @Override
            public void onLocalVideoTrackRemoved(LocalMedia localMedia, LocalVideoTrack localVideoTrack) {
                localContainer.removeAllViews();
            }

            @Override
            public void onLocalVideoTrackError(LocalMedia localMedia, LocalVideoTrack localVideoTrack, TwilioConversationsException e) {
                Log.e(TAG, e.getMessage());
            }
        };
    }

    private View.OnClickListener callActionFabClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (conversationsClient != null) {
                    stopPreview();

                    Set<String> participants = new HashSet<>();
                    participants.add("YawningQuincyPortland");

                    // Create local media
                    LocalMedia localMedia = setupLocalMedia();

                    outgoingInvite = conversationsClient.sendConversationInvite(participants, localMedia, new ConversationCallback() {
                        @Override
                        public void onConversation(Conversation conversation, TwilioConversationsException e) {
                            if (e == null) {
                                // Participant has accepted invite, we are in active conversation
                                MainActivity.this.conversation = conversation;
                                conversation.setConversationListener(conversationListener());
                                setHangupAction();
                            } else {
                                hangup();
                                reset();
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "invalid participant call");
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
