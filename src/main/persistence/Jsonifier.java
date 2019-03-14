package persistence;


import model.DueDate;
import model.Priority;
import model.Tag;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

// Converts model elements to JSON objects
public class Jsonifier {

    // EFFECTS: returns JSON representation of tag
    public static JSONObject tagToJson(Tag tag) {
        JSONObject tagJson = new JSONObject();

        tagJson.put("name", tag.getName());

        return tagJson;
    }

    // EFFECTS: returns JSON representation of priority
    public static JSONObject priorityToJson(Priority priority) {
        JSONObject priorityJson = new JSONObject();

        boolean isImportant = priority.isImportant();
        boolean isUrgent = priority.isUrgent();

        priorityJson.put("important", isImportant);
        priorityJson.put("urgent", isUrgent);

        return priorityJson;
    }

    // EFFECTS: returns JSON representation of dueDate
    public static JSONObject dueDateToJson(DueDate dueDate) {
        if (dueDate == null) {
            JSONObject dateJson = new JSONObject();
            dateJson.put("year", JSONObject.NULL);
            dateJson.put("month", JSONObject.NULL);
            dateJson.put("day", JSONObject.NULL);
            dateJson.put("hour", JSONObject.NULL);
            dateJson.put("minute", JSONObject.NULL);
            return dateJson;
        }

        JSONObject dateJson = new JSONObject();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dueDate.getDate());

        dateJson.put("year", cal.get(Calendar.YEAR));
        dateJson.put("month", cal.get(Calendar.MONTH));
        dateJson.put("day", cal.get(Calendar.DAY_OF_MONTH));
        dateJson.put("hour", cal.get(Calendar.HOUR_OF_DAY));
        dateJson.put("minute", cal.get(Calendar.MINUTE));

        return dateJson;
    }

    // EFFECTS: returns JSON representation of task
    public static JSONObject taskToJson(Task task) {
        JSONObject taskJson = new JSONObject();

        taskJson.put("description", task.getDescription());

        if (task.getTags().size() == 0) {
            taskJson.put("tags", new JSONArray());
        } else {
            taskJson.put("tags", tagsToJsonArray(task.getTags()));
        }

        if (task.getDueDate() == null) {
            taskJson.put("due-date", JSONObject.NULL);
        } else {
            taskJson.put("due-date", dueDateToJson(task.getDueDate()));
        }

        taskJson.put("priority", priorityToJson(task.getPriority()));
        taskJson.put("status", task.getStatus().toString().replace(" ", "_"));

        return taskJson;
    }

    public static JSONArray tagsToJsonArray(Set<Tag> tags) {
        JSONArray tagsJsonArray = new JSONArray();
        for (Tag t : tags) {
            tagsJsonArray.put(tagToJson(t));
        }
        return tagsJsonArray;
    }

    // EFFECTS: returns JSON array representing list of tasks
    public static JSONArray taskListToJson(List<Task> tasks) {
        JSONArray tasksJsonArray = new JSONArray();

        if (tasks != null) {
            for (Task t : tasks) {
                tasksJsonArray.put(taskToJson(t));
            }
        }

        return tasksJsonArray;   // stub
    }
}
