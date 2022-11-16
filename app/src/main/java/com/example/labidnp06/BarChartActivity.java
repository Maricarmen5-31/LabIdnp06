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
import java.util.Iterator;

public class BarChartActivity extends AppCompatActivity {

    private BarChart barChart;
    private Button buttonVerPie;

    String TAG ="main";
    private TextView textView;
    //Button btnLeerExcel, btnGuardarExcel;
    //TextView tvDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart = findViewById(R.id.viewBar);
        //capturarDatos(barChart);
        textView = findViewById(R.id.tvDatos);
        LeerDatosExcel(barChart);

        buttonVerPie = findViewById(R.id.buttonVerPieChart);
        buttonVerPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPie= new Intent(getApplicationContext(), PieChartActivity.class);
                startActivity(intentPie);
            }
        });

        /*btnGuardarExcel = findViewById(R.id.button);
        //btnLeerExcel = findViewById(R.id.button);
        tvDatos = findViewById(R.id.tvDatos);

        btnGuardarExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarExcel();
            }
        });*/

    }
    /*public void guardarExcel(){
        Workbook workbook = new HSSFWorkbook();
        Cell cell = null;
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        Sheet sheet = null;
        sheet = workbook.createSheet("Lista de usuarios");

        Row row = null;
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("USUARIO");
        cell.setCellStyle(cellStyle);

        sheet.createRow(1);
        cell = row.createCell(1);
        cell.setCellValue("NOMBRE");
        cell.setCellStyle(cellStyle);

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("xcheko51x");

        cell = row.createCell(1);
        cell.setCellValue("Sergio Peralta");

        File file = new File(getExternalFilesDir(null), "Relacion.xls");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    public void leerExcel(){
        File file = new File(this.getExternalFilesDir(null), "datos.xls");
        FileInputStream inputStream = null;

        String datos = "hola";
        try{
            inputStream = new FileInputStream(file);

            POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream);

            HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);

            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            while (rowIterator.hasNext()) {
                HSSFRow row = (HSSFRow) rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while(cellIterator.hasNext()){
                    HSSFCell cell = (HSSFCell) cellIterator.next();

                    datos = datos+" = "+cell.toString();
                }
                datos = datos+"\n";
            }
            tvDatos.setText(datos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
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
            String etiquetaX = "", etiquetaY = "";

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
                }
                rownum++;
            }
            barView.setBottomTextList(etiquetasList);
            barView.setDataList(barraDataList,50);

        } catch (Exception e) {
            Log.e(TAG, "error "+ e.toString());
        }
    }

    private void capturarDatos(BarChart barView){
        int random = (int)(Math.random()*20)+6;

        // nombres de etiquetas
        ArrayList<String> etiquetasList = new ArrayList<String>();
        for (int i=0; i<random; i++){
            etiquetasList.add("test"+i);
            //test.add("pqg");
            //test.add(String.valueOf(i+1));
        }
        barView.setBottomTextList(etiquetasList);

        // valores de cada barra
        ArrayList<Integer> barraDataList = new ArrayList<Integer>();
        for(int i=0; i<random; i++){
            barraDataList.add((int)(Math.random() * 100));
        }
        barView.setDataList(barraDataList,100);
    }

    public void readExcelFileFromAssets() {
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

            int rowno = 0;
            textView.append("\n");

            while (rowIter.hasNext()) {
                //Log.e(TAG, " row no "+ rowno );
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno == 0) {

                }
                Iterator<Cell> cellIter = myRow.cellIterator();
                int colno = 0;
                String sno="", date="", det="";

                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if (colno==0){
                        sno = myCell.toString();
                    } else if (colno==1){
                        date = myCell.toString();
                    } else if (colno==2){
                        det = myCell.toString();
                    }
                    colno++;
                    //Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                }
                textView.append( sno + " -- "+ date+ "  -- "+ det+"\n");
                rowno++;
            }
        } catch (Exception e) {
            Log.e(TAG, "error "+ e.toString());
        }
    }
}