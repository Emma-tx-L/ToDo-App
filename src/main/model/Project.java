package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;

import java.util.*;

// Represents a Project, a collection of zero or more Tasks
// Class Invariant: no duplicated task; order of tasks is preserved
public class Project extends Todo implements Iterable<Todo> {
    private List<Todo> tasks;
    
    // MODIFIES: this
    // EFFECTS: constructs a project with the given description
    //     the constructed project shall have no tasks.
    //  throws EmptyStringException if description is null or empty
    public Project(String description) {
        super(description);
        tasks = new ArrayList<>();
    }
    
    // MODIFIES: this
    // EFFECTS: task is added to this project (if it was not already part of it)
    //   throws NullArgumentException when task is null
    public void add(Todo task) {
        if (task == null) {
            throw new NullArgumentException();
        }

        if (!contains(task) && !task.equals(this)) {
            tasks.add(task);
        }
    }
    
    // MODIFIES: this
    // EFFECTS: removes task from this project
    //   throws NullArgumentException when task is null
    public void remove(Todo task) {
        if (task == null) {
            throw new NullArgumentException();
        }

        if (contains(task)) {
            tasks.remove(task);
        }
    }

    @Override
    public int getEstimatedTimeToComplete() {
        int counter = 0;
        for (Todo t : tasks) {
            if (t instanceof Task) {
                counter = counter + t.getEstimatedTimeToComplete();
            } else {
                Project p = (Project) t;
                counter = counter + p.getEstimatedTimeToComplete();
            }
        }
        return counter;
    }

    // EFFECTS: returns an unmodifiable list of tasks in this project.
    @Deprecated
    public List<Task> getTasks() {
        throw new UnsupportedOperationException();
    }

/*    // EFFECTS: returns an integer between 0 and 100 which represents
    //     the percentage of completed tasks (rounded down to the closest integer).
    //     returns 100 if this project has no tasks!
    public int getProgress() {
        int numerator = getNumberOfCompletedTasks();
        int denominator = getNumberOfTasks();
        if (numerator == denominator) {
            return 100;
        } else {
            return (int) Math.floor(numerator * 100.0 / denominator);
        }
    }*/

    // EFFECTS: returns an integer between 0 and 100 which represents
//     the percentage of completion (rounded down to the nearest integer).
//     the value returned is the average of the percentage of completion of
//     all the tasks and sub-projects in this project.
    public int getProgress() {
        int numerator = 0;
        int denominator = 0;

        for (Todo t : tasks) {
            if (t instanceof Task) {
                Task x = (Task) t;
                denominator++;
                numerator = numerator + x.getProgress();
            } else {
                Project p = (Project) t;
                denominator++;
                numerator = numerator + p.getProgress();
            }
        }

        if (denominator == 0) {
            return 0;
        }

        return (int) Math.floor(numerator / denominator);
    }

    public int getSumOfTaskProgress() {
        int counter = 0;
        for (Todo t : tasks) {
            if (t instanceof Task) {
                counter =  counter + t.getProgress();
            } else {
                Project p = (Project) t;
                counter = counter + p.getSumOfTaskProgress();
            }
        }
        return counter;
    }

    // EFFECTS: returns the number of tasks (and sub-projects) in this project
    public int getNumberOfTasks() {
        return tasks.size();
    }
/*    public int getNumberOfTasks() {
        int counter = 0;
        for (Todo t : tasks) {
            if (t instanceof Task) {
                counter++;
            } else {
                Project p = (Project) t;
                counter = counter + p.getNumberOfTasks();
            }
        }
        return counter;
    }*/

    // EFFECTS: returns true if every task (and sub-project) in this project is completed, and false otherwise
//     If this project has no tasks (or sub-projects), return false.
    public boolean isCompleted() {
        return getNumberOfTasks() != 0 && getProgress() == 100;
    }
    
    // EFFECTS: returns true if this project contains the task
    //   throws NullArgumentException when task is null
    public boolean contains(Todo task) {
        if (task == null) {
            throw new NullArgumentException("Illegal argument: task is null");
        }
        return tasks.contains(task);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        return Objects.equals(description, project.description);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public Iterator<Todo> iterator() {
        return new ProjectIterator();
    }

    private class ProjectIterator implements Iterator<Todo> {
        //the priority level currently iterating over
        int level = 1;
        int cursor = 0;
        List<Integer> sent = new ArrayList();

        //iterator has more elements when not all the todos have been sent
        @Override
        public boolean hasNext() {
            return sent.size() < tasks.size();
        }

        @Override
        public Todo next() {
            if (hasNext()) {
                //if we already looked at this position, move on
                if (sent.contains(cursor)) {
                    cursor = incrementCursor();
                    return next();
                } else if (tasks.get(cursor).getPriority().equals(new Priority(level))) {
                    //if the task at the current position has the same priority as the current level
                    //save that task to return, add the position to sent, and increment cursor
                    return returnNextTodo();
                } else { //if the task at the current position is not at the priority level
                    if (cursor >= tasks.size() - 1) { //if we are at the end of the todos
                        //look for the next priority, reset the cursor, and return the result of that
                        cursor = resetPosition();
                        return next();
                    } else {
                        //else this task is not what we're looking for, increment position and keep looking
                        cursor = incrementCursor();
                        return next();
                    }
                }
            }
            throw new NoSuchElementException();
        }

        private void addPositionToSent(int pos) {
            if (!sent.contains(pos)) {
                sent.add(pos);
            }
        }

        //returns the first position that has not already been visited and increases level by 1
        private int resetPosition() {
            level++;
            for (int i = 0; i <= tasks.size() - 1; i++) {
                if (!sent.contains(i)) {
                    return i;
                }
            }
            //this should never hit
            return 0;
        }

        //increments cursor by 1 if not at the end of the list, if at the end then reset the position
        private int incrementCursor() {
            if (cursor >= tasks.size() - 1) {
                return resetPosition();
            } else {
                return cursor + 1;
            }
        }

        //if the task at the current position has the same priority as the current level
        //save that task to return, add the position to sent, and increment cursor
        private Todo returnNextTodo() {
            Todo next = tasks.get(cursor);
            addPositionToSent(cursor);
            cursor = incrementCursor();
            return next;
        }
    }
}