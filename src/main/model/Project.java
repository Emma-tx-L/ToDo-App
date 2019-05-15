package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;

import java.util.*;

// Represents a Project, a collection of zero or more Tasks
// Class Invariant: no duplicated task; order of tasks is preserved
public class Project extends Todo implements Iterable<Todo>, Observer {
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
            task.addObserver(this);
            task.etcChanged(task.getEstimatedTimeToComplete());
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
            task.deleteObserver(this);
            this.etcChanged(-task.getEstimatedTimeToComplete());
            tasks.remove(task);
        }
    }

/*    @Override
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
    }*/

    //EFFECTS: returns the estimated time to completion of this based on all sub-projects/sub-tasks
    @Override
    public int getEstimatedTimeToComplete() {
        return etcHours;
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

    //EFFECTS: returns sum of progress of ALL sub-projects and sub-tasks
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

/*    public boolean reallyContains(Todo task) {
        boolean contains = false;
        if (task == null) {
            throw new NullArgumentException("Illegal argument: task is null");
        }
        for (Todo t : tasks) {
            if (task instanceof Task) {
                if (tasks.contains(task)) {
                    contains = true;
                }
            } else {
                Project p = (Project) t;
                p.reallyContains(task);
            }
        }
        return contains;
    }*/
    
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

    @Override
    public void update(Observable o, Object arg) {
        if (this.countObservers() >= 0) {
            this.etcChanged((int) arg);
        }
        etcHours = etcHours + (int) arg;

    }

    private class ProjectIterator implements Iterator<Todo> {
        //the priority level currently iterating over
        int level = 1;
        int cursor = 0;
        int sent = 0;


        //EFFECTS: returns true if not all tasks have been returned by the iterator
        @Override
        public boolean hasNext() {
            return !tasks.isEmpty() && sent < tasks.size();
        }

        //MODIFIES: this
        //EFFECTS: iterates through tasks in order of decreasing priority levels
        @Override
        public Todo next() {
            if (hasNext()) {
                if (tasks.get(cursor).getPriority().equals(new Priority(level))) {
                    return returnNextTodo();
                } else {
                    if (cursor >= tasks.size() - 1) {
                        level++;
                        cursor = 0;
                        return next();
                    } else {
                        cursor = incrementCursor();
                        return next();
                    }
                }
            }
            throw new NoSuchElementException();
        }

        //MODIFIES: this
        //EFFECTS: increments cursor by 1 if not at the end of the list, if at the end then reset the position
        private int incrementCursor() {
            if (cursor >= tasks.size() - 1) {
                level++;
                cursor = 0;
            } else {
                return cursor + 1;
            }
            //this should never hit
            return cursor;
        }

        //MODIFIES: this
        //EFFECTS:  if the task at the current position has the same priority as the current level
        //          save that task to return, add the position to sent, and increment cursor
        private Todo returnNextTodo() {
            Todo next = tasks.get(cursor);
            cursor = incrementCursor();
            sent++;
            return next;
        }
    }
}