package com.example.gestionsae;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PresenceActivity extends Activity{
	
	GestionBdd saedb = new GestionBdd(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pesence);
		
		
		/**
		 * Button.setOnClickListener : Gestion du bouton "Nouveau Membre"
		 * On clique sur le bouton pour afficher une boite de dialogue "R.layout.new_membre"
		 * On obtient le nom et prénom, que l'on rajoute dans la table membre nom,prenom
		 */
		Button newMembre = (Button) findViewById(R.id.btPresenceNewMembre);
		newMembre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				LayoutInflater inflater = getLayoutInflater();
				View dialogLayout = inflater.inflate(R.layout.new_membre, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(PresenceActivity.this);
				builder.setView(dialogLayout);
				
				builder.setPositiveButton("Enregister", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						EditText newNom = (EditText) findViewById(R.id.editNom);
						String nom = newNom.getText().toString();
						EditText newPrenom = (EditText) findViewById(R.id.editPrenom);
						String prenom = newPrenom.getText().toString();
						saedb.setNewMembre(nom, prenom);
						// TODO Auto-generated method stub
						
					}
				});
				
				builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
			}
		});
	}

}
