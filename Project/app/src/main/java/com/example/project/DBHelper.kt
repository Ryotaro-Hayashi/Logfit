package com.example.project

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.project.fragment.RecordFragment

// テーブルの定義
object PhysicalRecordTable : BaseColumns {
    const val TABLE_NAME = "physicalRecord"
    const val COLUMN_NAME_BODY_WEIGHT = "bodyWeight"
}

// テーブル作成のSQL
private const val SQL_CREATE_PHYSICAL_RECORD = "CREATE TABLE ${PhysicalRecordTable.TABLE_NAME}" +
        "(${BaseColumns._ID} INTEGER PRIMARY KEY, " +
        "${PhysicalRecordTable.COLUMN_NAME_BODY_WEIGHT} STRING NOT NULL)"

// テーブル削除のSQL
private const val SQL_DELETE_PHYSICAL_RECORD = "DROP TABLE IF EXISTS ${PhysicalRecordTable.TABLE_NAME}"

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "Logfit.db"


class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // テーブル作成
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_PHYSICAL_RECORD)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // テーブルがあれば、削除して作り直す
        db?.execSQL(SQL_DELETE_PHYSICAL_RECORD)
        onCreate(db)
    }

}