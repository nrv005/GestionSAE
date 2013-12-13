package com.example.gestionsae;

import android.app.Activity;
import android.os.Bundle;

public class PresentActivity extends Activity{
	
	GestionBdd saedb = new GestionBdd(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extra = this.getIntent().getExtras();
		if (extra != null) {
			long idSeance = extra.getLong("idSeance");
			String jourSeance = extra.getString("jourSeance");
		}
		
		setContentView(R.layout.activity_present);
	}
}
