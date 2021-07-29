package persistence;

import model.GameBoard;
import model.GamePiece;
import model.State;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads GameBoard from JSON data stored in file
// Largely taken from JsonSerializationDemo at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: Constructs a reader to process the source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: Reads a game board from file and returns it.
    //          Throws IOException if an error comes up while processing
    public GameBoard read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGameBoard(jsonObject);
    }

    // EFFECTS: Reads source file as a string and return sit
    private String readFile(String source) throws IOException {
        StringBuilder content = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> content.append(s));
        }

        return content.toString();
    }

    // EFFECTS: parses a game board from JSON object and returns it
    private GameBoard parseGameBoard(JSONObject jsonObject) {
        State turn = State.valueOf(jsonObject.getString("turn"));
        int clearPieceCount = jsonObject.getInt("clearPieceCount");
        int fillPieceCount = jsonObject.getInt("fillPieceCount");
        int gameOverCount = jsonObject.getInt("gameOverCount");
        GameBoard loadedBoard = new GameBoard(turn, clearPieceCount, fillPieceCount, gameOverCount);
        addGamePieces(loadedBoard, jsonObject);
        return loadedBoard;
    }

    // MODIFIES: gb
    // EFFECTS: parses game pieces from JSON object and adds them to the game board
    private void addGamePieces(GameBoard gb, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("pieces");
        for (Object json : jsonArray) {
            JSONObject nextPiece = (JSONObject) json;
            addGamePiece(gb, nextPiece);
        }
    }

    // MODIFIES: gb
    // EFFECTS: Parses a game piece from JSON object and adds it to the game board
    private void addGamePiece(GameBoard gb, JSONObject jsonObject) {
        int position = jsonObject.getInt("position");
        State state = State.valueOf(jsonObject.getString("state"));
        GamePiece piece = new GamePiece(position, state);
        gb.getBoard().add(piece);
    }
}
