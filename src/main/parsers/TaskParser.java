package parsers;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

// Represents Task parser
public class TaskParser {

    // EFFECTS: iterates over every JSONObject in the JSONArray represented by the input
    // string and parses it as a task; each parsed task is added to the list of tasks.
    // Any task that cannot be parsed due to malformed JSON data is not added to the
    // list of tasks.
    // Note: input is a string representation of a JSONArray
    public List<Task> parse(String input) {
        JSONArray taskArray = new JSONArray(input);
        List<Task> tasks = new ArrayList<>();

        for (Object o : taskArray) {
            JSONObject taskJ = (JSONObject) o;

            Task task = parseTask(taskJ);

            if (task != null) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    //EFFECTS: parses a single task, returns null if task has any missing or incorrectly represented data
    public Task parseTask(JSONObject taskJ) {

        if (!isValidStringComponent(taskJ, "description") || !checkDueDate(taskJ) || !checkPriority(taskJ)
                || !checkStatus(taskJ) || !checkTags(taskJ)) {
            return null;
        }
        Task task = new Task(taskJ.getString("description"));

        task.setDueDate(parseDueDate(taskJ));
        task.setPriority(parsePriority(taskJ));
        task.setStatus(parseStatus(taskJ));
        parseTags(taskJ, task);
        return task;
    }

    public Boolean checkTags(JSONObject taskJ) {
        return (taskJ.has("tags") && (taskJ.get("tags") instanceof JSONArray) && !taskJ.isNull("tags"));
    }

    public void parseTags(JSONObject taskJ, Task task) {
        JSONArray tagsJ = taskJ.getJSONArray("tags");
        for (Object t : tagsJ) {
            if (isValidStringComponent((JSONObject) t, "name")) {
                task.addTag(((JSONObject) t).getString("name"));
            }
        }
    }

    public DueDate parseDueDate(JSONObject taskJ) {
        if (taskJ.isNull("due-date")) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, taskJ.getJSONObject("due-date").getInt("year"));
        cal.set(Calendar.MONTH, taskJ.getJSONObject("due-date").getInt("month"));
        cal.set(Calendar.DAY_OF_MONTH, taskJ.getJSONObject("due-date").getInt("day"));
        cal.set(Calendar.HOUR_OF_DAY, taskJ.getJSONObject("due-date").getInt("hour"));
        cal.set(Calendar.MINUTE, taskJ.getJSONObject("due-date").getInt("minute"));
        Date date = cal.getTime();

        return new DueDate(date);
    }

    //EFFECTS: returns true if due date is valid, else false
    public boolean checkDueDate(JSONObject taskJ) {
        JSONObject date;

        if (!taskJ.has("due-date")) {
            return false;
        }

        if (taskJ.isNull("due-date")) {
            return true;
        }

        date = (JSONObject) taskJ.get("due-date");

        return (isValidIntComponent(date, "year") && isValidIntComponent(date, "month")
                && isValidIntComponent(date, "day") && isValidIntComponent(date, "hour")
                && isValidIntComponent(date, "minute"));
    }

    public Boolean isValidIntComponent(JSONObject obj, String key) {
        return (obj.has(key) && (obj.get(key) instanceof Integer) && !obj.isNull(key));
    }

    public Boolean isValidJsonObjectComponent(JSONObject obj, String key) {
        return (obj.has(key) && (obj.get(key) instanceof JSONObject) && !obj.isNull(key));
    }

    public Boolean isValidStringComponent(JSONObject obj, String key) {
        return (obj.has(key) && (obj.get(key) instanceof String) && !obj.isNull(key));
    }

    public Boolean isValidBooleanComponent(JSONObject obj, String key) {
        return (obj.has(key) && (obj.get(key) instanceof Boolean) && !obj.isNull(key));
    }

    public Boolean checkPriority(JSONObject taskJ) {
        if (!isValidJsonObjectComponent(taskJ, "priority")) {
            return false;
        } else {
            JSONObject p = taskJ.getJSONObject("priority");
            return isValidBooleanComponent(p, "urgent") && isValidBooleanComponent(p, "important");
        }
    }

    public Priority parsePriority(JSONObject taskJ) {
        Priority priority = new Priority();
        priority.setImportant(taskJ.getJSONObject("priority").getBoolean("important"));
        priority.setUrgent(taskJ.getJSONObject("priority").getBoolean("urgent"));

        return priority;
    }

    public Boolean checkStatus(JSONObject taskJ) {
        if (!isValidStringComponent(taskJ, "status")) {
            return false;
        }
        String s = taskJ.getString("status");
        return (s.equals("UP_NEXT") || s.equals("IN_PROGRESS")
                || s.equals("DONE") || s.equals("TODO"));
    }

    public Status parseStatus(JSONObject taskJ) {
        String statusS = taskJ.getString("status");
        Status status = Status.TODO;

        if (statusS.equals("UP_NEXT")) {
            status = Status.UP_NEXT;
        } else if (statusS.equals("IN_PROGRESS")) {
            status = Status.IN_PROGRESS;
        } else if (statusS.equals("DONE")) {
            status = Status.DONE;
        }

        return status;
    }
}
