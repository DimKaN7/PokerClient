package com.example.dimka_n7.pokerclient;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private final String TAG = "DimKa_N7";

    private PokerClient client = null;
    private taskConnect tC = null;
    private taskSend tS;

    private ArrayList<String> myCards;
    private ArrayList<String> sizes;
    private ArrayList<String> suits;

    EditText editTextName;

    Button buttonConnect;
    Button buttonFold;
    Button buttonCheck;
    Button buttonCall;
    Button buttonRaise;

    TextView textViewName;
    TextView textViewCall;
    TextView textViewBank;

    TextView textViewABank;

    TextView textViewP1Name;
    TextView textViewP1Call;
    TextView textViewP1Bank;

    TextView textViewP2Name;
    TextView textViewP2Call;
    TextView textViewP2Bank;

    TextView textViewStartBank;
    TextView textViewRate;
    TextView textViewRaise;

    ImageView imageViewP1;
    ImageView imageViewP2;

    ImageView imageViewTableCard1;
    ImageView imageViewTableCard2;
    ImageView imageViewTableCard3;
    ImageView imageViewTableCard4;
    ImageView imageViewTableCard5;

    ImageView imageViewP1C1;
    ImageView imageViewP1C2;

    ImageView imageViewP2C1;
    ImageView imageViewP2C2;

    TextView[] textViewsPNames = new TextView[2];
    TextView[] textViewsPBanks = new TextView[2];
    TextView[] textViewsPCalls = new TextView[2];

    SeekBar seekBarRaise;

    public ImageView imageViewCard1;
    public ImageView imageViewCard2;
    public Bitmap cards;

    int cardId = 1;

    int bank = 0;
    int rate = 0; // фиксированная ставка на игру
    int myRate = 0; // ставка
    int needToCall = 0;
    boolean isBigBlind = false;
    boolean isMyTurn = false;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        editTextName = (EditText) findViewById(R.id.editTextName);

        buttonConnect  = (Button) findViewById(R.id.buttonConnect);

        buttonCheck = (Button) findViewById(R.id.buttonCheck);
        buttonCall = (Button) findViewById(R.id.buttonCall);
        buttonFold = (Button) findViewById(R.id.buttonFold);
        buttonRaise = (Button) findViewById(R.id.buttonRaise);

        textViewBank   = (TextView) findViewById(R.id.textViewBank);
//        textViewBank.setText("300000");

        textViewName   = (TextView) findViewById(R.id.textViewName);
//        textViewName.setText("Dima");
        textViewRaise  = (TextView) findViewById(R.id.textViewRaise);
        textViewCall = (TextView) findViewById(R.id.textViewCall);

        textViewABank = (TextView) findViewById(R.id.textViewABank);

        textViewP1Name = (TextView) findViewById(R.id.textViewP1Name);
        textViewP1Bank = (TextView) findViewById(R.id.textViewP1Bank);
        textViewP1Call = (TextView) findViewById(R.id.textViewP1Call);

        textViewsPNames[0] = textViewP1Name;
        textViewsPBanks[0] = textViewP1Bank;
        textViewsPCalls[0] = textViewP1Call;

        textViewP2Name = (TextView) findViewById(R.id.textViewP2Name);
        textViewP2Bank = (TextView) findViewById(R.id.textViewP2Bank);
        textViewP2Call = (TextView) findViewById(R.id.textViewP2Call);

        textViewsPNames[1] = textViewP2Name;
        textViewsPBanks[1] = textViewP2Bank;
        textViewsPCalls[1] = textViewP2Call;

        textViewStartBank = (TextView) findViewById(R.id.textViewStartBank);
        textViewRate = (TextView) findViewById(R.id.textViewRate);

        imageViewCard1 = (ImageView) findViewById(R.id.imageViewCard1);
        imageViewCard2 = (ImageView) findViewById(R.id.imageViewCard2);

        imageViewP1 = (ImageView) findViewById(R.id.imageViewPlayer1);
        imageViewP2 = (ImageView) findViewById(R.id.imageViewPlayer2);

        imageViewTableCard1 = (ImageView) findViewById(R.id.tableCard1);
        imageViewTableCard2 = (ImageView) findViewById(R.id.tableCard2);
        imageViewTableCard3 = (ImageView) findViewById(R.id.tableCard3);
        imageViewTableCard4 = (ImageView) findViewById(R.id.tableCard4);
        imageViewTableCard5 = (ImageView) findViewById(R.id.tableCard5);

        imageViewP1C1 = (ImageView) findViewById(R.id.imageViewP1C1);
        imageViewP1C2 = (ImageView) findViewById(R.id.imageViewP1C2);

        imageViewP2C1 = (ImageView) findViewById(R.id.imageViewP2C1);
        imageViewP2C2 = (ImageView) findViewById(R.id.imageViewP2C2);

        seekBarRaise = (SeekBar) findViewById(R.id.seekBarRaise);
        seekBarRaise.setVisibility(View.INVISIBLE);

        textViewRaise.setVisibility(View.INVISIBLE);

        AssetManager assetManager = getAssets();
        try {
            imageViewP1.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("images/imagePlayer.jpg")));
            imageViewP2.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("images/imagePlayer.jpg")));
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.toString());
        }

        setStartCards();
        setEnable(isMyTurn, "");

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCards = new ArrayList<>(2);
                sizes   = new ArrayList<>(13);
                suits   = new ArrayList<>(4);

                name = editTextName.getText().toString();
                textViewName.setText(name);

                sizes.add("T");
                sizes.add("2");
                sizes.add("3");
                sizes.add("4");
                sizes.add("5");
                sizes.add("6");
                sizes.add("7");
                sizes.add("8");
                sizes.add("9");
                sizes.add("10");
                sizes.add("J");
                sizes.add("Q");
                sizes.add("K");

                suits.add("c");
                suits.add("d");
                suits.add("h");
                suits.add("s");

                tC = new taskConnect();
                tC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                setStartCards();
                buttonConnect.setEnabled(false);
                editTextName.setEnabled(false);
            }
        });

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMyTurn = false;
                myRate = 0;
                setEnable(isMyTurn, "");
                tS = new taskSend();
                tS.execute("CH:" + name);
            }
        });

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMyTurn = false;
                myRate = needToCall;
                buttonCall.setText("Call");
                textViewCall.setText(Integer.toString(myRate));
                tS = new taskSend();
                tS.execute("NR:" + name + ":" + Integer.toString(myRate));
                needToCall = 0;
                setEnable(isMyTurn, "");
            }
        });

        buttonFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonRaise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonRaise.getText().equals("Raise")) {
                    buttonRaise.setText("Ok");
                    seekBarRaise.setVisibility(View.VISIBLE);
                    textViewRaise.setVisibility(View.VISIBLE);
                    seekBarRaise.setProgress(rate); // далее заменить на ту сумму которая лежит на столе
                } else {
                    // все работало, это хз
                    isMyTurn = false;
                    myRate = Integer.parseInt(textViewRaise.getText().toString());
                    textViewCall.setText(Integer.toString(myRate));
                    tS = new taskSend();
                    tS.execute("NR:" + name + ":" + Integer.toString(myRate));
                    setEnable(isMyTurn, "");
                    //

                    buttonRaise.setText("Raise");
                    seekBarRaise.setProgress(0);
                    seekBarRaise.setVisibility(View.INVISIBLE);
                    textViewRaise.setVisibility(View.INVISIBLE);
                }
            }
        });

        seekBarRaise.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = i / (rate / 2);
                i = i * (rate / 2);
                textViewRaise.setText(Integer.toString(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void setEnable(boolean isMyTurn, String sMessage) {

        if (isMyTurn) {

            if ((Integer.parseInt(sMessage.split("&")[1].substring(2)) == 0) && sMessage != "") {
                buttonCheck.setEnabled(true);
                buttonFold.setEnabled(true);
                buttonRaise.setEnabled(true);
            } else {
                buttonCheck.setEnabled(false);
                buttonCall.setEnabled(true);
                buttonFold.setEnabled(true);
                buttonRaise.setEnabled(true);
                buttonCall.setText("Call " + sMessage.split("&")[1].substring(2));
                needToCall = Integer.parseInt(sMessage.split("&")[1].substring(2));
            }
        } else {
            buttonCall.setEnabled(false);
            buttonCheck.setEnabled(false);
            buttonFold.setEnabled(false);
            buttonRaise.setEnabled(false);
        }

    }

    public void setMyRate(int newRate) { // делаем новую ставку

        this.myRate = newRate;
        textViewCall.setText(Integer.toString(newRate));
        tS = new taskSend();
        tS.execute("NR:" + name + ":" + Integer.toString(myRate));

    }

    public void onClick(View view) {

        switch (cardId) {
            case 1:
                tS = new taskSend();
                tS.execute("NC:1");
                break;
            case 2:
                tS = new taskSend();
                tS.execute("NC:2");
                break;
            case 3:
                tS = new taskSend();
                tS.execute("NC:3");
                break;
            case 4:
                tS = new taskSend();
                tS.execute("NC:4");
                break;
            case 5:
                tS = new taskSend();
                tS.execute("NC:5");
                break;
            case 6:
                tS = new taskSend();
                tS.execute("EG:1");
                break;
            case 7:
                tS = new taskSend();
                tS.execute("NG:1");
                break;
            case 8:
                tS = new taskSend();
                tS.execute("NG:1");
//                setMyRate(0);
                break;
        }

    }

    public void setBank(int bank, boolean isStartBank) {

        this.bank = bank;
        textViewBank.setText(Integer.toString(this.bank));
        if (isStartBank) {
            textViewP1Bank.setText(Integer.toString(this.bank));
            textViewStartBank.setText("Start bank: " + this.bank);
        }

    }

    public void setRate(int rate) {

        this.rate = rate;
        textViewRate.setText("Rate: " + Integer.toString(rate));

    }

    public void setStartCards() {

        AssetManager assetManager = getAssets();
        try {
            cards = BitmapFactory.decodeStream(assetManager.open("images/cards.png"));
            setCard("n_n", imageViewCard1);
            setCard("n_n", imageViewCard2);

            setCard("n_n", imageViewTableCard1);
            setCard("n_n", imageViewTableCard2);
            setCard("n_n", imageViewTableCard3);
            setCard("n_n", imageViewTableCard4);
            setCard("n_n", imageViewTableCard5);

            setCard("n_n", imageViewP1C1);
            setCard("n_n", imageViewP1C2);
            setCard("n_n", imageViewP2C1);
            setCard("n_n", imageViewP2C2);
        } catch (Exception e) {
            Log.e(TAG, "Can not get bitmap: " + e.toString());
        }

    }

    public void setCard(String card, ImageView view) {

        if (card.equals("n_n")) {
            Bitmap bCard = Bitmap.createBitmap(cards, 184, 536, 92, 134);
            view.setImageBitmap(bCard);
        } else {
            String size = card.split("_")[0]; // размер карты
            String suit = card.split("_")[1]; // масть карты
//            Log.e(TAG, "Size in: " + size);
//            Log.e(TAG, "Suit in: " + suit);
            Bitmap bCard = Bitmap.createBitmap(cards, sizes.indexOf(size) * 92, suits.indexOf(suit) * 134, 92, 134);
            view.setImageBitmap(bCard);
        }

    }

    class taskSend extends AsyncTask<String, Void, Void> {

        String message;

        @Override
        protected Void doInBackground(String... strings) {

            if (client != null) {
                client.sendMessage(strings[0].toString());
                message = strings[0].toString(); // New game - следующая раздача End game - конец игры, вскрываем карты  New Rate - новая ставка New card - следующая карта на столе
            }
            return null;

        }

//        @Override
//        protected void onPostExecute(Void result) {
//
//            super.onPostExecute(result);
//            editTextMessage.setText("");
//            Log.e(TAG, "Client " + client.addr.toString() + ": " + message);
//
//        }

    }

    class taskConnect extends AsyncTask<String,String,PokerClient> {

        @Override
        protected PokerClient doInBackground(String... message) {

            client = new PokerClient(new PokerClient.OnMessageReceived() {

                @Override
                public void messageReceived(final String message) {

                    if (message.equals("Server stopped")) {
                        client.setStop();
                        client = null;
                        myCards.clear();
                        myCards = null;
                        sizes.clear();
                        sizes = null;
                        suits.clear();
                        suits = null;
                        bank = 0;
                        rate = 0;
                        myRate = 0;
                        cardId = 1;
                        needToCall = 0;
                        isMyTurn = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setCard("n_n", imageViewCard1);
                                setCard("n_n", imageViewCard2);

                                setCard("n_n", imageViewTableCard1);
                                setCard("n_n", imageViewTableCard2);
                                setCard("n_n", imageViewTableCard3);
                                setCard("n_n", imageViewTableCard4);
                                setCard("n_n", imageViewTableCard5);

                                setCard("n_n", imageViewP1C1);
                                setCard("n_n", imageViewP1C2);
                                setCard("n_n", imageViewP2C1);
                                setCard("n_n", imageViewP2C2);

                                buttonConnect.setEnabled(true);
                                editTextName.setEnabled(true);
                                textViewBank.setText("");
                                textViewStartBank.setText("Start bank: ");
                                textViewRate.setText("Rate: ");
                                textViewABank.setText("");
                                textViewCall.setText("");

                                textViewRaise.setVisibility(View.INVISIBLE);
                                seekBarRaise.setVisibility(View.INVISIBLE);

                                setEnable(isMyTurn, "");

                                buttonCall.setText("Call");

                                for (int i = 0; i < 2; i++) {
                                    textViewsPNames[i].setText("");
                                    textViewsPCalls[i].setText("");
                                }
                            }
                        });
                        tC.cancel(true);
                        tC = null;
                        return;
                    }

                    final String[] components = message.split("&");
                    switch (components[0]) {
                        case "D": // изначальная рассылка карт
                            cardId = 1;
                            myCards.clear();
                            myCards.add(components[1]);
                            myCards.add(components[2]);
                            needToCall = 0;
                            myRate = 0;
//                            Log.e(TAG, "Card: " + myCards.get(0) + " " + myCards.get(1));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setCard("n_n", imageViewTableCard1);
                                    setCard("n_n", imageViewTableCard2);
                                    setCard("n_n", imageViewTableCard3);
                                    setCard("n_n", imageViewTableCard4);
                                    setCard("n_n", imageViewTableCard5);

                                    setCard("n_n", imageViewP1C1);
                                    setCard("n_n", imageViewP1C2);
                                    setCard("n_n", imageViewP2C1);
                                    setCard("n_n", imageViewP2C2);

                                    setCard(myCards.get(0), imageViewCard1);
                                    setCard(myCards.get(1), imageViewCard2);

                                    textViewABank.setText("");

                                    if (isBigBlind) {
                                        Log.d(TAG, "YA ZDEZ");
                                        isMyTurn = false;
                                        setMyRate(rate);
//                                        setEnable(isMyTurn, "");
                                    } else {
                                        setMyRate(0);
                                    }

                                }
                            });
                            break;
                        case "SB": // start bank - присылается в самом начале
                            bank = Integer.parseInt(components[1]);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setBank(bank, true);
                                    seekBarRaise.setMax(bank);
                                }
                            });
                            break;
                        case "BB": // Big Blind
                            isBigBlind = true;
                            break;
                        case "R": // Rate
                            rate = Integer.parseInt(components[1]);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setRate(rate);
                                    seekBarRaise.setProgress(rate);
                                }
                            });
                            break;
                        case "NP": // New Player
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (textViewP1Name.getText().equals("")) {
                                        textViewP1Name.setText(components[1]);
                                        textViewP1Bank.setText(Integer.toString(bank));
                                    } else {
                                        if (textViewP2Name.getText().equals("")) {
                                            textViewP2Name.setText(components[1]);
                                            textViewP2Bank.setText(Integer.toString(bank));
                                        }
                                    }
                                }
                            });
                            break;
                        case "NC": // Next Card
                            switch (cardId) {
                                case 1:
                                    myRate = 0;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setCard(components[1], imageViewTableCard1);
                                            textViewCall.setText("0");
                                            for (int i = 0; i < 2; i++) {
                                                if (!textViewsPNames[i].getText().equals("")) {
                                                    textViewsPCalls[i].setText("0");
                                                }
                                            }
//                                            setEnable(isMyTurn, "");
//                                            setMyRate(0);
                                        }
                                    });
                                    cardId++;
                                    break;
                                case 2:
                                    myRate = 0;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setCard(components[1], imageViewTableCard2);
                                            textViewCall.setText("0");
                                            for (int i = 0; i < 2; i++) {
                                                if (!textViewsPNames[i].getText().equals("")) {
                                                    textViewsPCalls[i].setText("0");
                                                }
                                            }
//                                            setEnable(isMyTurn, "");
//                                            setMyRate(0);
                                        }
                                    });
                                    cardId++;
                                    break;
                                case 3:
                                    myRate = 0;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setCard(components[1], imageViewTableCard3);
                                            textViewCall.setText("0");
                                            for (int i = 0; i < 2; i++) {
                                                if (!textViewsPNames[i].getText().equals("")) {
                                                    textViewsPCalls[i].setText("0");
                                                }
                                            }
//                                            setEnable(isMyTurn, "");
//                                            setMyRate(0);
                                        }
                                    });
                                    cardId++;
                                    break;
                                case 4:
                                    myRate = 0;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setCard(components[1], imageViewTableCard4);
                                            textViewCall.setText("0");
                                            for (int i = 0; i < 2; i++) {
                                                if (!textViewsPNames[i].getText().equals("")) {
                                                    textViewsPCalls[i].setText("0");
                                                }
                                            }
//                                            setEnable(isMyTurn, "");
//                                            setMyRate(0);
                                        }
                                    });
                                    cardId++;
                                    break;
                                case 5:
                                    myRate = 0;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setCard(components[1], imageViewTableCard5);
                                            textViewCall.setText("0");
                                            for (int i = 0; i < 2; i++) {
                                                if (!textViewsPNames[i].getText().equals("")) {
                                                    textViewsPCalls[i].setText("0");
                                                }
                                            }
//                                            setEnable(isMyTurn, "");
//                                            setMyRate(0);
                                        }
                                    });
                                    cardId++;
                                    break;
                            }
                            break;
                        case "PI": // Players info - при подключении получается инфа о уже подключившихся
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 1; i < components.length; i++) {
                                        textViewsPNames[i - 1].setText(components[i]);
                                    }
                                }
                            });
                            break;
                        case "EG": // вскрываем карты
                            String name = components[1];
                            final String card1 = components[2];
                            final String card2 = components[3];
                            if (textViewP1Name.getText().equals(name)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setCard(card1, imageViewP1C1);
                                        setCard(card2, imageViewP1C2);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setCard(card1, imageViewP2C1);
                                        setCard(card2, imageViewP2C2);
                                    }
                                });
                            }
                            cardId++;
                            isBigBlind = false;
                            break;
                        case "NR": // информация о новых ставках игроков
                            if (textViewP1Name.getText().equals(components[1])) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textViewP1Call.setText(components[2]);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textViewP2Call.setText(components[2]);
                                    }
                                });
                            }
                            break;
                        case "YT": // your tern - твой ход
                            isMyTurn = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setEnable(isMyTurn, message);
                                }
                            });
                            break;
                        case "TB": // Table bank - информация о общем банке на столе
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewABank.setText(components[1]);
                                }
                            });
                            break;
                    }

                }

            });
            client.setName(name);
            client.run();
            return null;

        }

    }

}
