package com.example.gestionsae;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;

public class MainActivity extends Activity {
	
	GestionBdd saedb = new GestionBdd(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		saedb.open();
		
		ArrayList<Date> listSeance = new ArrayList<Date>();
		listSeance = saedb.getAllDate();
		
		ArrayAdapter<Date> adapteur = new ArrayAdapter<Date>(this, android.R.layout.simple_list_item_1,listSeance);
		ListView listeSeance = (ListView) findViewById(R.id.listSeance);
		listeSeance.setAdapter(adapteur);
		
		
		Button newSeance = (Button) findViewById(R.id.btNewSeance);
		newSeance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		
		//Définition de la date sous forme yyyy-mm-dd
		Calendar aujourdhui = Calendar.getInstance();
		int btYear = aujourdhui.get(Calendar.YEAR);
		int btMonth = aujourdhui.get(Calendar.MONTH);
		int btDay = aujourdhui.get(Calendar.DAY_OF_MONTH);
		
		return new DatePickerDialog(this, pickerListener, btYear, btMonth, btDay);
	}
	
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			String newSeance = Integer.toString(year) + "-" + Integer.toString(monthOfYear + 1) + "-" + Integer.toString(dayOfMonth);
			saedb.setNewSeance(newSeance);
			
			Intent presence = new Intent(MainActivity.this, PresenceActivity.class);
			startActivity(presence);
		}
	};
}
