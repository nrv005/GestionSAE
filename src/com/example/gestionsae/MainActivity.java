package com.example.gestionsae;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		GestionBdd saedb = new GestionBdd(this);
		saedb.open();
		
		ArrayList<Date> listSeance = new ArrayList<Date>();
		listSeance = saedb.getAllDate();
		
		ArrayAdapter<Date> adapteur = new ArrayAdapter<Date>(this, android.R.layout.simple_list_item_1,listSeance);
		ListView listeSeance = (ListView) findViewById(R.id.listSeance);
		listeSeance.setAdapter(adapteur);
		
	}
}
