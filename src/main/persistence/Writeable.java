package persistence;

import org.json.JSONObject;

// Taken from JsonSerializationDemo at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writeable {
    // EFFECTS: Returns this as a JSONObject
    JSONObject toJson();
}
