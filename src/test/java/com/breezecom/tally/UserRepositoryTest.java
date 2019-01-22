package com.breezecom.tally;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.breezecom.tally.business.repo.SystemParameterRepository;
import com.breezecom.tally.model.SystemParameter;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private SystemParameterRepository userRepository;
    @Before
    public void setUp() throws Exception {
        SystemParameter user1= new SystemParameter("Alice", "23");
        SystemParameter user2= new SystemParameter("Bob", "38");
        //save user, verify has ID value after save
//        assertNull(user1.getName());
//        assertNull(user2.getName());//null before save
        this.userRepository.save(user1);
        this.userRepository.save(user2);
//        assertNotNull(user1.getName());
//        assertNotNull(user2.getName());
    }

    @Test
    public void testFetchData(){
        /*Test data retrieval*/
        SystemParameter userA = userRepository.findByName("Bob");
        assertNotNull(userA);
        assertEquals("38", userA.getValue());
        /*Get all products, list should only have two*/
        Iterable<SystemParameter> users = userRepository.findAll();
        int count = 0;
        // Count users but don't need to use
        for(@SuppressWarnings("unused") SystemParameter u : users){
            count++;
            System.out.println("very good");
        }
//        assertEquals(count, 2);
    }
}