package com.eye7.ShootyWooty.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private HashMap<String, Integer> myCurAchievements = null;
    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;
    private boolean interruptEndGame = false;
    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;
    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    String mRoomId = null;
    private boolean endGame = false;
    private main oldShootyWooty = null;
    // Are we playing in multiplayer mode?
    boolean mMultiplayer = false;

    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    String mMyId = null;

    // If non-null, this is the id of the invitation we received via the
    // invitation listener
    String mIncomingInvitationId = null;

    // Message buffer for sending messages
//    byte[] mMsgBuf = new byte[2];
    private Participant host = null;
    private View gameView;
    private Map<String, String> moves = new HashMap<>();
    private String curMoves = null;
    private Boolean validMoves = false;
    private int myIDNum = -1;
    private int numPlayers ;
    private ArrayList<Integer> leftPlayers = new ArrayList<Integer>();
    private Map<String, Integer> idToNum = new HashMap<>();
    private AndroidApplicationConfiguration config;
    private ArrayList<Integer> deadPlayers = new ArrayList<Integer>();
    private main shootyWooty;
    private Map<String,Integer> imMoves = new HashMap<>();
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
                //  resetGameVars();
                startGame(false);
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
//            case R.id.button_sign_out:
//                // user wants to sign out
//                // sign out.
//                Log.d(TAG, "Sign-out button clicked");
//                mSignInClicked = false;
//                Games.signOut(mGoogleApiClient);
//                mGoogleApiClient.disconnect();
//                switchToScreen(R.id.screen_sign_in);
//                break;
//            case R.id.button_invite_players:
//                // show list of invitable players
//                intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 3);
//                switchToScreen(R.id.screen_wait);
//                startActivityForResult(intent, RC_SELECT_PLAYERS);
//                break;
//            case R.id.button_see_invitations:
//                // show list of pending invitations
//                intent = Games.Invitations.getInvitationInboxIntent(mGoogleApiClient);
//                switchToScreen(R.id.screen_wait);
//                startActivityForResult(intent, RC_INVITATION_INBOX);
//                break;
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
//        if(!shootyWootyCreated){
//            shootyWooty = new main(this);
//            gameView = initializeForView(shootyWooty,new AndroidApplicationConfiguration());
//            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.screen_game);
//            linearLayout.addView(gameView,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
//            shootyWootyCreated = true;
//        }
//        if(!shootyWootyCreated) {
//            mGoogleApiClient.reconnect();
//            shootyWooty.reCreate();
//            shootyWootyCreated=true;
//            Log.d(TAG, "Created again");
//        }
        if(oldShootyWooty!=null){
            oldShootyWooty.dispose();
        }
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        // resetGameVars();

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
                achieve = true;
                break;

                //    if (responseCode = RESULT_RECONNECT_REQUIRED);
                //Adding achievements here
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
        //resetGameVars();
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
        // resetGameVars();
        Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());
    }

    // Activity is going to the background. We have to leave the current room.
    @Override
    public void onStop() {
        Log.d(TAG, "**** got onStop");

        // if we're in a room, leave it.
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
    @Override
    public void onResume(){
        if(achieve) {
            achieve = false;
            switchToMainScreen();
        }
        super.onResume();
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
    @Override
    public void onBackPressed(){
        if(mCurScreen!=R.id.screen_main)
            leaveRoom();
        else
            super.onBackPressed();
    }
    // Handle back key to make sure we cleanly leave a game if we are in the middle of one
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent e) {
//        if (keyCode == KeyEvent.KEYCODE_BACK ) {
//            leaveRoom();
//            return true;
//        }
//        return super.onKeyDown(keyCode, e);
//    }

    // Leave the room.
    public void leaveRoom() {
        Log.d(TAG, "Leaving room.");
        mSecondsLeft = 0;

        stopKeepingScreenOn();
        if(!getMultiplayer()){
           // resetLibGdxnVars();
            resetGameVars();

        }

        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);

            //mRoomId = null;
//            resetGameVars();
//            switchToMainScreen();
//           // switchToScreen(R.id.screen_wait);
//        } else {
        }
        switchToMainScreen();
//        reset = true;

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
        if (mRoomId !=null) {
            if(endGame){
                switchToScreen(R.id.GameDecided);
            }
            else
             switchToMainScreen();

//            BaseGameUtil.makeSimpleDialog(this, "Game Notification", "You have quit the game.").show();
        }

        resetGameVars();

    }
//    void resetLibGdxnVars(){
//        linearLayout.removeViewAt(0);
//        oldShootyWooty = shootyWooty;
//        shootyWooty = new main(this);
//        gameView = initializeForView(shootyWooty,config);
//        linearLayout.addView(gameView, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        checkNewLib = true;
//    }
    // Reset game variables in preparation for a new game.
    void resetGameVars() {
        interruptEndGame = false;
        mMultiplayer = false;
        host = null;
        moves = new HashMap<>();
        curMoves = null;
        validMoves = false;
        myIDNum = -1;
        mParticipants = null;
        mMyId = null;
        imMoves = new HashMap<>();
        deadPlayers = new ArrayList<Integer>();
        leftPlayers = new ArrayList<Integer>();
        idToNum = new HashMap<>();
        linearLayout.removeViewAt(0);
        oldShootyWooty = shootyWooty;
        shootyWooty = new main(this);
        gameView = initializeForView(shootyWooty,config);
        linearLayout.addView(gameView, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

    }
    // Called when we get disconnected from the room. We return to the main screen.
    @Override
    public void onDisconnectedFromRoom(Room room) {
        Log.w(TAG, "Left from disconnected");
        if(!endGame) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            mRoomId = null;
            showGameError();
        }

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

    //    @Override
//    public void onBackPressed() {
//        reset = true;
////        resetGameVars();
//    }

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

    public void updateHost(List<String> peersWhoLeft){
        for(String s: peersWhoLeft){
            if (s.equals(host.getParticipantId())){
                chooseHost();
            }
        }
    }
    void updateRoom2(Room room, List<String> peers){
        if(room!=null) {
            for(String peer:peers){
                leftPlayers.add(idToNum.get(peer));
                deadPlayers.add(idToNum.get(peer));
            }
            mParticipants = room.getParticipants();
        }
        if ((mParticipants.size()<=1||room==null)&&!endGame){
            Log.d(TAG, "In check indicating everyone LEFT");
            leaveRoom();

        }

        sortParticipants();
//            if(mParticipants.size()==1){
//                Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
//                mRoomId = null;
//                switchToScreen(R.id.screen_wait);
//                switchToMainScreen();
//                return;
//            }

        if (mParticipants.get(0).getParticipantId().equals(mMyId))
            updateHost(peers);

    }
    public void setEndGame(){
        endGame = true;
    }
    public ArrayList<Integer> getLeftPlayers(){
        return leftPlayers;
    }
    public void clearLeftPlayers(){
        leftPlayers = new ArrayList<>();
    }
    void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
            sortParticipants();
        }
//        int check = 0;
//        if (mParticipants != null) {
//            if (mRoomId != null) {
//                for (Participant p : mParticipants) {
//                    String pid = p.getParticipantId();
//                    if (pid.equals(mMyId))
//                        continue;
//                    if (p.getStatus() == Participant.STATUS_JOINED) {
//                        check++;
//                    }
//                }
//            }
//
//        }
        if ((mParticipants.size()<=1||room==null)&&!endGame){
            Log.d(TAG, "In check indicating everyone LEFT");
            leaveRoom();

        }
    }

    public int getNumPlayers(){
        return numPlayers;
    }
    public ArrayList<Integer> getDeadPlayers(){
        return deadPlayers;
    }
    /*
     * GAME LOGIC SECTION. Methods that implement the game's rules.
     */

    // Current state of the game:
    int mSecondsLeft = -1; // how long until the game ends (seconds)
    final static int GAME_DURATION = 20; // game duration, seconds.
    int mScore = 0; // user's current score



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
        if(idToNum.size()==0){
            i=0;
            for(Participant p: mParticipants){
                idToNum.put(p.getParticipantId(),i);
                i++;
            }
        }
        mParticipants = sorted;
    }
    // Start the gameplay phase of the game.
    void startGame(boolean multiplayer) {
        mMultiplayer = multiplayer;
//        if(!shootyWootyCreated) {
//            shootyWooty.create();
//        }
        if(mMultiplayer) {
            sortParticipants();
            if (mParticipants.get(0).getParticipantId().equals(mMyId)) {
                Log.d(TAG, "Choosing Host");
                chooseHost();
            }
        }
        switchToScreen(R.id.screen_game);


    }
    public boolean getValid(){
        return validMoves;
    }
    public void setValid(boolean v){
        validMoves = v;
    }
    public String getMoves(){
        return curMoves;
    }
    public int getMyID(){
        int i =0;
        if(myIDNum==-1) {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId)) {
                    myIDNum = i;
                }
                i++;
            }
        }
        return myIDNum;
    }
    public String getActive(){
        return mRoomId;
    }
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
    public void chooseHost(){
        Random rnd = new Random();
        String s = mParticipants.get(rnd.nextInt(mParticipants.size()-1)).getParticipantId();
        host = getHost(s);
        sendMessageAll("$", s);
        Log.d(TAG, "Host chosen message sent "+s + mMyId);
    }

    public void readyToSend(){
        String movestoSend = "";
        int i=0;
        for(Participant p:mParticipants){
            if(!deadPlayers.contains(i))
                movestoSend += moves.get(p.getParticipantId());
        }
//        sendMessageHost("@");
        curMoves = movestoSend;
//        lock.unlock();
//        execute.signalAll();
        validMoves = true;
        moves = new HashMap<>();
    }



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

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
//        Log.d(TAG, "Message Received");
        byte[] buf = rtm.getMessageData();
        String s = new String(buf);
        String sender = rtm.getSenderParticipantId();
        if (s.charAt(0)=='$'){
            host = getHost(s.substring(1));
        }
        if(s.charAt(0)=='!'){
            moves.put(sender,s);
            if (moves.size()==mParticipants.size()){
                readyToSend();
            }
        }
        if(s.charAt(0)=='^'){
            imMoves.put(sender,Integer.valueOf(s.substring(1)));
        }
//        if(s.charAt(0)=='@'){
//            Log.d(TAG, "Message Received Opponent dead" + s.substring(1));
//            deadPlayers.add(Integer.getInteger(s.substring(1)));
//        }
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
//            Log.d(TAG, "Unrealiable Message sent "+ p.getParticipantId());
        }

    }
    public void sendMessageAll(String x, String s){
        if(!mMultiplayer)
            return;
        if (x.equals("!")){
//            lock.lock();
            moves.put(mMyId,"!"+s);
            if (moves.size()==mParticipants.size()){
                readyToSend();
            }

//            statusMessage.put(mMyId,0);
        }
        if (x.equals("@")){
            if(!deadPlayers.contains(Integer.getInteger(s.substring(1))))
                deadPlayers.add(Integer.getInteger(s.substring(1)));
            return;
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

    public boolean getMultiplayer(){
        return mMultiplayer;
    }
    /*
     * UI SECTION. Methods that implement the game's UI.
     */

    // This array lists everything that's clickable, so we can install click
    // event handlers.
    final static int[] CLICKABLES = {
            R.id.button_accept_popup_invitation,
            //R.id.button_invite_players,
            R.id.button_quick_game,
            R.id.button_quick_game1,
            //R.id.button_see_invitations,
            R.id.button_sign_in,
            // R.id.button_sign_out,
            R.id.button_single_player_2
    };
    public void addAchievements(HashMap<String, Integer> myAchievements) throws InterruptedException {
        if(myAchievements.get("Kills")==2){
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




//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
//
//                    }
//                });
//            };
//        };
//        thread.start();
//        thread.join();




    }
    public void gameDecided(String s, final HashMap<String, Integer> myAchievements) throws InterruptedException {
        final String check = s;

        Thread thread = new Thread(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "BEGINNING SCREEN CHANGE");
                        try {
                            //resetLibGdxnVars();


                            String uri;
                            if(check!="draw")
                                uri = "@drawable/"+check+Integer.toString(myIDNum+1);
                            else
                                uri = "@drawable/draw";

                            int imageResource = getResources().getIdentifier(uri,"drawable", getPackageName());
                            Log.d(TAG, "FOUND IMAGE URI");
                            ImageView imageView = (ImageView)findViewById(R.id.EndImage);
                            Drawable res = getResources().getDrawable(imageResource);
                            imageView.setImageDrawable(res);
                            Log.d(TAG, "SET IMAGE");


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


        try {
            addAchievements(myAchievements);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        leaveRoom();


        //  gameView.setOnTouchListener(new OnTouchListener() {
        //      @Override
        //      public boolean onTouch(View v, MotionEvent event) {
        //show dialog here
        //          Log.d(TAG, "CALLED ACHIEVEMENTS");

        //          return true;
        //      }
        //  });
    }

    // This array lists all the individual screens our game has.
    final static int[] SCREENS = {
            R.id.screen_game, R.id.screen_main, R.id.screen_sign_in,
            R.id.screen_wait, R.id.GameDecided
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
    void switchToDecision(){
        //switchToScreen(R.id.GameDecided);
       // startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);

    }

    void switchToMainScreen() {
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