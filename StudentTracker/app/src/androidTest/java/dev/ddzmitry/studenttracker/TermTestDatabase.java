package dev.ddzmitry.studenttracker;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import dev.ddzmitry.studenttracker.database.AppDatabase;
import dev.ddzmitry.studenttracker.database.TermDAO;
import dev.ddzmitry.studenttracker.models.Term;
import dev.ddzmitry.studenttracker.utilities.SampleData;
import static org.junit.Assert.assertEquals;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */
@RunWith(AndroidJUnit4.class)
public class TermTestDatabase {

    public static final  String TAG = "Junit FOR TERMS TEST";
    private AppDatabase allDatabase;
    private TermDAO termDAO;

    @Before
    public void createDb(){
        // get contect
        Context context = InstrumentationRegistry.getTargetContext();
        // build temp db
        allDatabase = Room.inMemoryDatabaseBuilder(context,AppDatabase.class).build();
        termDAO = allDatabase.termDAO();
        Log.i(TAG, "Database was Created");
    }

    @After
    public void closeDb(){
        allDatabase.close();
        Log.i(TAG,"Database was Closed");
    }

    @Test
    public void  createAndRetrieveTerms(){
        termDAO.insertAllTerms(SampleData.getSampleTerms());
        Term TermOG = SampleData.getSampleTerms().get(0);
        Term TermDB = termDAO.getTermById(1);
        assertEquals(TermOG.getTerm_title(),TermDB.getTerm_title());
        assertEquals(1,TermDB.getTerm_id());
        Term TermDBId2 = termDAO.getTermById(2);

        assertEquals(TermDBId2.getTerm_title(),SampleData.getSampleTerms().get(1).getTerm_title());

    }

    @Test
    public void compareSizes(){
        termDAO.insertAllTerms(SampleData.getSampleTerms());
        Term termToDelete = termDAO.getTermById(1);
        // Before deletion
        assertEquals(3,termDAO.getTermsCount());
        // Delete Term
        termDAO.deleteTerm(termToDelete);
        assertEquals(2,termDAO.getTermsCount());

    }

    @Test
    public void makeSureThatTableWillBeCleaned(){
        termDAO.insertAllTerms(SampleData.getSampleTerms());
        assertEquals(3,termDAO.getTermsCount());
        termDAO.deleteAllTerms();
        assertEquals(0,termDAO.getTermsCount());

    }

}
