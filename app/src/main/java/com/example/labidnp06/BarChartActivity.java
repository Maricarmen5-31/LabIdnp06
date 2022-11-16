package com.example.labidnp06;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class BarChartActivity extends AppCompatActivity {

    private BarChart barChart;
    private Button buttonVerPie;
    private String TAG ="main";
    private TextView textViewX;
    private TextView textViewY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart = findViewById(R.id.viewBar);
        textViewX = findViewById(R.id.textViewEtiquetaX);
        textViewY = findViewById(R.id.textViewEtiquetaY);

        LeerDatosExcel(barChart);

        buttonVerPie = findViewById(R.id.buttonVerPieChart);
        buttonVerPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPie= new Intent(getApplicationContext(), PieChartActivity.class);
                startActivity(intentPie);
            }
        });
    }

    public void LeerDatosExcel(BarChart barView){
        // nombres de etiquetas
        ArrayList<String> etiquetasList = new ArrayList<String>();

        // valores de cada barra
        ArrayList<Integer> barraDataList = new ArrayList<Integer>();

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
            HSSFSheet mySheet = myWorkBook.getSheetAt(1);

            // Ahora necesitamos algo para ietarar  a través de las celdas (cells)
            Iterator<Row> rowIter = mySheet.rowIterator();

            int rownum = 0;

            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if (rownum != 0){
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno = 0;
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0) {
                            etiquetasList.add(myCell.toString());
                        } else if (colno==1) {
                            barraDataList.add((int)Double.parseDouble(myCell.toString()));
                        }
                        colno++;
                    }
                } else {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    textViewX.setText(myCell.toString());
                    myCell = (HSSFCell) cellIter.next();
                    textViewY.setText(myCell.toString());
                }
                rownum++;
            }
            barView.setBottomTextList(etiquetasList);
            barView.setDataList(barraDataList,getMax(barraDataList));

        } catch (Exception e) {
            Log.e(TAG, "error "+ e.toString());
        }
    }

    public int getMax(ArrayList<Integer> array){
        return Collections.max(array);
    }
}