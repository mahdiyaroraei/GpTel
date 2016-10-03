package ir.parhoonco.traccar.core;

/**
 * Created by Parhoon on 7/24/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ir.parhoonco.traccar.core.model.GPSElement;
import ir.parhoonco.traccar.core.model.InboxItem;
import ir.parhoonco.traccar.core.model.PaymentItem;
import ir.parhoonco.traccar.core.model.Response;
import ir.parhoonco.traccar.core.model.TicketItem;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "traccar.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    private static final String TAG = "Database";

    private static DatabaseHelper dbHelper = null;

    private Context context;

    // DAO
    private Dao<GPSElement, Long> gpsElements;
    private Dao<Response, Long> responses;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (dbHelper == null)
            dbHelper = new DatabaseHelper(context);
        return dbHelper;
    }

    public Dao<GPSElement, Long> getGpsElementsDao() throws SQLException {
        if (gpsElements == null) {
            gpsElements = getDao(GPSElement.class);
        }
        return gpsElements;
    }

    public Dao<Response, Long> getResponsesDao() throws SQLException {
        if (responses == null) {
            responses = getDao(Response.class);
        }
        return responses;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, GPSElement.class);
            TableUtils.createTable(connectionSource, Response.class);
            getGpsElementsDao().create(new GPSElement(4,12656465,1,1,1,37.2605 , 59.6168 , 120 , 361575451351L , 8.7 , 21));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @Override
    public void close() {
        super.close();

        gpsElements = null;
        responses = null;
    }
}
