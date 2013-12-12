package com.example.gestionsae;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		String aujourdhui = dFormat.format(new Date());
		int btYear = Integer.parseInt(aujourdhui.substring(0,3));
		int btMonth = Integer.parseInt(aujourdhui.substring(5, 6));
		int btDay = Integer.parseInt(aujourdhui.substring(8, 9));
		
		return new DatePickerDialog(this, pickerListener, btYear, btMonth, btDay);
	}
	
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			String newSeance = Integer.toString(year) + "-" + Integer.toString(monthOfYear + 1) + "-" + Integer.toString(dayOfMonth);
			saedb.setNewSeance(newSeance);
			
			startActivity(presence);
		}
	};
}
