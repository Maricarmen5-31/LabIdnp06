package com.example.labidnp06;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class PieChartActivity extends AppCompatActivity {

    PieChart pieChart;
    private Button buttonVerBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        pieChart = findViewById(R.id.viewPie);
        randomSet(pieChart);

        buttonVerBar = findViewById(R.id.buttonVerBarChart);
        buttonVerBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBar= new Intent(getApplicationContext(),BarChartActivity.class);
                startActivity(intentBar);
            }
        });
    }

    private void randomSet(PieChart pieView){
        ArrayList<PieDetail> pieHelperArrayList = new ArrayList<PieDetail>();
        ArrayList<Integer> intList = new ArrayList<Integer>();
        int totalNum = 14;

        int totalInt = 0;
        /*for(int i=0; i<totalNum; i++){
            int ranInt = (int)(Math.random()*10)+1;
            intList.add(ranInt);
            totalInt += ranInt;
        }
        for(int i=0; i<totalNum; i++){
            pieHelperArrayList.add(new PieDetail(100f*intList.get(i)/totalInt));
        }

        pieView.selectedPie(PieChart.NO_SELECTED_INDEX);
        pieView.showPercentLabel(true);
        pieView.setDate(pieHelperArrayList);*/

        try {
            InputStream myInput;

            // Inicializar el administrador asset (manager)
            AssetManager assetManager = getAssets();

            // Abrir hoja (sheet) excel
            myInput = assetManager.open("datos.xls");

            // Crear un objeto POI File System (sistema de archivos)
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Creamos a workbook utilizando el sistema de archivos (File System)
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Obtener la primera sheet (hoja) del workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(2);

            // Ahora necesitamos algo para ietarar  a través de las celdas (cells)
            Iterator<Row> rowIter = mySheet.rowIterator();

            int rowno = 0;

            while (rowIter.hasNext()) {
                //Log.e(TAG, " row no "+ rowno );
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno != 0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();

                    int colno = 0;
                    String sno="", date="", det="";

                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0){
                            //sno = myCell.toString();
                        } else if (colno==1){
                            int ranInt = (int)Double.parseDouble(myCell.toString());
                            intList.add(ranInt);
                            totalInt += ranInt;
                        }
                        colno++;
                    }
                }
                rowno++;
            }
            for(int i=0; i<totalNum; i++){
                pieHelperArrayList.add(new PieDetail(100f*intList.get(i)/totalInt));
            }
        } catch (Exception e) {
            //Log.e(TAG, "error "+ e.toString());
        }

        pieView.selectedPie(PieChart.NO_SELECTED_INDEX);
        pieView.showPercentLabel(true);
        pieView.setDate(pieHelperArrayList);
    }

    private void set(PieChart pieView){
        ArrayList<PieDetail> pieHelperArrayList = new ArrayList<PieDetail>();
        pieHelperArrayList.add(new PieDetail(20, Color.BLACK));
        pieHelperArrayList.add(new PieDetail(6));
        pieHelperArrayList.add(new PieDetail(30));
        pieHelperArrayList.add(new PieDetail(12));
        pieHelperArrayList.add(new PieDetail(32));

        pieView.setDate(pieHelperArrayList);
        pieView.setOnPieClickListener(new PieChart.OnPieClickListener() {
            @Override
            public void onPieClick(int index) {
                if(index != PieChart.NO_SELECTED_INDEX) {
                    //textView.setText(index + " selected");
                }else{
                    //textView.setText("No selected pie");
                }
            }
        });
        pieView.selectedPie(2);
    }
}