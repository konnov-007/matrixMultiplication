package konnov.commr.vk.threadslab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "myLogs";
    private final int maximumValue = 100;
    private TextView textView;
    private TextView elapsedTime;
    private int [][] matrixOne;
    private int [][] matrixTwo;
    private int [][] newMatrix;
    private int numberOfRowsAndColumns;
    private EditText rowsAndColumnsNumberEditText;
    private Button button;
    private boolean theNumberOfElementsIsHuge;
    private int numberOfThreads = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        rowsAndColumnsNumberEditText = (EditText) findViewById(R.id.rowsAndColumnsNumberEditText);
        elapsedTime = (TextView) findViewById(R.id.elapsedTime);
        button = (Button) findViewById(R.id.button);
    }



    public void executeBtnClicked(View view){
        button.setVisibility(View.INVISIBLE);
        initialization();
        executeThreads();
    }




    private void executeThreads(){
        final int [] initalValue = new int[4];
        final int[] finalValue = new int[4];
        switch (numberOfThreads){
            case 1:
                initalValue[0] = 0;
                finalValue[0] = numberOfRowsAndColumns;
                break;
            case 2:
                initalValue[0] = 0;
                initalValue[1] = numberOfRowsAndColumns/2;
                finalValue[0] = numberOfRowsAndColumns/2;
                finalValue[1] = numberOfRowsAndColumns;
                break;
            case 3:
                initalValue[0] = 0;
                initalValue[1] = numberOfRowsAndColumns/3;
                initalValue[2] = (numberOfRowsAndColumns * 2)/3;
                finalValue[0] = numberOfRowsAndColumns/3;
                finalValue[1] = (numberOfRowsAndColumns * 2)/3;
                finalValue[2] = numberOfRowsAndColumns;


                break;
            case 4:
                initalValue[0] = 0;
                initalValue[1] = numberOfRowsAndColumns/4;
                initalValue[2] = numberOfRowsAndColumns/2;
                initalValue[3] = (int)(numberOfRowsAndColumns*0.75);
                finalValue[0] = numberOfRowsAndColumns/4;
                finalValue[1] = numberOfRowsAndColumns/2;
                finalValue[2] = (int)(numberOfRowsAndColumns*0.75);
                finalValue[3] = numberOfRowsAndColumns;
                break;
        }

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < numberOfRowsAndColumns; i++) {
                        for (int j = initalValue[0]; j < finalValue[0]; j++) {
                            for (int k = initalValue[0]; k < finalValue[0]; k++) {
                                newMatrix[i][j] += matrixOne[i][k] * matrixTwo[k][j];
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.currentThread().interrupt();
            }
        });


        final Thread threadTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < numberOfRowsAndColumns; i++) {
                        for (int j = initalValue[1]; j < finalValue[1]; j++) {
                            for (int k = initalValue[1]; k < finalValue[1]; k++) {
                                newMatrix[i][j] += matrixOne[i][k] * matrixTwo[k][j];
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.currentThread().interrupt();
            }
        });


        final Thread threadThree = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < numberOfRowsAndColumns; i++) {
                        for (int j = initalValue[2]; j < finalValue[2]; j++) {
                            for (int k = initalValue[2]; k < finalValue[2]; k++) {
                                newMatrix[i][j] += matrixOne[i][k] * matrixTwo[k][j];
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.currentThread().interrupt();
            }
        });


        final Thread threadFour = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < numberOfRowsAndColumns; i++) {
                        for (int j = initalValue[3]; j < finalValue[3]; j++) {
                            for (int k = initalValue[3]; k < finalValue[3]; k++) {
                                newMatrix[i][j] += matrixOne[i][k] * matrixTwo[k][j];
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.currentThread().interrupt();
            }
        });


        final long startTime = System.nanoTime();

        switch (numberOfThreads){
            case 1:
                thread.start();
                break;
            case 2:
                thread.start();
                threadTwo.start();
                break;
            case 3:
                thread.start();
                threadTwo.start();
                threadThree.start();
                break;
            case 4:
                thread.start();
                threadTwo.start();
                threadThree.start();
                threadFour.start();
                break;
        }

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean threadsAreDone = false;
                        switch (numberOfThreads){
                            case 1:
                                if(!thread.isAlive())
                                    threadsAreDone = true;
                                break;
                            case 2:
                                if(!thread.isAlive() && !threadTwo.isAlive())
                                    threadsAreDone = true;
                                break;
                            case 3:
                                if(!thread.isAlive() && !threadTwo.isAlive() && !threadThree.isAlive())
                                    threadsAreDone = true;
                                break;
                            case 4:
                                if(!thread.isAlive() && !threadTwo.isAlive() && !threadThree.isAlive() && !threadFour.isAlive())
                                    threadsAreDone = true;
                                break;
                        }
                        if(threadsAreDone) {
                            long stopTime = System.nanoTime();
                            elapsedTime.setText("Execution time: " + String.valueOf((double) (stopTime - startTime) / 1000000000L) + " sec");
                            matrixOutput();
                            button.setVisibility(View.VISIBLE);
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, 100);


    }






    private void initialization(){
        if(rowsAndColumnsNumberEditText.getText().toString().length() != 0  &&
                !rowsAndColumnsNumberEditText.getText().toString().equals("0")) {
            numberOfRowsAndColumns = Integer.parseInt(rowsAndColumnsNumberEditText.getText().toString());
            newMatrix = new int[numberOfRowsAndColumns][numberOfRowsAndColumns];
            theNumberOfElementsIsHuge = numberOfRowsAndColumns>200;
            randomizingMatrix();
        }
        else{
            Toast.makeText(this, "Input the number of rows and columns", Toast.LENGTH_SHORT).show();
        }
    }




    private void randomizingMatrix(){
        Random random = new Random();
        matrixOne = new int[numberOfRowsAndColumns][numberOfRowsAndColumns];
        matrixTwo = new int[numberOfRowsAndColumns][numberOfRowsAndColumns];
        for(int i = 0; i < numberOfRowsAndColumns; i++){
            for(int j = 0; j < numberOfRowsAndColumns; j++){
                matrixOne[i][j] = random.nextInt(maximumValue);
                matrixTwo[i][j] = random.nextInt(maximumValue);
            }
        }
    }





    private void matrixOutput(){
        if(!theNumberOfElementsIsHuge) {
            textView.setText("");
            for (int i = 0; i < numberOfRowsAndColumns; i++) {
                for (int j = 0; j < numberOfRowsAndColumns; j++) {
                    textView.append("A [" + String.valueOf(i) + "][" + String.valueOf(j) + "] (" + String.valueOf(matrixOne[i][j]) + ")  *  " +
                            "B [" + String.valueOf(matrixTwo[i][j]) + ")    " + "C [" + String.valueOf(i) + "][" + String.valueOf(j) + "] = " +
                            String.valueOf(newMatrix[i][j]) + "\n");
                }
            }

        }
        else
            textView.setText("The execution has been completed, but the number of elements is too big to output");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.one_thread_menu:
                numberOfThreads = 1;
                Toast.makeText(this, "ONE THREAD", Toast.LENGTH_SHORT).show();
                break;
            case R.id.two_thread_menu:
                numberOfThreads = 2;
                Toast.makeText(this, "TWO THREADS", Toast.LENGTH_SHORT).show();
                break;
            case R.id.three_thread_menu:
                numberOfThreads = 3;
                Toast.makeText(this, "THREE THREADS", Toast.LENGTH_SHORT).show();
                break;
            case R.id.four_thread_menu:
                numberOfThreads = 4;
                Toast.makeText(this, "FOUR THREADS", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}

