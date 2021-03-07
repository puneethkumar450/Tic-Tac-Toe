package com.example.tic_tac_toe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    TableLayout mGameBardLayout;
    TextView m_X_Trun;
    TextView m_O_Trun;
    char [][] mBoard;
    char mPlayerTurn;
    private int mBoardGrideSize;

    ObjectAnimator anim;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBoardGrideSize = Integer.parseInt(getString(R.string.size_of_board));
        mBoard = new char [mBoardGrideSize][mBoardGrideSize];
        mGameBardLayout = findViewById(R.id.mainBoard);
        m_X_Trun = findViewById(R.id.turn_x);
        m_O_Trun = findViewById(R.id.turn_O);
        animate();
        resetBoard();
        if(mPlayerTurn == 'X')
        {
            m_X_Trun.setText("Player "+ mPlayerTurn + " turn ");
            m_O_Trun.setText("Waiting for player " + mPlayerTurn + " to finish");

            if(anim != null){
                anim.cancel();
                m_X_Trun.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
            anim = ObjectAnimator.ofInt(m_O_Trun, "textColor",
                    Color.LTGRAY,
                    Color.RED,
                    Color.LTGRAY);

        }else{
            m_O_Trun.setText("Player "+ mPlayerTurn + " turn ");
            m_X_Trun.setText("Waiting for player " + mPlayerTurn + " to finish");

            if(anim != null){
                anim.cancel();
                m_O_Trun.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
            anim = ObjectAnimator.ofInt(m_X_Trun, "textColor",
                    Color.LTGRAY,
                    Color.RED,
                    Color.LTGRAY);
        }
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();

        for(int i = 0; i< mGameBardLayout.getChildCount(); i++){
            TableRow row = (TableRow) mGameBardLayout.getChildAt(i);
            for(int j = 0; j<row.getChildCount(); j++){
                TextView tv = (TextView) row.getChildAt(j);
                tv.setText(R.string.none);
                tv.setOnClickListener(Move(i, j, tv));
            }
        }
    }

    private void restart()
    {
        Intent current = getIntent();
        finish();
        startActivity(current);
        overridePendingTransition(0,0);
    }

    private void animate() {
        final TableRow lRow0 = findViewById(R.id.row0);
        final TableRow lRow1 = findViewById(R.id.row1);
        final TableRow lRow2 = findViewById(R.id.row2);

        lRow0.setTranslationX(-1000);
        lRow0.animate()
                .translationXBy(1000)
                .setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        lRow0.setVisibility(View.VISIBLE);
                    }
                });

        lRow2.setTranslationX(1000);
        lRow2.animate()
                .translationXBy(-1000)
                .setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        lRow2.setVisibility(View.VISIBLE);
                    }
                });


        AlphaAnimation animation1 = new AlphaAnimation(0f, 1.0f);
        animation1.setDuration(500);
        lRow1.setAlpha(1f);
        lRow1.startAnimation(animation1);
    }

    protected void resetBoard()
    {
        Random rand = new Random();
        int randomNum = rand.nextInt((100 - 1) + 1) + 1;
        if ( randomNum % 2 == 0 )
            mPlayerTurn = 'O';
        else
            mPlayerTurn = 'X';

        for(int i = 0; i< mBoardGrideSize; i++){
            for(int j = 0; j< mBoardGrideSize; j++){
                mBoard[i][j] = ' ';
            }
        }
    }

    protected int gameStatus()
    {
        //0 Continue
        //1 X Wins
        //2 O Wins
        //-1 Draw

        for(int i = 0; i< mBoardGrideSize; i++)
        {
            if(check_Row_Equality(i,'X'))
                return 1;
            if(check_Column_Equality(i, 'X'))
                return 1;
            if(check_Row_Equality(i,'O'))
                return 2;
            if(check_Column_Equality(i,'O'))
                return 2;
            if(check_Diagonal('X'))
                return 1;
            if(check_Diagonal('O'))
                return 2;
        }

        boolean boardFull = true;
        for(int i = 0; i< mBoardGrideSize; i++){
            for(int j = 0; j< mBoardGrideSize; j++){
                if (mBoard[i][j] == ' ') {
                    boardFull = false;
                    break;
                }
            }
        }
        if(boardFull)
            return -1;
        else return 0;
    }

    protected boolean check_Diagonal(char player)
    {
        int count_Equal1 = 0,count_Equal2 = 0;
        for(int i = 0; i< mBoardGrideSize; i++)
            if(mBoard[i][i]==player)
                count_Equal1++;
        for(int i = 0; i< mBoardGrideSize; i++)
            if(mBoard[i][mBoardGrideSize -1-i]==player)
                count_Equal2++;
        return count_Equal1 == mBoardGrideSize || count_Equal2 == mBoardGrideSize;
    }

    protected boolean check_Row_Equality(int r, char player)
    {
        int count_Equal=0;
        for(int i = 0; i< mBoardGrideSize; i++){
            if(mBoard[r][i]==player)
                count_Equal++;
        }

        return count_Equal == mBoardGrideSize;
    }

    protected boolean check_Column_Equality(int c, char player){
        int count_Equal=0;
        for(int i = 0; i< mBoardGrideSize; i++){
            if(mBoard[i][c]==player)
                count_Equal++;
        }

        return count_Equal == mBoardGrideSize;
    }

    protected boolean lCell_Set(int r, int c){
        return !(mBoard[r][c]==' ');
    }

    View.OnClickListener Move(final int r, final int c, final TextView tv)
    {
        return new View.OnClickListener()
        {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v)
            {
                if(!lCell_Set(r,c))
                {
                    mBoard[r][c] = mPlayerTurn;
                    if (mPlayerTurn == 'X') {
                        tv.setText(R.string.X);
                        tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.colorAccent));
                        mPlayerTurn = 'O';
                    } else if (mPlayerTurn == 'O') {
                        tv.setText(R.string.O);
                        tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.blue));
                        mPlayerTurn = 'X';
                    }
                    if (gameStatus() == 0)
                    {
                        if(mPlayerTurn == 'X')
                        {
                            m_X_Trun.setText("Player "+ mPlayerTurn + " turn ");
                            m_O_Trun.setText("Waiting for player " + mPlayerTurn + " to finish");
                            if(anim != null){
                                anim.cancel();
                                m_X_Trun.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                        R.color.black));
                            }
                            anim = ObjectAnimator.ofInt(m_O_Trun, "textColor",
                                    Color.LTGRAY,
                                    Color.RED,
                                    Color.LTGRAY);
                            anim.setDuration(1500);
                            anim.setEvaluator(new ArgbEvaluator());
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            anim.start();
                        }else{
                            m_O_Trun.setText("Player "+ mPlayerTurn + " turn ");
                            m_X_Trun.setText("Waiting for player " + mPlayerTurn + " to finish");
                            if(anim != null){
                                anim.cancel();
                                m_O_Trun.setTextColor(ContextCompat.getColor(getApplicationContext()
                                        , R.color.black));
                            }
                            anim = ObjectAnimator.ofInt(m_X_Trun, "textColor",
                                    Color.LTGRAY,
                                    Color.RED,
                                    Color.LTGRAY);
                            anim.setDuration(1500);
                            anim.setEvaluator(new ArgbEvaluator());
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            anim.start();
                        }
                    }
                    else if(gameStatus() == -1){
                        showMatchDrawDialog();
                        stopMatch();
                    }
                    else{
                        showWinnerDialog();
                        stopMatch();
                    }
                }
                else{
                    //txt_turn.setText(txt_turn.getText()+" Choose an Empty Call");
                }
            }
        };
    }

    private void showMatchDrawDialog()
    {
        View lView = LayoutInflater.from(this).inflate(R.layout.dialog_success, null, false);
        new AlertDialog.Builder(this)
                .setView(lView)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                        restart();
                    }
                })
                .show();
        ImageView lImage = lView.findViewById(R.id.success_imageview);
        lImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        TextView lSuccessText = lView.findViewById(R.id.success_text);
        lSuccessText.setText("It's a Draw Match");
        m_O_Trun.setText("");
        m_X_Trun.setText("");
    }

    private void showWinnerDialog()
    {
        m_O_Trun.setText("");
        m_X_Trun.setText("");
        String lWinner;
        if(mPlayerTurn == 'X')
        {
            lWinner =  " O WINNER ";
        }else{
            lWinner = " X WINNER ";
        }

        View lView = LayoutInflater.from(this).inflate(R.layout.dialog_success, null, false);
        new AlertDialog.Builder(this)
                .setView(lView)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                        restart();
                    }
                })
                .show();

        TextView lSuccessText = lView.findViewById(R.id.success_text);
        lSuccessText.setText(lWinner);
    }


    protected void stopMatch(){
        for(int i = 0; i< mGameBardLayout.getChildCount(); i++){
            TableRow row = (TableRow) mGameBardLayout.getChildAt(i);
            for(int j = 0; j<row.getChildCount(); j++){
                TextView tv = (TextView) row.getChildAt(j);
                tv.setOnClickListener(null);
            }
        }
    }
}