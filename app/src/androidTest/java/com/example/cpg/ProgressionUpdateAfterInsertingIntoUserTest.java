package com.example.cpg;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.example.cpg.dao.ProgressionDao;
import com.example.cpg.dao.UserDao;
import com.example.cpg.model.Chord;
import com.example.cpg.model.Progression;
import com.example.cpg.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ProgressionUpdateAfterInsertingIntoUserTest {

    private UserDao userDao;
    private ProgressionDao progressionDao;
    private User user, user2;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.getUserDao();
        progressionDao = db.getProgressionDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testUserInsert() throws Exception {
        user = new User();

        user.setName("john");
        user.setEmail("john@gmail.com");
        user.setPassword("password123");
        userDao.insert(user);

        List<User> userList = userDao.getAllUsers();

        // Check that the inserted users exist
        assertNotNull(userList.get(0).getId());
        assertEquals(userList.get(0).getName(), "john");
        assertEquals(userList.get(0).getEmail(), "john@gmail.com");
        assertEquals(userList.get(0).getPassword(), "password123");

        testProgressionInsertThenUpdate(userDao);
    }

    public void testProgressionInsertThenUpdate(UserDao userDao) {
        List<User> userList = userDao.getAllUsers();
        Chord ch1 = new Chord("F", 1);
        Chord ch2 = new Chord("Fm", 1);
        Chord ch3 = new Chord("C", 1);
        Chord ch4 = new Chord("C", 1);
        Chord ch5 = new Chord("G7", 1);

        Progression p = new Progression();
        p.setName("FFmCC");
        p.addChord(ch1);
        p.addChord(ch2);
        p.addChord(ch3);
        p.addChord(ch4);

        p.setUserId(userDao.getUserByEmail(userList.get(0).getEmail()).getId());

        // Insert the progression into the db with the given userID
        progressionDao.insert(p);
        // Check that the correct progression was inserted into the correct user
        assertEquals(progressionDao.getAllProgNames(userDao.getUserByEmail(userList.get(0).getEmail()).getId()).get(0), "FFmCC");
        assertNotNull(progressionDao.getAllProgs(userDao.getUserByEmail(userList.get(0).getEmail()).getId()).get(0));

        // Update progression in database with the given userID
        p = progressionDao.getProgressionByName("FFmCC", userDao.getUserByEmail(userList.get(0).getEmail()).getId());
        p.removeChord(ch4);
        p.addChord(ch5);
        p.setName("FFmCG7");
        progressionDao.update(p);

        // Check that the progression was correctly updated for the user in the db
        assertEquals(progressionDao.getAllProgNames(userDao.getUserByEmail(userList.get(0).getEmail()).getId()).get(0), "FFmCG7");
    }
}