package com.eye7.ShootyWooty.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.main;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.plus.Plus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AndroidLauncher extends AndroidApplication implements ActionResolver, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, RealTimeMessageReceivedListener,
        RoomStatusUpdateListener, RoomUpdateListener, OnInvitationReceivedListener{

    /*
     * API INTEGRATION SECTION. This section contains the code that integrates
     * the game with the Google Play game services API.
     */

    final static String TAG = "ShootyWooty";
    // Request codes for the UIs that we show with startActivityForResult:
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;
    final static int REQUEST_ACHIEVEMENTS = 10003;
    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;
    private boolean achieve = false;

    // Client used to interact with Google APIs.
    private GoogleApiClient mGoogleApiClient;
    private boolean checkNewLib = true;
    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;
    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // All the bitmaps that are loaded during the course of the game
    Bitmap bitmap;
    Bitmap homeMap;
    Bitmap tutorial;
    Bitmap doublePlayer;
    Bitmap fourPlayer;
    Bitmap tutorialpress;
    Bitmap doublePlayerpress;
    Bitmap fourPlayerpress;

    //All Audio Files Used
    private MediaPlayer bgHome;
    private MediaPlayer drawMusic;
    private MediaPlayer winMusic;
    private MediaPlayer loseMusic;
    private MediaPlayer bgTutorial;
    private static MediaPlayer buttonPress;

    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;
    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    private HashMap<Integer,Boolean> checkDead = new HashMap<>();
    String mRoomId = null;
    private boolean endGame = false;
    private int numPlayersdisc = 0;
    // Are we playing in multiplayer mode?
    boolean mMultiplayer = false;
    int curPage = 1;

    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    String mMyId = null;

    // If non-null, this is the id of the invitation we received via the
    // invitation listener
    String mIncomingInvitationId = null;
    private Participant host = null;// Host for the game
    private View gameView; //Main Game view
    private Map<String, String> moves = new HashMap<>(); //Hash map containing the moves of all players
    private String curMoves = null; //String containing the moves of all players at the end of 15sec
    private Map<String,Integer> imMoves = new HashMap<>();//HashMap of immediate moves of players
    private Boolean validMoves = false; //Indicates if the moves are ready to be read
    private int myIDNum = -1;//myIDNum during the game
    private int numPlayers; // number of players in the game
    private ArrayList<Integer> leftPlayers = new ArrayList<Integer>(); //String of players who left
    private Map<String, Integer> idToNum = new HashMap<>(); //Hash map that maps the id of a player to his id number
    private boolean quitGame = false;
    private boolean toastShow = false;

    private String[] twoPlayerMaps = new String[]{"CorridorOfDeath","SandPit","CongoLine","L"}; //List of 2 player maps
    private String[] fourPlayerMaps = new String[]{"CheckMate4","CorridorOfDeath4","Pachinko4"}; //List of 4 player maps
    private String gameMap = "";

    private AndroidApplicationConfiguration config;
    private main shootyWooty;
    LinearLayout linearLayout = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the Google Api Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        // set up a click listener for everything we care about
        for (int id : CLICKABLES) {
            findViewById(id).setOnClickListener(this);
        }
        setUpScreen();
        bgHome.start();
        shootyWooty = new main(this);
        config = new AndroidApplicationConfiguration();
        gameView = initializeForView(shootyWooty,config);
        linearLayout = (LinearLayout) findViewById(R.id.screen_game);
        linearLayout.addView(gameView,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

    }



    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {

            case R.id.button_single_player_2:
                // play a single-player game
                bgHome.stop();
                tutorial();
                break;
            case R.id.button_sign_in:
                // user wants to sign in
                // Check to see the developer who's running this sample code read the instructions :-)
                // NOTE: this check is here only because this is a sample! Don't include this
                // check in your actual production app.
                if (!BaseGameUtil.verifySampleSetup(this, R.string.app_id)) {
                    Log.w(TAG, "*** Warning: setup problems detected. Sign in may not work!");
                }

                // start the sign-in flow
                Log.d(TAG, "Sign-in button clicked");
                mSignInClicked = true;
                mGoogleApiClient.connect();
                break;
            case R.id.button_accept_popup_invitation:
                // user wants to accept the invitation shown on the invitation popup
                // (the one we got through the OnInvitationReceivedListener).
                acceptInviteToRoom(mIncomingInvitationId);
                mIncomingInvitationId = null;
                break;
            case R.id.button_quick_game:
                // user wants to play against a random opponent right now
                startQuickGame(2);
                break;
            case R.id.button_quick_game1:
                startQuickGame(4);
                break;

        }
    }

    void startQuickGame(int player) {
        // quick-start a game with 1 randomly selected opponent
        Log.d(TAG, "Start quick game");
        checkNewLib = false;
        final int MIN_OPPONENTS = player - 1, MAX_OPPONENTS = player-1;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
                MAX_OPPONENTS, 0);
        numPlayers = player;
        quitGame = false;
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        if(!checkNewLib)
            resetLibGdxVars();

        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        switch (requestCode) {
            case RC_SELECT_PLAYERS:
                // we got the result from the "select players" UI -- ready to create the room
                handleSelectPlayersResult(responseCode, intent);
                break;
            case RC_INVITATION_INBOX:
                // we got the result from the "select invitation" UI (invitation inbox). We're
                // ready to accept the selected invitation:
                handleInvitationInboxResult(responseCode, intent);
                break;
            case RC_WAITING_ROOM:
                // we got the result from the "waiting room" UI.
                if (responseCode == Activity.RESULT_OK) {
                    // ready to start playing
                    Log.d(TAG, "Starting game (waiting room returned OK).");
                    sortParticipants();
                    if (mParticipants.get(0).getParticipantId().equals(mMyId)) {
                        Log.d(TAG, "Choosing Map");
                        chooseMap();
                    }
                    startGame(true);
                } else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player indicated that they want to leave the room
                    leaveRoom();
                } else if (responseCode == Activity.RESULT_CANCELED) {
                    // Dialog was cancelled (user pressed back key, for instance). In our game,
                    // this means leaving the room too. In more elaborate games, this could mean
                    // something else (like minimizing the waiting room UI).
                    leaveRoom();
                }
                break;
            case RC_SIGN_IN:
                Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                        + responseCode + ", intent=" + intent);
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
                if (responseCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                } else {
                    BaseGameUtil.showActivityResultError(this, requestCode, responseCode, R.string.signin_other_error);
                }
                break;
            case REQUEST_ACHIEVEMENTS:
                break;
        }
        super.onActivityResult(requestCode, responseCode, intent);
    }

    // Handle the result of the "Select players UI" we launched when the user clicked the
    // "Invite friends" button. We react by creating a room with those players.
    private void handleSelectPlayersResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** select players UI cancelled, " + response);
            switchToMainScreen();
            return;
        }

        Log.d(TAG, "Select players UI succeeded.");

        // get the invitee list
        final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        Log.d(TAG, "Invitee count: " + invitees.size());

        // get the automatch criteria
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                    minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
        }

        // create the room
        Log.d(TAG, "Creating room...");
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.addPlayersToInvite(invitees);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        if (autoMatchCriteria != null) {
            rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        }
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());
        Log.d(TAG, "Room created, waiting for it to be ready...");
    }

    // Handle the result of the invitation inbox UI, where the player can pick an invitation
    // to accept. We react by accepting the selected invitation, if any.
    private void handleInvitationInboxResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** invitation inbox UI cancelled, " + response);
            switchToMainScreen();
            return;
        }
        Log.d(TAG, "Invitation inbox UI succeeded.");
        Invitation inv = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);
        // accept invitation
        acceptInviteToRoom(inv.getInvitationId());
    }

    // Accept the given invitation.
    void acceptInviteToRoom(String invId) {
        // accept the invitation
        Log.d(TAG, "Accepting invitation: " + invId);
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
        roomConfigBuilder.setInvitationIdToAccept(invId)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());
    }

    // Activity is going to the background. We have to leave the current room.
    @Override
    public void onStop() {
        Log.d(TAG, "**** got onStop");

        // if we're in a room, leave it.
        bgHome.stop();
        if(bgTutorial.isPlaying()){
            bgTutorial.stop();
        }
        leaveRoom();

        // stop trying to keep the screen on
        //       stopKeepingScreenOn();

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
            switchToScreen(R.id.screen_sign_in);
        }
        else {
            switchToScreen(R.id.screen_wait);
        }
        super.onStop();
    }
    // Activity just got to the foreground. We switch to the wait screen because we will now
    // go through the sign-in flow (remember that, yes, every time the Activity comes back to the
    // foreground we go through the sign-in flow -- but if the user is already authenticated,
    // this flow simply succeeds and is imperceptible).
    @Override
    public void onStart() {
        switchToScreen(R.id.screen_wait);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.w(TAG,"GameHelper: client was already connected on onStart()");
            switchToMainScreen();
        } else {
            Log.d(TAG,"Connecting client.");
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    //Handles the press of the back button on the phone
    @Override
    public void onBackPressed(){
        if(mCurScreen!=R.id.screen_main) {
            if(!endGame){
                switchToScreen(R.id.screen_wait);
            }
            leaveRoom();
        }
        else {
            bgHome.stop();
            super.onBackPressed();
        }
    }

    // Leave the room.
    public void leaveRoom() {
        quitGame=true;
        Log.d(TAG, "Leaving room.");
        stopKeepingScreenOn();
        if(!getMultiplayer()){
            resetGameVars();
            switchToMainScreen();
            return;
        }
        if(!toastShow && !endGame) {
            Toast.makeText(this, "You are being redirected.... Please Wait", Toast.LENGTH_LONG).show();
        }
        toastShow = false;
        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);

        }

        //. switchToMainScreen();


    }

    public boolean mapDecided(){
        if(gameMap.equals(""))
            return false;
        return true;
    }

    // Show the waiting room UI to track the progress of other players as they enter the
    // room and get connected.
    void showWaitingRoom(Room room) {
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, MIN_PLAYERS);

        // show waiting room UI
        startActivityForResult(i, RC_WAITING_ROOM);
    }

    // Called when we get an invitation to play a game. We react by showing that to the user.
    @Override
    public void onInvitationReceived(Invitation invitation) {
        // We got an invitation to play a game! So, store it in
        // mIncomingInvitationId
        // and show the popup on the screen.
        mIncomingInvitationId = invitation.getInvitationId();
        ((TextView) findViewById(R.id.incoming_invitation_text)).setText(
                invitation.getInviter().getDisplayName() + " " +
                        getString(R.string.is_inviting_you));
        switchToScreen(mCurScreen); // This will show the invitation popup
    }

    @Override
    public void onInvitationRemoved(String invitationId) {
        if (mIncomingInvitationId.equals(invitationId)) {
            mIncomingInvitationId = null;
            switchToScreen(mCurScreen); // This will hide the invitation popup
        }
    }

    /*
     * CALLBACKS SECTION. This section shows how we implement the several games
     * API callbacks.
     */

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "onConnected() called. Sign in successful!");

        Log.d(TAG, "Sign-in succeeded.");

        // register listener so we are notified if we receive an invitation to play
        // while we are in the game
        Games.Invitations.registerInvitationListener(mGoogleApiClient, this);

        if (connectionHint != null) {
            Log.d(TAG, "onConnected: connection hint provided. Checking for invite.");
            Invitation inv = connectionHint
                    .getParcelable(Multiplayer.EXTRA_INVITATION);
            if (inv != null && inv.getInvitationId() != null) {
                // retrieve and cache the invitation ID
                Log.d(TAG,"onConnected: connection hint has a room invite!");
                acceptInviteToRoom(inv.getInvitationId());
                return;
            }
        }
        switchToMainScreen();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);

        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = BaseGameUtil.resolveConnectionFailure(this, mGoogleApiClient,
                    connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }

        switchToScreen(R.id.screen_sign_in);
    }

    // Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
    // is connected yet).

    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom.");

        // get room ID, participants and my ID:
        mRoomId = room.getRoomId();
        mParticipants = room.getParticipants();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(mGoogleApiClient));
        mMultiplayer = true;
        // print out the list of participants (for debug purposes)
        Log.d(TAG, "Room ID: " + mRoomId);
        Log.d(TAG, "My ID " + mMyId);
        Log.d(TAG, "<< CONNECTED TO ROOM>>");
    }

    // Called when we've successfully left the room (this happens a result of voluntarily leaving
    // via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
    @Override
    public void onLeftRoom(int statusCode, String roomId) {
        // we have left the room; return to main screen.
        Log.d(TAG, "onLeftRoom, code " + statusCode);
        if(!endGame) {
            switchToScreen(R.id.screen_wait);
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        switchToMainScreen();
        resetGameVars();
    }

    void resetLibGdxVars(){
        linearLayout.removeViewAt(0);
        shootyWooty = new main(this);
        gameView = initializeForView(shootyWooty,config);
        linearLayout.addView(gameView, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    // Reset game variables in preparation for a new game.
    void resetGameVars() {
        checkDead = new HashMap<>();
        mMultiplayer = false;
        host = null;
        moves = new HashMap<>();
        curMoves = null;
        validMoves = false;
        myIDNum = -1;
        mParticipants = null;
        mMyId = null;
        imMoves = new HashMap<>();
        if(bitmap!=null) {
            bitmap.recycle();
        }
        checkNewLib = false;
        leftPlayers = new ArrayList<Integer>();
        idToNum = new HashMap<>();
        endGame = false;
        gameMap = "";
        numPlayersdisc = 0;
    }

    // Called when we get disconnected from the room. We return to the main screen.
    @Override
    public void onDisconnectedFromRoom(Room room) {
        if(!endGame) {
            quitGame=true;
            stopKeepingScreenOn();
            switchToScreen(R.id.screen_wait);
            Toast.makeText(this, "Your opponent(s) have left.... Please Wait", Toast.LENGTH_LONG).show();
            toastShow = true;
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            mRoomId = null;
            showGameError();
        }
        Log.w(TAG, "Left from disconnected");
    }

    // Show error message about game being cancelled and return to main screen.
    void showGameError() {
        BaseGameUtil.makeSimpleDialog(this, getString(R.string.game_problem));
        switchToMainScreen();
    }

    // Called when room has been created
    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            showGameError();
            return;
        }

        // show the waiting room UI
        showWaitingRoom(room);
    }

    // Called when room is fully connected.
    @Override
    public void onRoomConnected(int statusCode, Room room) {
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }
        updateRoom(room);
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }

        // show the waiting room UI
        showWaitingRoom(room);
    }

    // We treat most of the room update callbacks in the same way: we update our list of
    // participants and update the display. In a real game we would also have to check if that
    // change requires some action like removing the corresponding player avatar from the screen,
    // etc.

    @Override
    public void onPeerDeclined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onP2PDisconnected(String participant) {
        Log.d(TAG, "In P2P");
        mParticipants.remove(participant);
        sortParticipants();
        if ((mParticipants.size()<=1)&&!endGame){
            Log.d(TAG, "In check indicating everyone LEFT");
            leaveRoom();

        }

    }

    @Override
    public void onP2PConnected(String participant) {

    }

    @Override
    public void onPeerJoined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerLeft(Room room, List<String> peersWhoLeft) {
        updateRoom2(room, peersWhoLeft);
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        updateRoom(room);
    }

    @Override
    public void onRoomConnecting(Room room) {
        updateRoom(room);
    }

    @Override
    public void onPeersConnected(Room room, List<String> peers) {
        updateRoom(room);
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> peers) {
        updateRoom2(room, peers);
    }

    void updateRoom(Room room) {
        Log.d(TAG, "In UPDATE ROOM");
        if (room != null) {
            mParticipants = room.getParticipants();
            sortParticipants();
        }
        if ((mParticipants.size()<=1||room==null)&&!endGame){
            Log.d(TAG, "In check indicating everyone LEFT");
            leaveRoom();

        }
    }

    void updateRoom2(Room room, List<String> peers){
        Log.d(TAG, "In UPDATE ROOM2");

        if(room!=null) {
            for(String peer:peers){
                leftPlayers.add(idToNum.get(peer));
                if(!checkDead.get(idToNum.get(peer))) {
                    numPlayersdisc++;
                    checkDead.put(idToNum.get(peer), true);
                }
            }
//            mParticipants = room.getParticipants();
        }
        if (((numPlayers-numPlayersdisc)<=1||room==null)&&!endGame){
            switchToScreen(R.id.screen_wait);
            Log.d(TAG, "In check indicating everyone LEFT");
            toastShow=true;
            leaveRoom();

        }
        else {
            sortParticipants();
        }

    }

    //Sets that the game has ended
    public void setEndGame(){
        endGame = true;
    }

    //Returns the list of players who have left the game
    public ArrayList<Integer> getLeftPlayers(){
        return leftPlayers;
    }

    public void clearLeftPlayers(){
        leftPlayers = new ArrayList<>();
    }

    //Returns number of Players in the game
    public int getNumPlayers(){
        return numPlayers;
    }
    //Returns the number of dead players
    public int getNumDeadPlayers(){
        int d = 0;
        for(int i=0; i<4; i++){
            if(checkDead.get(i)!=null) {
                if (checkDead.get(i))
                    d++;
            }
        }
        return d;
    }

    //Always sorts the mMultiplayer list of participants
    public void sortParticipants(){
        int i;
        ArrayList<Participant> sorted = new ArrayList<>();
        outer:
        for (Participant p: mParticipants){
            for(i=0; i<sorted.size(); i++){
                if (p.getParticipantId().compareTo(sorted.get(i).getParticipantId())<0){
                    sorted.add(i,p);
                    continue outer;
                }
            }
            sorted.add(p);
        }
        mParticipants = sorted;
        if(idToNum.size()==0){
            i=0;
            for(Participant p: mParticipants){
                idToNum.put(p.getParticipantId(),i);
                Log.d(TAG,"MOVES TO SEND " + p.getParticipantId()+Integer.toString(i));
                i++;
            }
        }
    }

    public boolean getQuitGame(){
        return quitGame;
    }



    // Start the gameplay phase of the game.
    void startGame(boolean multiplayer) {
        mMultiplayer = multiplayer;
        if(!multiplayer){
            gameMap = "CorridorOfDeath";
        }
        for(int i=0; i<4; i++){
            checkDead.put(i,false);
        }//Initialises the list of dead players
        bgHome.stop();
        switchToScreen(R.id.screen_game);


    }

    //Indicates that a valid set of moves can be read
    public boolean getValid(){
        return validMoves;
    }

    //Sets it to false after reading the moves
    public void setValid(boolean v){
        validMoves = v;
    }

    //Sends the current string of moves
    public String getMoves(){
        return curMoves;
    }

    //Returns the idNumber of the player
    public int getMyID(){
        myIDNum = idToNum.get(mMyId);
        return idToNum.get(mMyId);
    }


    public String getActive(){
        return mRoomId;
    }

    //Searches and assigns the host
    public Participant getHost(String s){
        Log.d(TAG, "Getting Host");
        for(Participant p : mParticipants){
            if (p.getParticipantId().equals(s)){
                return p;
            }
        }
        Log.d(TAG, "Could not find Host");
        return null;
    }

    //Chooses the host for the game
    public void chooseMap(){
        Random rnd = new Random(System.nanoTime());
        String s;
        if(mParticipants.size()==4){
            s = fourPlayerMaps[rnd.nextInt(fourPlayerMaps.length)];
        }
        else {
            s = twoPlayerMaps[rnd.nextInt(twoPlayerMaps.length)];
        }
        sendMessageAll("$", s);
        Log.d(TAG, "Map sent "+s + mMyId);

    }

    //Called when the moves are ready to be sent
    public void readyToSend(){
        String movestoSend = "";
        int i=0;
        for(Participant p:mParticipants){
            Log.d(TAG, "4 PLAYER DEBUG" + String.valueOf(checkDead.get(idToNum.get(p.getParticipantId())))+Integer.toString(idToNum.get(p.getParticipantId()))+ p.getParticipantId() );
            if(!checkDead.get(idToNum.get(p.getParticipantId()))) {
                movestoSend += moves.get(p.getParticipantId());
            }
            else {
                movestoSend += "!0B0 0B0 0B0 0B0";
            }
        }
        Log.d(TAG, "MOVES TO SEND" + movestoSend);
        curMoves = movestoSend;
        validMoves = true;
        moves = new HashMap<>();
    }

    //Returns the immediate list of moves of all players
    public Integer[] getImMoves(){
        Integer[] moves = new Integer[mParticipants.size()];
        int i=0;
        for(Participant p :mParticipants){
            if(imMoves.get(p.getParticipantId())!=null) {
                moves[i] = imMoves.get(p.getParticipantId());
            }
            else{
                moves[i]=0;
            }
            i++;
        }
        return moves;

    }

    public String getMapDecided(){
        return gameMap;
    }


    //Method to process incoming messages
    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
        byte[] buf = rtm.getMessageData();
        String s = new String(buf);
        String sender = rtm.getSenderParticipantId();
        if (s.charAt(0)=='$'){
            gameMap = s.substring(1);
        }
        if(s.charAt(0)=='!'){
            moves.put(sender,s);
            Log.d(TAG, "4 PLAYER DEBUG "+ sender + s);
            Log.d(TAG, "4 PLAYER DEBUG "+ Integer.toString(numPlayers-numPlayersdisc));
            if (moves.size()==(numPlayers-numPlayersdisc)){
                readyToSend();
            }
        }
        if(s.charAt(0)=='^'){
            imMoves.put(sender,Integer.valueOf(s.substring(1)));
        }
    }


    public void sendContMessage(String s){
        byte[] bytes = ("^"+s).getBytes();
        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId))
                imMoves.put(mMyId, Integer.valueOf(s));
            if (p.getStatus() == Participant.STATUS_LEFT)
                continue;
            if(mRoomId!=null && !endGame)
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, bytes,
                        mRoomId, p.getParticipantId());
        }

    }

    //Marks the player as dead in the checkDead list
    public void markDead(int i){
        Log.d(TAG, "Player marked dead " + Integer.toString(i));
        if(!checkDead.get(i)) {
            checkDead.put(i, true);
            numPlayersdisc++;
        }
    }
    public HashMap<Integer,Boolean> prevDead(){
        return checkDead;
    }

    //Sends message to all players
    public void sendMessageAll(String x, String s){
        if(!mMultiplayer)
            return;
        if (x.equals("!")){
            moves.put(mMyId,"!"+s);
            if (moves.size()==(numPlayers-numPlayersdisc)){
                readyToSend();
            }
        }
        if(x.equals("$")){
            gameMap = s;
        }
        byte[] bytes = (x+s).getBytes();
        for (Participant p : mParticipants){
            if (p.getParticipantId().equals(mMyId))
                continue;
            if (p.getStatus()==Participant.STATUS_LEFT)
                continue;
            if(mRoomId!=null) {
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, bytes,
                        mRoomId, p.getParticipantId());
            }
            Log.d(TAG, "Message sent "+ p.getParticipantId());
        }

    }

    //Returns if the game is multiplayer
    public boolean getMultiplayer(){
        return mMultiplayer;
    }

    // This array lists everything that's clickable, so we can install click
    // event handlers.
    final static int[] CLICKABLES = {
            R.id.button_accept_popup_invitation,
            R.id.button_quick_game,
            R.id.button_quick_game1,
            R.id.button_sign_in,
            R.id.button_single_player_2
    };
    public void addAchievements(HashMap<String, Integer> myAchievements) throws InterruptedException {
        if(myAchievements.get("Kills")>=1){
            Games.Achievements.unlock(mGoogleApiClient, "CgkI_oGooPsFEAIQAw");//level 3
        }
        if(myAchievements.get("Hits")>=15){
            Games.Achievements.unlock(mGoogleApiClient, "CgkI_oGooPsFEAIQAg");// level 2
        }
        if(myAchievements.get("Water")>=6){
            Games.Achievements.unlock(mGoogleApiClient, "CgkI_oGooPsFEAIQAQ");// level 1
        }
        if(myAchievements.get("Shots Fired")>=12){
            Games.Achievements.unlock(mGoogleApiClient, "CgkI_oGooPsFEAIQBA");// level 4
        }
        if(myAchievements.get("Kills")==3){
            Games.Achievements.unlock(mGoogleApiClient, "CgkI_oGooPsFEAIQBQ");// level 5
        }





    }

    //Called to Display Achievements
    public void displayAchievements(final HashMap<String, Integer> myAchievements){
        try {
            addAchievements(myAchievements);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);


    }

    public void gameDecided(String s ) throws InterruptedException {
        final String check = s;
        final View.OnClickListener listener1= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveRoom();
                if(drawMusic.isPlaying())
                    drawMusic.stop();
                if(winMusic.isPlaying())
                    winMusic.stop();
                if(loseMusic.isPlaying())
                    loseMusic.stop();
            }
        };

        Thread thread = new Thread(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "BEGINNING SCREEN CHANGE");
                        try {
                            InputStream is;
                            if(check!="draw") {
                                is = getAssets().open("EndScreens/"+check+Integer.toString(myIDNum+1)+".png");
                            }
                            else {
                                drawMusic.start();
                                is = getAssets().open("EndScreens/"+check+".png");
                            }
                            Log.d(TAG, "FOUND IMAGE URI" + check);
                            bitmap = BitmapFactory.decodeStream(is);
                            ImageView imageView = (ImageView)findViewById(R.id.EndImage);
                            imageView.setImageBitmap(bitmap);
                            Log.d(TAG, "SET IMAGE");
                            if(check.equals("win")){
                                winMusic.start();
                            }
                            else{
                                loseMusic.start();
                            }
                            switchToScreen(R.id.GameDecided);
                            imageView.setOnClickListener(listener1);
                            keepScreenOn();

                        }
                        catch(Exception e){
                            Log.d(TAG, "Error in game decided");
                        }

                    }
                });
            };
        };
        thread.start();
        thread.join();

    }

    // This array lists all the individual screens our game has.
    final static int[] SCREENS = {
            R.id.screen_game, R.id.screen_main, R.id.screen_sign_in,
            R.id.screen_wait, R.id.GameDecided, R.id.TutorialMainScreen
    };
    int mCurScreen = -1;

    void switchToScreen(int screenId) {
        // make the requested screen visible; hide all others.
        for (int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
            gameView.setVisibility(screenId == R.id.screen_game ? View.VISIBLE : View.GONE);
        }
        mCurScreen = screenId;

        // should we show the invitation popup?
        boolean showInvPopup;
        if (mIncomingInvitationId == null) {
            // no invitation, so no popup
            showInvPopup = false;
        } else if (mMultiplayer) {
            // if in multiplayer, only show invitation on main screen
            showInvPopup = (mCurScreen == R.id.screen_main);
        } else {
            // single-player: show on main screen and gameplay screen
            showInvPopup = (mCurScreen == R.id.screen_main || mCurScreen == R.id.screen_game);
        }
        findViewById(R.id.invitation_popup).setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
    }

    void switchToMainScreen() {
        bgHome.start();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            switchToScreen(R.id.screen_main);
        }
        else {
            switchToScreen(R.id.screen_sign_in);
        }
    }



    /*
     * MISC SECTION. Miscellaneous methods.
     */
    public void setUpScreen(){
        InputStream is = null;
        try {
            is = getAssets().open("HomeScreen/homescreen.png");
            homeMap = BitmapFactory.decodeStream(is);
            ImageView imageView = (ImageView)findViewById(R.id.signin);
            imageView.setImageBitmap(homeMap);
            imageView = (ImageView)(findViewById(R.id.mainhome));
            imageView.setImageBitmap(homeMap);
            imageView = (ImageView)(findViewById(R.id.wait));
            imageView.setImageBitmap(homeMap);
            is = getAssets().open("HomeScreen/tutorial.png");
            tutorial = BitmapFactory.decodeStream(is);
            is = getAssets().open("HomeScreen/tutorialpress.png");
            tutorialpress = BitmapFactory.decodeStream(is);
            is = getAssets().open("HomeScreen/players2.png");
            doublePlayer = BitmapFactory.decodeStream(is);
            is = getAssets().open("HomeScreen/players2press.png");
            doublePlayerpress = BitmapFactory.decodeStream(is);
            is = getAssets().open("HomeScreen/players4.png");
            fourPlayer = BitmapFactory.decodeStream(is);
            is = getAssets().open("HomeScreen/players4press.png");
            fourPlayerpress = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageButton imageButton = (ImageButton) findViewById(R.id.button_single_player_2);
        imageButton.setImageBitmap(tutorial);
        ImageView tutImage = (ImageView) findViewById(R.id.button_single_player_2);
        buttonEffect(tutImage,R.id.button_single_player_2, tutorialpress, tutorial);
        ImageView doubImage = (ImageView) findViewById(R.id.button_quick_game);
        buttonEffect(doubImage,R.id.button_quick_game,doublePlayerpress,doublePlayer);
        ImageView fourImage = (ImageView) findViewById(R.id.button_quick_game1);
        buttonEffect(fourImage,R.id.button_quick_game1,fourPlayerpress,fourPlayer);
        imageButton = (ImageButton) findViewById(R.id.button_quick_game);
        imageButton.setImageBitmap(doublePlayer);
        imageButton = (ImageButton) findViewById(R.id.button_quick_game1);
        imageButton.setImageBitmap(fourPlayer);
        bgHome = MediaPlayer.create(this,R.raw.bghome);
        drawMusic = MediaPlayer.create(this,R.raw.youdraw);
        winMusic = MediaPlayer.create(this,R.raw.youwin);
        loseMusic = MediaPlayer.create(this,R.raw.youlose);
        buttonPress = MediaPlayer.create(this,R.raw.homebuttonpressed);
        bgTutorial = MediaPlayer.create(this,R.raw.bgtutorial);
        bgHome.setLooping(true);
        drawMusic.setLooping(true);
        winMusic.setLooping(true);
        loseMusic.setLooping(true);


    }

    public static void buttonEffect(View button, int ib, Bitmap press, Bitmap release){
        final int i = ib;
        final Bitmap pressed = press;
        final Bitmap released = release;

        button.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        buttonPress.start();
                        ((ImageView)v.findViewById(i)).setImageBitmap(pressed);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        ((ImageView)v.findViewById(i)).setImageBitmap(released);
                        buttonPress.stop();
                        break;
                    }
                }
                return false;
            }
        });
    }
    public void tutorial(){
        bgTutorial.start();
        switchToScreen(R.id.TutorialMainScreen);
        final ImageView imageView, buttonView;
        final Bitmap pageOne,pageThree,pageTwo,next,play;
        try {
            InputStream is = getAssets().open("Tutorial/tutorial1.png");
            pageOne = BitmapFactory.decodeStream(is);
            is = getAssets().open("Tutorial/tutorial2.png");
            pageTwo = BitmapFactory.decodeStream(is);
            is = getAssets().open("Tutorial/tutorial3.png");
            pageThree = BitmapFactory.decodeStream(is);
            is = getAssets().open("Tutorial/next.png");
            next = BitmapFactory.decodeStream(is);
            is = getAssets().open("Tutorial/play.png");
            play = BitmapFactory.decodeStream(is);
            imageView = (ImageView) findViewById(R.id.tutorialScreen);
            imageView.setImageBitmap(pageOne);
            buttonView = (ImageView) findViewById(R.id.navigate);
            buttonView.setImageBitmap(next);

            buttonView.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            if(curPage==1){
                                imageView.setImageBitmap(pageTwo);
                                curPage++;
                            }
                            else if(curPage==2){
                                imageView.setImageBitmap(pageThree);
                                buttonView.setImageBitmap(play);
                                curPage++;
                            }
                            else{
                                curPage = 1;
                                bgTutorial.stop();
                                switchToMainScreen();
                                pageOne.recycle();
                                pageTwo.recycle();
                                pageThree.recycle();
                                next.recycle();
                                play.recycle();



                            }

                            break;
                        }
                    }
                    return false;
                }
            });
        }
        catch(Exception e){

        }


    }
    // Sets the flag to keep this screen on. It's recommended to do that during
    // the
    // handshake when setting up a game, because if the screen turns off, the
    // game will be
    // cancelled.
    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}