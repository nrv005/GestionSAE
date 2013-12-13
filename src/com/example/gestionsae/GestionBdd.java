package com.example.gestionsae;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GestionBdd {
	
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "gestionsae.db";
	private static final String PRAGMA = "PRAGMA foreign_keys=ON;";
	
	private static final String TBL_DATE = "date";
	private static final String DATE_COL_ID = "did";
	private static final String DATE_COL_JOUR = "jour";
	private static final String DATE_COL_ORDER = "jour DESC";
	
	private static final String TBL_MEMBRE = "membre";
	private static final String MEMBRE_COL_ID = "mid";
	private static final String MEMBRE_COL_NOM = "nom";
	private static final String MEMBRE_COL_PRENOM = "prenom";
	private static final String MEMBRE_COL_ORDER = "nom,prenom";
	
	private static final String TBL_PRESENCE = "presence";
	private static final String PRESENCE_COL_FKDATE = "fkdate";
	private static final String PRESENCE_COL_FKNOM = "fknom";
	
	private SQLiteDatabase bdd;
	private SqlCreation maBaseSql;
	
	public GestionBdd(Context context) {
		
		maBaseSql = new SqlCreation(context, NOM_BDD, null, VERSION_BDD);
	}
	
	/**
	 * Ouvrir la base de données gestionsae.db
	 * 
	 */
	public void open() {
		
		bdd = maBaseSql.getWritableDatabase();
		bdd.execSQL(PRAGMA);
	}
	
	/**
	 * Fermer la base de données gestionsae.db
	 * 
	 */
	public void close() {
		bdd.close();
	}
	
	
	
/////////////////////////////////////////////////////////////////////////////////////////
//Table des dates 
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public ArrayList<Date> getAllDate() {
		Cursor cAllDate = bdd.query(TBL_DATE, null, null, null, null, null, DATE_COL_ORDER);
		return cursorToDate(cAllDate);
	}
	
	private ArrayList<Date> cursorToDate(Cursor cAllDate) {
		
		//Si la table des dates est vide
		if (cAllDate.getCount() == 0) return new ArrayList<Date>(0);
		
		//Récupération des données de toutes les dates
		ArrayList<Date> retAllDate = new ArrayList<Date>(cAllDate.getCount());
		cAllDate.moveToFirst();
		do {
			Date nDate = new Date();
			nDate.setId(cAllDate.getInt(0));
			nDate.setJour(cAllDate.getString(1));
			retAllDate.add(nDate);
		} while (cAllDate.moveToNext());
		
		//Fermer le cursor pour rendre la mémoire puis retourner le résultat
		cAllDate.close();
		return retAllDate;
	}

	public void setNewSeance(String newSeance) {
		
		ContentValues values = new ContentValues();
		values.put(DATE_COL_JOUR, newSeance);
		bdd.insert(TBL_DATE, DATE_COL_JOUR, values);
	}
	
	
	
/////////////////////////////////////////////////////////////////////////////////////////
//Table des membres
/////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * setNewMembre : Ajoute un nouveau membre dans la table correspondate
	 * @param nom : String, nom du nouveau membre
	 * @param prenom : String, prenom du nouveau membre
	 * @return void
	 */
	public void setNewMembre(String nom, String prenom) {
		
		ContentValues values = new ContentValues();
		values.put(MEMBRE_COL_NOM, nom);
		values.put(MEMBRE_COL_PRENOM, prenom);
		bdd.insert(TBL_MEMBRE, MEMBRE_COL_NOM, values);
	}
	
	/**
	 * getAllMembre : Récupère tous les membres de la base de données
	 * @param void
	 * @return cursorToMembre : fonction private pour obtenir un ArrayList du Cursor
	 */
	public ArrayList<Membre> getAllMembre() {
		
		Cursor cAllMembre = bdd.query(TBL_MEMBRE, null, null, null, null, null, MEMBRE_COL_ORDER);
		return cursorToMembre(cAllMembre);
	}

	private ArrayList<Membre> cursorToMembre(Cursor cAllMembre) {
		
		//Si la table des membres est vide
		if (cAllMembre.getCount() == 0) return new ArrayList<Membre>(0);
		
		//Récupère les données de tous les membres dans un ArrayList
		ArrayList<Membre> retAllMembre = new ArrayList<Membre>(cAllMembre.getCount());
		cAllMembre.moveToFirst();
		do {
			Membre nMembre = new Membre();
			nMembre.setId(cAllMembre.getInt(0));
			nMembre.setNom(cAllMembre.getString(1));
			nMembre.setPrenom(cAllMembre.getString(2));
			retAllMembre.add(nMembre);
		} while (cAllMembre.moveToNext());
		
		//Ferme le cursor pour libérer les ressources puis renvoie l'ArrayList
		cAllMembre.close();
		return retAllMembre;
	}
}
