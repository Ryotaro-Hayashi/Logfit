package com.hysrtr.logfit

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

// テーブルの定義
object PhysicalRecordContract {
    object PhysicalRecordEntry : BaseColumns {
        const val TABLE_NAME = "physicalRecord"
        const val COLUMN_NAME_BODY_WEIGHT = "bodyWeight"
        const val COLUMN_NAME_BODY_FAT_PERCENTAGE = "bodyFatPercentage"
        const val COLUMN_NAME_SKELETAL_MUSCLE_PERCENTAGE = "skeletalMusclePercentage"
        const val COLUMN_NAME_BASAL_METABOLIC_RATE = "basalMetabolicRate"
        const val COLUMN_NAME_BITMAP = "bitmap"
        const val COLUMN_NAME_CREATED_AT = "createdAt"
    }
}

// テーブル作成のSQL
private const val SQL_CREATE_TABLE = "CREATE TABLE ${PhysicalRecordContract.PhysicalRecordEntry.TABLE_NAME}" +
        "(${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "${PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_WEIGHT} TEXT, " +
        "${PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BODY_FAT_PERCENTAGE} TEXT, " +
        "${PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_SKELETAL_MUSCLE_PERCENTAGE} TEXT, " +
        "${PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BASAL_METABOLIC_RATE} TEXT, " +
        "${PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_BITMAP} BLOB, " +
        "${PhysicalRecordContract.PhysicalRecordEntry.COLUMN_NAME_CREATED_AT} TIMESTAMP DEFAULT (DATETIME('now','localtime')))"

// テーブル削除のSQL
private const val SQL_DELETE_PHYSICAL_RECORD = "DROP TABLE IF EXISTS ${PhysicalRecordContract.PhysicalRecordEntry.TABLE_NAME}"


class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // テーブル作成
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // テーブルがあれば、削除して作り直す
        db?.execSQL(SQL_DELETE_PHYSICAL_RECORD)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Logfit.db"
    }
}