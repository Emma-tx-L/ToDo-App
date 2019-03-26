package model;

import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TestTaskPhase3 {
    // TODO: design tests for new behaviour added to Task class

    Task testTask;

    @BeforeEach
    void runBefore() {
        testTask = new Task("tester");
    }

    @Test
    void testSameTask() {
        assertEquals(testTask, testTask);
    }

    @Test
    void testSimilarTask() {
        Task newTask = new Task("tester");
        assertEquals(testTask, newTask);
    }

    @Test
    void testSimilarTaskNoNullValues() {
        testTask.setDescription("tester ## tomorrow; tag4; tag5; important; in progress");
        Task newTask = new Task("tester ## tomorrow; tag4; tag5; important; in progress");
        assertEquals(testTask, newTask);
    }

    @Test
    void testDifferentDescription() {
        testTask.setDescription("tester ## tomorrow; tag4; tag5; important; in progress");
        Task newTask = new Task("task1 ## tomorrow; tag4; tag5; important; in progress");
        assertNotEquals(testTask, newTask);
        assertEquals("tester ", testTask.getDescription());
    }

    @Test
    void testDifferentDueDate() {
        testTask.setDescription("tester ## tomorrow; tag4; tag5; important; in progress");
        Task newTask = new Task("tester ## today; tag4; tag5; important; in progress");
        assertNotEquals(testTask, newTask);
    }

    @Test
    void testDifferentPriority() {
        testTask.setDescription("tester ## tomorrow; tag4; tag5; important; in progress");
        Task newTask = new Task("tester ## tomorrow; tag4; tag5; urgent; in progress");
        assertFalse(testTask.equals(newTask)); //TT

        Task newTask2 = new Task("tester ## today; tag4; tag5; important; in progress");
        assertFalse(newTask2.equals(testTask)); //FT

        newTask2.setDescription("tester ## today; tag4; tag5; urgent; in progress");
        assertFalse(newTask2.equals(testTask)); //FF

        newTask2.setDescription("tester ## tomorrow; tag4; tag5; urgent; in progress");
        assertFalse(newTask2.equals(testTask)); //TF

        assertNotEquals(testTask, newTask);
    }

    @Test
    void testDifferentStatus() {
        testTask.setDescription("tester ## tomorrow; tag4; tag5; important; in progress");
        Task newTask = new Task("task1 ## tomorrow; tag4; tag5; important; todo");
        assertNotEquals(testTask, newTask);
    }

    @Test
    void testNullObject() {
        assertNotEquals(testTask, null);
    }

    @Test
    void testAddOneTag() {
        Tag testTag = new Tag("tester");
        Set testSet = new HashSet<Tag>();
        testSet.add(testTag);

        try {
            testTask.addTag(testTag);
            assertEquals(testTask.getTags().size(), 1);
            assertEquals(testSet, testTask.getTags());
        } catch (NullArgumentException e) {
            fail();
        }
    }

    @Test
    void testAddMultipleTags() {
        Tag testTag = new Tag("tester");
        Tag testTag1 = new Tag("tester1");
        Set testSet = new HashSet<Tag>();
        testSet.add(testTag);
        testSet.add(testTag1);

        try {
            testTask.addTag(testTag);
            testTask.addTag(testTag1);
            assertEquals(testTask.getTags().size(), 2);
            assertEquals(testSet, testTask.getTags());
        } catch (NullArgumentException e) {
            fail();
        }
    }

    @Test
    void testAddSameTag() {
        Tag testTag = new Tag("tester");
        Tag testTag1 = new Tag("tester");
        Set testSet = new HashSet<Tag>();
        testSet.add(testTag);

        try {
            testTask.addTag(testTag);
            testTask.addTag(testTag1);
            assertEquals(testTask.getTags().size(), 1);
            assertEquals(testSet, testTask.getTags());
        } catch (NullArgumentException e) {
            fail();
        }
    }

    @Test
    void testAddNullTag() {
        Tag testTag = null;
        Set testSet = new HashSet<Tag>();
        testSet.add(testTag);

        try {
            testTask.addTag(testTag);
            assertEquals(testTask.getTags().size(), 0);
            assertEquals(testSet, testTask.getTags());
            fail();
        } catch (NullArgumentException e) {
            System.out.println("Test passed");
        }
    }

    @Test
    void testRemoveTags() {
        Tag testTag = new Tag("tester");
        Tag testTag1 = new Tag("tester1");
        Tag testTag2 = new Tag("tester2");
        Set testSet = new HashSet<Tag>();
        testSet.add(testTag);
        testSet.add(testTag1);

        try {
            testTask.addTag(testTag);
            testTask.addTag(testTag1);
            assertEquals(testTask.getTags().size(), 2);
            assertEquals(testSet, testTask.getTags());

            testTask.removeTag(testTag);
            testSet.remove(testTag);
            assertEquals(testTask.getTags().size(), 1);
            assertEquals(testSet, testTask.getTags());

            testTask.removeTag("tester2");
            assertEquals(testTask.getTags().size(), 1);
            assertEquals(testSet, testTask.getTags());

            testTask.removeTag(testTag1);
            testSet.remove(testTag1);
            assertEquals(testTask.getTags().size(), 0);
            assertEquals(testSet, testTask.getTags());

        } catch (NullArgumentException e) {
            fail();
        }
    }

    @Test
    void testRemoveNullTag() {
        Tag testTag = null;
        Set testSet = new HashSet<Tag>();
        testSet.add(testTag);

        try {
            testTask.removeTag(testTag);
            assertEquals(testTask.getTags().size(), 0);
            assertEquals(testSet, testTask.getTags());
            fail();
        } catch (NullArgumentException e) {
            System.out.println("Test passed");
        }
    }

    @Test
    void testConstructorThrowExceptionNullDescription() {
        try {
            testTask = new Task(null);
            fail();
        } catch (EmptyStringException e) {
            System.out.println("test passed");
        }
    }

    @Test
    void testConstructorThrowExceptionEmptyString() {
        try {
            testTask = new Task("");
            fail();
        } catch (EmptyStringException e) {
            System.out.println("test passed");
        }
    }

    @Test
    void testSetPriorityExceptionThrown() {
        try {
            testTask.setPriority(null);
            fail();
        } catch (NullArgumentException e) {
            System.out.println("test passed");
        }
    }

    @Test
    void testSetPriority() {
        Priority p = new Priority();
        try {
            testTask.setPriority(p);
        } catch (NullArgumentException e) {
            fail();
        }
    }

    @Test
    void testSetStatusExceptionThrown() {
        try {
            testTask.setStatus(null);
            fail();
        } catch (NullArgumentException e) {
            System.out.println("passed");
        }
    }

    @Test
    void testSetDescriptionExceptionThrownNull() {
        try {
            testTask.setDescription(null);
            fail();
        } catch (EmptyStringException e) {
            System.out.println("passed");
        }
    }

    @Test
    void testSetDescriptionExceptionThrownEmpty() {
        try {
            testTask.setDescription("");
            fail();
        } catch (EmptyStringException e) {
            System.out.println("passed");
        }
    }

    @Test
    void testContainsTagExceptionNull() {
        String n = null;
        try {
            testTask.containsTag(n);
            fail();
        } catch (EmptyStringException e) {
            System.out.println("passed");
        }
    }

    @Test
    void testContainsTagExceptionEmpty() {
        String n = "";
        try {
            testTask.containsTag(n);
            fail();
        } catch (EmptyStringException e) {
            System.out.println("passed");
        }
    }

    @Test
    void testContainsTagString() {
        testTask.addTag("a");
        try {
            assertTrue(testTask.containsTag("a"));
        } catch (EmptyStringException e) {
            fail();
        }
    }

    @Test
    void testToString() {
        testTask.toString();
        testTask = new Task("tester## tomorrow; important; urgent; cpsc210; project");
        System.out.println(testTask.toString());
    }

    @Test
    void testGetters() {
        assertEquals(0, testTask.getEstimatedTimeToComplete());
        assertEquals(0, testTask.getProgress());
    }

    @Test
    void testProgressLessThanZero() {
        try {
            testTask.setProgress(-1);
            fail();
        } catch (InvalidProgressException e) {
            System.out.println("passed");
        }
    }

    @Test
    void testProgressGreaterThanHundred() {
        try {
            testTask.setProgress(300);
            fail();
        } catch (InvalidProgressException e) {
            System.out.println("passed");
        }
    }

    @Test
    void testProgress() {
        try {
            testTask.setProgress(5);
            assertEquals(5, testTask.getProgress());
        } catch (InvalidProgressException e) {
            fail();
        }
    }

    @Test
    void testSetETCTimeNegative() {
        try {
            testTask.setEstimatedTimeToComplete(-1);
            fail();
        } catch (NegativeInputException e) {
            System.out.println("passed");
        }
    }

    @Test
    void testSetETCTime() {
        try {
            testTask.setEstimatedTimeToComplete(100);
            assertEquals(100, testTask.getEstimatedTimeToComplete());
        } catch (NegativeInputException e) {
            fail();
        }
    }
}