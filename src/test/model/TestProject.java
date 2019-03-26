package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestProject {
    private Project testProject;

    private Task task1 = new Task("1");
    private Task task2 = new Task("2");

    @BeforeEach
    void runBefore() {
        testProject = new Project("aaa");
    }

    @Test
    void testConstructorNoErrorsThrown() {
        try {
            testProject = new Project("aaa");
            assertEquals("aaa", testProject.getDescription());
            assertEquals(0, testProject.getNumberOfTasks());
        } catch (EmptyStringException e) {
            fail("Threw unexpected EmptyStringException");
        }
    }

    @Test
    void testConstructorEmptyStringExceptionThrown() {
        try {
            testProject = new Project("");
            fail("Did not throw expected EmptyStringException");
        } catch (EmptyStringException e) {
            System.out.println("Test passed");
        }
    }

    @Test
    void testConstructorNullEmptyStringExceptionThrown() {
        try {
            testProject = new Project(null);
        } catch (EmptyStringException e) {
            System.out.println("Test passed");
        }
    }

    @Test
    void testAddNoExceptionThrown() {
        try {
            testProject.add(task1);
            assertEquals(1, testProject.getNumberOfTasks());
            assertTrue(testProject.contains(task1));
            testProject.add(task2);
            assertEquals(2, testProject.getNumberOfTasks());
            assertTrue(testProject.contains(task2));
        } catch (NullArgumentException e) {
            fail("Threw unexpected NullArgumentException");
        }
    }

    @Test
    void testAddNullExceptionThrown() {
        try {
            testProject.add(null);
            fail("Did not throw expected NullArgumentException");
        } catch (NullArgumentException e) {
            System.out.println("Test passed");
        }
    }

    @Test
    void testRemoveNoExceptionsThrown() {
        try {
            testProject.add(task1);
            testProject.add(task2);
            testProject.remove(task2);
            assertEquals(1, testProject.getNumberOfTasks());
            assertTrue(testProject.contains(task1));
            assertFalse(testProject.contains(task2));
            testProject.remove(task1);
            assertEquals(0, testProject.getNumberOfTasks());
            assertFalse(testProject.contains(task1));
        } catch (NullArgumentException e) {
            fail("Threw unexpected NullArgumentException");
        }
    }

    @Test
    void testRemoveExceptionThrown() {
        try {
            testProject.remove(null);
            fail("Did not catch expected NullArgumentException");
        } catch (NullArgumentException e) {
            System.out.println("Test passed");
        }
    }

    @Test
    void testRemoveFromNoTasks() {
        testProject.remove(task2);
        assertEquals(0, testProject.getNumberOfTasks());
        assertFalse(testProject.contains(task2));
    }

    @Test
    void testGetProgressNoTasks() {
        assertEquals(0, testProject.getProgress());
    }

    @Test
    void testGetZeroProgress() {
        testProject.add(task1);
        testProject.add(task2);
        assertEquals(0, testProject.getProgress());
    }

    @Test
    void testGetSomeProgress() {
        testProject.add(task1);
        task1.setStatus(Status.DONE);
        task1.setProgress(100);
        assertEquals(task1.getStatus(), Status.DONE);
        testProject.add(task2);
        assertEquals(50, testProject.getProgress());
    }

    @Test
    void testGetCompletedProgress() {
        task1.setStatus(Status.DONE);
        task2.setStatus(Status.DONE);
        task1.setProgress(100);
        task2.setProgress(100);
        testProject.add(task1);
        testProject.add(task2);
        assertEquals(100, testProject.getProgress());
        assertTrue(testProject.isCompleted());
    }

/*    @Test
    void testIsCompletedTrue() {
        assertFalse(testProject.isCompleted());
        task1.setStatus(Status.DONE);
        task2.setStatus(Status.DONE);
        testProject.add(task1);
        testProject.add(task2);
        assertTrue(testProject.isCompleted());
    }*/

/*    @Test
    void testIsCompletedFalse() {
        task1.setStatus(Status.DONE);
        testProject.add(task1);
        testProject.add(task2);
        assertFalse(testProject.isCompleted());
    }*/

/*    @Test
    void testGetTasks() {
        ArrayList<Task> testTasks = new ArrayList<Task>();
        testTasks.add(task1);
        testTasks.add(task2);
        testProject.add(task1);
        testProject.add(task2);
        assertEquals(testTasks, testProject.getTasks());
    }*/

    @Test
    void testContainsNoErrorThrown() {
        testProject.add(task1);
        try {
            testProject.contains(task1);
        } catch (NullArgumentException e) {
            fail("Threw unexpected NullArgumentException");
        }
    }

    @Test
    void testContainsErrorThrown() {
        testProject.add(task1);
        try {
            testProject.contains(null);
            fail("Did not throw expected NullArgumentException");
        } catch (NullArgumentException e) {
            System.out.println("Test passed");
        }
    }

    @Test
    void testEquals() {
        Project p = new Project("a");
        Project p2 = new Project("a");
        assertTrue(p.equals(p));
        assertFalse(p.equals("s"));
        assertTrue(p.equals(p2));
        p.hashCode();
    }

    @Test
    void testGetNumberOfTasksComposite() {
        Project pa = new Project("Project a");
        Task ta = new Task("Task a");
        Project pb = new Project("Project b");
        Task ba = new Task("Task b");
        Project pc = new Project("Project c");
        Task testTask = new Task("Test task");

        pa.add(ta);
        pb.add(ba);
        pb.add(pc);

        testProject.add(testTask);
        testProject.add(pa);
        testProject.add(ba);

        assertEquals(3, testProject.getNumberOfTasks());
    }

    @Test
    void testGetSumOfTaskProgressComposite() {
        Project pa = new Project("Project a");
        Task ta = new Task("Task a");
        ta.setProgress(50);
        Project pb = new Project("Project b");
        Task ba = new Task("Task b");
        ba.setProgress(20);
        Project pc = new Project("Project c");
        Task testTask = new Task("Test task");
        testTask.setProgress(87);

        pa.add(ta);
        pb.add(ba);
        pb.add(pc);

        testProject.add(testTask);
        testProject.add(pa);
        testProject.add(ba);

        assertEquals(50 + 20 + 87, testProject.getSumOfTaskProgress());
    }

    @Test
    void testRemoveNonexistentTask() {
        Task n = new Task("aa");
        testProject.remove(n);
    }

    @Test
    void testGetEstimatedTimeToComplete() {
        Project pa = new Project("Project a");
        Task ta = new Task("Task a");
        Project pb = new Project("Project b");
        Task ba = new Task("Task b");
        Project pc = new Project("Project c");
        Task testTask = new Task("Test task");
        ta.setEstimatedTimeToComplete(5);
        ba.setEstimatedTimeToComplete(10);
        testTask.setEstimatedTimeToComplete(60);

        pa.add(ta);
        pb.add(ba);
        pb.add(pc);

        testProject.add(testTask);
        testProject.add(pa);
        testProject.add(ba);

        assertEquals(5 + 10 + 60, testProject.getEstimatedTimeToComplete());
    }

    @Test
    void testGetTasksDeprecated() {
        try {
            testProject.getTasks();
            fail();
        } catch (UnsupportedOperationException e) {
            System.out.println("test passed");
        }
    }

    @Test
    void testGivenExampleOneGetProgress() {
        Task task1 = new Task("task1");
        Task task2 = new Task("task2");
        Task task3 = new Task("task3");

        testProject.add(task1);
        testProject.add(task2);
        testProject.add(task3);
        assertEquals(0, testProject.getProgress());

        task1.setProgress(100);
        assertEquals(33, testProject.getProgress());

        task2.setProgress(50);
        task3.setProgress(25);
        assertEquals(58, testProject.getProgress());

        Project p2 = new Project("project 2");
        Task task4 = new Task("task 4");
        p2.add(task4);
        p2.add(testProject);

        assertEquals(29, p2.getProgress());
    }

    @Test
    void testGivenExample2GetETCTime() {
        Task task1 = new Task("task1");
        Task task2 = new Task("task2");
        Task task3 = new Task("task3");

        testProject.add(task1);
        testProject.add(task2);
        testProject.add(task3);
        assertEquals(0, testProject.getEstimatedTimeToComplete());

        task1.setEstimatedTimeToComplete(8);
        assertEquals(8, testProject.getEstimatedTimeToComplete());

        task2.setEstimatedTimeToComplete(2);
        task3.setEstimatedTimeToComplete(10);
        assertEquals(20, testProject.getEstimatedTimeToComplete());

        Project p2 = new Project("project 2");
        Task task4 = new Task("task 4");
        task4.setEstimatedTimeToComplete(4);
        p2.add(task4);
        p2.add(testProject);
        assertEquals(24, p2.getEstimatedTimeToComplete());
    }

    @Test
    void testAddSameTodoAsProject() {
        int i = testProject.getNumberOfTasks();
        testProject.add(testProject);
        assertEquals(i, testProject.getNumberOfTasks());
    }

    @Test
    void testAddSameTodo() {
        Task t1 = new Task("1");

        testProject.add(t1);
        assertEquals(1, testProject.getNumberOfTasks());
        testProject.add(t1);
        assertEquals(1, testProject.getNumberOfTasks());
    }

    @Test
    void testIsCompleted() {
        //tasks = 0, progress = 0 (FF case)
        assertFalse(testProject.isCompleted());

        //tasks = 1, progress = 0 (TF case)
        Task t = new Task("a");
        testProject.add(t);
        assertFalse(testProject.isCompleted());

        //TT case
        t.setProgress(100);
        assertTrue(testProject.isCompleted());
    }
}