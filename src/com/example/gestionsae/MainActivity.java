package com.example.gestionsae;

import java.util.ArrayList;

import android.os.Bundle;
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
		
		afficheSeance();
		
		
	}

	private void afficheSeance() {
		
		
	}
}
