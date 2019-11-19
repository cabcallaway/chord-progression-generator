package com.example.cpg;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.example.cpg.dao.UserDao;
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
public class UserInsertAndDeleteTest {

    private UserDao userDao;
    private User user, user2;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.getUserDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testUserInsert() throws Exception {
        user = new User();
        user2 = new User();

        user.setName("john");
        user.setEmail("john@gmail.com");
        user.setPassword("password123");
        userDao.insert(user);

        user2.setName("dave");
        user2.setEmail("dave@gmail.com");
        user2.setPassword("password1234");
        userDao.insert(user2);

        List<User> userList = userDao.getAllUsers();

        // Check that the inserted users exist
        assertNotNull(userList.get(0).getId());
        assertNotNull(userList.get(1).getId());

        assertEquals(userList.get(0).getName(), "john");
        assertEquals(userList.get(1).getName(), "dave");

        assertEquals(userList.get(0).getEmail(), "john@gmail.com");
        assertEquals(userList.get(1).getEmail(), "dave@gmail.com");

        assertEquals(userList.get(0).getPassword(), "password123");
        assertEquals(userList.get(1).getPassword(), "password1234");

        testUserDelete(userDao);
    }

    public void testUserDelete(UserDao userDao) {
        List<User> userList = userDao.getAllUsers();

        userDao.delete(userDao.getUserByEmail(userList.get(1).getEmail()));
        assertNotNull(userDao.getUserByEmail(userList.get(0).getEmail()));
        assertNull(userDao.getUserByEmail(userList.get(1).getEmail()));

        userDao.delete(userDao.getUserByEmail(userList.get(0).getEmail()));
        assertNull(userDao.getUserByEmail(userList.get(0).getEmail()));
        assertNull(userDao.getUserByEmail(userList.get(1).getEmail()));
    }
}
