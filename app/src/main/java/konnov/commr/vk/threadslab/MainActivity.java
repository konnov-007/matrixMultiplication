package konnov.commr.vk.threadslab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "myLogs";
    private final int maximumValue = 100; //максимальное значение рандома для каждого элемента матрицы
    private TextView textView; // текствью с матрицей получившейся при умножении наших матриц
    private TextView elapsedTime; // время потраченное на умножение
    private int [][] matrixOne; // первая матрица
    private int [][] matrixTwo; // вторая
    private int [][] newMatrix; // матрица которая = первая матрица * вторая матрица
    private int numberOfRowsAndColumns;  // количество строк и столбцов для matrixOne и matrixTwo
    private EditText rowsAndColumnsNumberEditText; // эдит текст для ввода numberOfRowsAndColumns
    private Button button; // кнопка при нажатии на которую происходит умножение
    private final int maximumNumberOfRowsToOutput = 50; // максимальное количество строк/столбцов при котором происходит вывод матрицы newMatrix
    private int numberOfThreads = 4; // количество поток по умолчанию

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        rowsAndColumnsNumberEditText = (EditText) findViewById(R.id.rowsAndColumnsNumberEditText);
        elapsedTime = (TextView) findViewById(R.id.elapsedTime);
        button = (Button) findViewById(R.id.button);
    }



    public void executeBtnClicked(View view){ // когда нажали на кнопку
        button.setVisibility(View.INVISIBLE); // сделал это потому что хотел убрать кнопку во время вычисления, но по-видимому это не работает хз
        initialization(); // инициализация матриц рандомными элементами
        executeThreads();
    }




    private void executeThreads(){
        final int [] initalValue = new int[4]; //начальный индекс матрицы
        final int[] finalValue = new int[4]; // конечный индекс матрицы
        switch (numberOfThreads){ // смотрим какое количество потоков выбрал пользователь (если он ничего не выбрал, то по умолчанию это 4)
            case 1: // если один поток, то умножаем все элементы матрицы
                initalValue[0] = 0;
                finalValue[0] = numberOfRowsAndColumns;
                break;
            case 2: // если два, то разбиваем матрицу на два, чтобы оба потока считали половину матрицы
                initalValue[0] = 0;
                initalValue[1] = numberOfRowsAndColumns/2;
                finalValue[0] = numberOfRowsAndColumns/2;
                finalValue[1] = numberOfRowsAndColumns;
                break;
            case 3: // разбиваем на три
                initalValue[0] = 0;
                initalValue[1] = numberOfRowsAndColumns/3;
                initalValue[2] = (numberOfRowsAndColumns * 2)/3;
                finalValue[0] = numberOfRowsAndColumns/3;
                finalValue[1] = (numberOfRowsAndColumns * 2)/3;
                finalValue[2] = numberOfRowsAndColumns;
                break;
            case 4: //  на 4
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

        final Thread thread = new Thread(new Runnable() { // первый поток
            @Override
            public void run() {
                try {
                    for (int i = 0; i < numberOfRowsAndColumns; i++) { // умножаем все от начала до finalValue[0]
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


        final Thread threadTwo = new Thread(new Runnable() {//второй поток
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


        final Thread threadThree = new Thread(new Runnable() { // третий
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


        final Thread threadFour = new Thread(new Runnable() {  //четвертый
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


        final long startTime = System.nanoTime(); // начинаем отсчитывать время умножения

        switch (numberOfThreads){ // запускаем потоки исходя из того сколько потоков выбрал пользователь
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


        switch (numberOfThreads){ // ждем завершение выполнения всех потоков
            case 1:
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    thread.join();
                    threadTwo.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    thread.join();
                    threadTwo.join();
                    threadThree.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    thread.join();
                    threadTwo.join();
                    threadThree.join();
                    threadFour.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
            long stopTime = System.nanoTime(); //берем конечное время
            elapsedTime.setText("Execution time: " + String.valueOf((double) (stopTime - startTime) / 1000000000L) + " sec");
            matrixOutput(); //  выводим матрицу
            button.setVisibility(View.VISIBLE);

    }






    private void initialization(){
        if(rowsAndColumnsNumberEditText.getText().toString().length() != 0  &&
                !rowsAndColumnsNumberEditText.getText().toString().equals("0")) {
            numberOfRowsAndColumns = Integer.parseInt(rowsAndColumnsNumberEditText.getText().toString()); // проверяем, что пользователь ввел корректное число элементов в эдит текст
            newMatrix = new int[numberOfRowsAndColumns][numberOfRowsAndColumns];
            randomizingMatrix(); // рандомим
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
        if(numberOfRowsAndColumns<= maximumNumberOfRowsToOutput) {
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
    public boolean onOptionsItemSelected(MenuItem item) { // смотрим сколько потоков выбрал пользователь
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

