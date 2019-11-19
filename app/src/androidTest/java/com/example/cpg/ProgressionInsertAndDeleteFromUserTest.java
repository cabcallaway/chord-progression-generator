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
public class ProgressionInsertAndDeleteFromUserTest {

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

        testProgressionInsertThenDelete(userDao);
    }

    public void testProgressionInsertThenDelete(UserDao userDao) {
        List<User> userList = userDao.getAllUsers();

        Progression p = new Progression();
        p.setName("FFmCC");
        p.addChord(new Chord("F", 1));
        p.addChord(new Chord("Fm", 1));
        p.addChord(new Chord("C", 1));
        p.addChord(new Chord("C", 1));

        p.setUserId(userDao.getUserByEmail(userList.get(0).getEmail()).getId());

        // Insert the progression into the db with the given userID
        progressionDao.insert(p);
        // Check that the correct progression was inserted into the correct user
        assertEquals(progressionDao.getAllProgNames(userDao.getUserByEmail(userList.get(0).getEmail()).getId()).get(0), "FFmCC");
        assertNotNull(progressionDao.getAllProgs(userDao.getUserByEmail(userList.get(0).getEmail()).getId()).get(0));

        // Delete progression from database with the given userID
        progressionDao.delete(progressionDao.getAllProgs(userDao.getUserByEmail(userList.get(0).getEmail()).getId()).get(0));
        assertNull(progressionDao.getProgressionByName("FFmCC", userDao.getUserByEmail(userList.get(0).getEmail()).getId()));
    }
}