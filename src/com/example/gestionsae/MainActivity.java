package com.example.gestionsae;

import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	ListView listSeance;
	ArrayList<Date> lSeance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		saedb.open();
		
		afficheDate();
		
		listSeance.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
				
				Date choixSeance = lSeance.get(position);
				long idSeance = choixSeance.getId();
				String dateSeance = choixSeance.getJour();
				
				Intent presence = new Intent(MainActivity.this, PresentActivity.class);
				presence.putExtra("idSeance", idSeance);
				presence.putExtra("jourSeance", dateSeance);
				startActivity(presence);
			}
		});
		
		
		Button newSeance = (Button) findViewById(R.id.btNewSeance);
		newSeance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});
	}
	
	
	
	private void afficheDate() {
		lSeance = new ArrayList<Date>();
		lSeance = saedb.getAllDate();
		
		ArrayAdapter<Date> adapteur = new ArrayAdapter<Date>(this, android.R.layout.simple_list_item_1,lSeance);
		listSeance = (ListView) findViewById(R.id.listSeance);
		listSeance.setAdapter(adapteur);
	}


	/**
	 * onCreateDialog : Paramétrage de la boite de dialogue pour définir une nouvelle date
	 * Insertion de la date dans la table date
	 * Passage à l'activité SelectionActivity avec comme paramétre le date sous forme de long
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		
		//Définition de la date sous forme yyyy-mm-dd
		Calendar aujourdhui = Calendar.getInstance();
		int btYear = aujourdhui.get(Calendar.YEAR);
		int btMonth = aujourdhui.get(Calendar.MONTH);
		int btDay = aujourdhui.get(Calendar.DAY_OF_MONTH);
		
		/*SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		String aujourdhui = dFormat.format(new java.util.Date());
		String sYear = aujourdhui.substring(0, 4);
		String sMonth = aujourdhui.substring(5, 7);
		String sDay = aujourdhui.substring(8, 10);
		
		int btYear = Integer.parseInt(sYear);
		int btMonth = Integer.parseInt(sMonth) - 1;
		int btDay = Integer.parseInt(sDay);*/
		
		return new DatePickerDialog(this, pickerListener, btYear, btMonth, btDay);
	}
	
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			
			//Enregistrement de la date au format 2013-01-01 en gardant les zéros.
			String newSeance = Integer.toString(year);
			String goodMonth = (monthOfYear < 9) ? "0" + Integer.toString(monthOfYear + 1) : Integer.toString(monthOfYear + 1);
			String goodDay = (dayOfMonth < 10) ? "0" + Integer.toString(dayOfMonth) : Integer.toString(dayOfMonth);
			newSeance = newSeance + "-" + goodMonth + "-" + goodDay;
			saedb.setNewSeance(newSeance);
			
			
			//Pour afficher la bonne liste de date au retour dans cette vue
			afficheDate();
			
			//Passage du paramétre id du jour date.id
			long rowSeance = saedb.getDateId(newSeance);
			
			Intent presence = new Intent(MainActivity.this, SelectionActivity.class);
			presence.putExtra("idSeance", rowSeance);
			presence.putExtra("jourSeance", newSeance);
			startActivity(presence);
		}
	};
}
