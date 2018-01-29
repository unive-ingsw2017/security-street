package com.giacomodeliberali.securitystreet;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.giacomodeliberali.securitystreet.models.dtos;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class DataView extends AppCompatActivity {

    public final static String CURRENT_CRASH_ID="CurrentCrashIdParamaterInActivityExtra";

    private String descriptionCity="Qui di seguito verranno riportati alcuni dati degli incidenti avvenuti nell'anno corrente nella provincia di ";
    private String descriptionUD1="Nella provincia di ";
    private String descriptionUD2=" secondo i dati si stima che su 100 persone coinvolte negli incidenti ";
    private String descriptionUD3=" sono uomini e ";
    private String descriptionUD4=" sono donne.";
    private String descriptionMI1="Nella provincia di ";
    private String descriptionMI2=" secondo i dati si stima che su 100 incidenti ";
    private String descriptionMI3=" sono mortali.";
    private String descriptionFM1="Nella provincia di ";
    private String descriptionFM2=" secondo i dati si stima che su 100 persone coinvolte negli'incidenti ";
    private String descriptionFM3=" non sono riuscite a sopravvivere";
    private String[] xUD={"Uomini","Donne"};
    private String[] xMI={"Incidenti","Mortali"};
    private String[] xFM={"Feriti","Morti"};
    PieChart pieChartUD;
    PieChart pieChartMI;
    PieChart pieChartFM;
    TextView nameCity;
    TextView cityDescription;
    TextView maleFemaleDescription;
    TextView mortalityDescription;
    TextView deathDescription;

    dtos.CrashDto crashDtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);
        Intent i = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });

        crashDtos =(dtos.CrashDto) i.getSerializableExtra(CURRENT_CRASH_ID);;

        String name=crashDtos.getState();
        float[] yUD={crashDtos.getMen(),crashDtos.getFemales()};                //il primo è riferito agli uomini il secondo alle donne
        float[] yMI={crashDtos.getCrashes(),crashDtos.getDeadlyCrashes()};      //incidenti - mortali
        float[] yFM={crashDtos.getInjuried(),crashDtos.getDead()};            //feriti - morti

        //Non toccare

        ArrayList<Integer> colorsUD=new ArrayList<>();
        ArrayList<Integer> colorsMI=new ArrayList<>();
        ArrayList<Integer> colorsFM=new ArrayList<>();
        Description des=new Description();

        des.setText("");
        pieChartUD=(PieChart)findViewById(R.id.idPieChart);
        pieChartMI=(PieChart)findViewById(R.id.idPieChart2);
        pieChartFM=(PieChart)findViewById(R.id.idPieChart3);
        nameCity=(TextView)findViewById(R.id.textViewNameCity);
        cityDescription=(TextView)findViewById(R.id.textViewDescriptionCity);
        maleFemaleDescription=(TextView)findViewById(R.id.textViewMaleFemaleDescription);
        mortalityDescription=(TextView)findViewById(R.id.textViewMortalityDescription);
        deathDescription=(TextView)findViewById(R.id.textViewDeathDescription);
        colorsUD.add(Color.BLUE);
        colorsUD.add(Color.RED);
        colorsMI.add(Color.GREEN);
        colorsMI.add(Color.YELLOW);
        colorsFM.add(Color.CYAN);
        colorsFM.add(Color.MAGENTA);

        nameCity.setText(name);
        cityDescription.setText(descriptionCity+name);
        maleFemaleDescription.setText(descriptionUD1+name+descriptionUD2+Math.round(100*yUD[0]/(yUD[0]+yUD[1]))+descriptionUD3+Math.round(100*yUD[1]/(yUD[0]+yUD[1]))+descriptionUD4);
        mortalityDescription.setText(descriptionMI1+name+descriptionMI2+Math.round(100*yMI[1]/(yMI[0]+yMI[1]))+descriptionMI3);
        deathDescription.setText(descriptionFM1+name+descriptionFM2+Math.round(100*yFM[1]/(yFM[0]+yFM[1]))+descriptionFM3);

        pieChartFM.setDescription(des);
        pieChartUD.setDescription(des);
        pieChartMI.setDescription(des);

        pieChartFM.setRotationEnabled(false);
        pieChartUD.setRotationEnabled(false);
        pieChartMI.setRotationEnabled(false);

        pieChartFM.setHoleRadius(0f);
        pieChartUD.setHoleRadius(0f);
        pieChartMI.setHoleRadius(0f);

        pieChartFM.setTransparentCircleAlpha(0);
        pieChartUD.setTransparentCircleAlpha(0);
        pieChartMI.setTransparentCircleAlpha(0);

        pieChartFM.setDrawEntryLabels(true);
        pieChartUD.setDrawEntryLabels(true);
        pieChartMI.setDrawEntryLabels(true);

        addDataSet(pieChartFM,xFM,yFM,colorsFM);
        addDataSet(pieChartUD,xUD,yUD,colorsUD);
        addDataSet(pieChartMI,xMI,yMI,colorsMI);
    }

    private void addDataSet(PieChart chart,String[] dataX,float[] dataY,ArrayList<Integer> colors){

        ArrayList<PieEntry> yEntrys=new ArrayList<>();
        ArrayList<String> xEntrys=new ArrayList<>();

        for (int i=0;i<2;i++){
            yEntrys.add(new PieEntry(100*dataY[i]/(dataY[0]+dataY[1]),i));
            xEntrys.add(dataX[i]);
        }

        //create the dataset
        PieDataSet pieDataSet=new PieDataSet(yEntrys,"");
        pieDataSet.setSliceSpace(4);
        pieDataSet.setValueTextSize(20);

        //add colors to dataset

        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData=new PieData(pieDataSet);
        chart.setData(pieData);
        chart.invalidate();
    }
}