package com.example.gestionsae;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlCreation extends SQLiteOpenHelper{
	
	private static final String TBL_DATE = "date";
	private static final String TBL_DATE_ID = "did";
	private static final String TBL_DATE_JOUR = "jour";
	
	private static final String TBL_MEMBRE = "membre";
	private static final String TBL_MEMBRE_ID = "mid";
	private static final String TBL_MEMBRE_NOM = "nom";
	private static final String TBL_MEMBRE_PRENOM = "prenom";
	
	private static final String TBL_PRESENCE = "presence";
	private static final String TBL_PRESENCE_ID = "pid";
	private static final String TBL_PRESENCE_FKDATE = "fkdate";
	private static final String TBL_PRESENCE_FKNOM = "fknom";
	
	private static final String CREATE_TBL_DATE = "CREATE TABLE " + TBL_DATE +  " ( "
			+ TBL_DATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TBL_DATE_JOUR + " TEXT NOT NULL UNIQUE "
			+ ");";
	
	private static final String CREATE_TBL_MEMBRE = "CREATE TABLE " + TBL_MEMBRE + " ( "
			+ TBL_MEMBRE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TBL_MEMBRE_NOM + " TEXT NOT NULL, "
			+ TBL_MEMBRE_PRENOM + " TEXT NOT NULL "
			+ ");";
	
	private static final String CREATE_TBL_PRESENCE = "CREATE TABLE " + TBL_PRESENCE + " ( "
			+ TBL_PRESENCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TBL_PRESENCE_FKDATE + " INTEGER NOT NULL, "
			+ TBL_PRESENCE_FKNOM + " INTEGER NOT NULL, "
			+ "FOREIGN KEY(" + TBL_PRESENCE_FKDATE + ") REFERENCES " + TBL_DATE + "(" + TBL_DATE_ID + "), "
			+ "FOREIGN KEY(" + TBL_PRESENCE_FKNOM + ") REFERENCES " + TBL_MEMBRE + "(" + TBL_MEMBRE_ID + ")"
			+ ");";
	
	private static final String CREATE_INDEX_PRESENCE = "CREATE UNIQUE INDEX unicite ON " 
			+ TBL_PRESENCE + "(" + TBL_PRESENCE_FKDATE + "," + TBL_PRESENCE_FKNOM + ");";
	
	private static final String PRAGMA = "PRAGMA foreign_keys=ON;";
	
	public SqlCreation(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	/**
	 * Creation de la base de données gestionsae.db
	 * 3 tables:
	 * date (id, jour)
	 * membre (mid, nom, prenom)
	 * presence (pid, fkdate, fknom)
	 * 
	 * ajout d'un index unique pour supprimer les doublons sur la table presence
	 * 
	 * @param db : String, nom de la base de données
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//On fixe la PRAGMA pour la gestion des clés étrangères
		db.execSQL(PRAGMA);
		
		//On crée les 3 tables.
		db.execSQL(CREATE_TBL_DATE);
		db.execSQL(CREATE_TBL_MEMBRE);
		db.execSQL(CREATE_TBL_PRESENCE);
		db.execSQL(CREATE_INDEX_PRESENCE);
	}
	
	
	/**
	 * onUpgrade 
	 * Suppression des 3 tables pour les recréer.
	 * 
	 * @param db : SQLiteDatabase, nom de la base de données
	 * @param oldVersion : int numéro de l'ancienne version
	 * @param newVersion : int, numéro de la nouvelle version
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		//Suppression des anciennes tables et perte des données
		db.execSQL("DROP TABLE " + TBL_PRESENCE);
		db.execSQL("DROP TABLE " + TBL_MEMBRE);
		db.execSQL("DROP TABLE " + TBL_DATE);
		db.execSQL("DROP INDEX unicite");
		
		//Creation des nouvelles tables vierges
		db.execSQL(CREATE_TBL_DATE);
		db.execSQL(CREATE_TBL_MEMBRE);
		db.execSQL(CREATE_TBL_PRESENCE);
		db.execSQL(CREATE_INDEX_PRESENCE);
	}
}
