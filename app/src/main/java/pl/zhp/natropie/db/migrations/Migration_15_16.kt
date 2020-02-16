package pl.zhp.natropie.db.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

object Migration_15_16 : Migration(15, 16) {
    /**
     * Should run the necessary migrations.
     *
     *
     * This class cannot access any generated Dao in this method.
     *
     *
     * This method is already called inside a transaction and that transaction might actually be a
     * composite transaction of all necessary `Migration`s.
     *
     * @param database The database instance
     */
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `clipboard_items` " +
                    "( " +
                    " `id` INTEGER, " +
                    " `post_id` INTEGER," +
                    " FOREIGN KEY(post_id) REFERENCES posts(id)," +
                    " PRIMARY KEY(`id`)" +
                    " )"
        )
    }

}