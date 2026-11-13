package io.github.project.openubl.xsender.sunat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Just enough JSON for SUNAT's REST API. Objects become {@code Map<String, Object>},
 * arrays {@code List<Object>}, numbers and strings {@code String}, booleans
 * {@code Boolean} and null {@code null}.
 * <p>
 * Lenient by design (e.g. it doesn't validate number formats): it only reads
 * responses from SUNAT, not arbitrary input.
 */
final class Json {

    private final String text;
    private int position;

    private Json(String text) {
        this.text = text;
    }

    /**
     * @return empty if the text is not a JSON object
     */
    static Optional<Map<String, Object>> parseObject(String text) {
        try {
            Json json = new Json(text);
            json.skipWhitespace();
            if (json.peek() != '{') {
                return Optional.empty();
            }
            Map<String, Object> object = json.readObject();
            json.skipWhitespace();
            return json.position == text.length() ? Optional.of(object) : Optional.empty();
        } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    static String quote(String value) {
        StringBuilder result = new StringBuilder("\"");
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"' -> result.append("\\\"");
                case '\\' -> result.append("\\\\");
                case '\n' -> result.append("\\n");
                case '\r' -> result.append("\\r");
                case '\t' -> result.append("\\t");
                default -> {
                    if (c < 0x20) {
                        result.append("\\u%04x".formatted((int) c));
                    } else {
                        result.append(c);
                    }
                }
            }
        }
        return result.append('"').toString();
    }

    private Object readValue() {
        skipWhitespace();
        char c = peek();
        return switch (c) {
            case '{' -> readObject();
            case '[' -> readArray();
            case '"' -> readString();
            case 't' -> readLiteral("true", Boolean.TRUE);
            case 'f' -> readLiteral("false", Boolean.FALSE);
            case 'n' -> readLiteral("null", null);
            default -> readNumber();
        };
    }

    private Map<String, Object> readObject() {
        Map<String, Object> object = new LinkedHashMap<>();
        expect('{');
        skipWhitespace();
        if (peek() == '}') {
            position++;
            return object;
        }
        do {
            skipWhitespace();
            String key = readString();
            skipWhitespace();
            expect(':');
            object.put(key, readValue());
            skipWhitespace();
        } while (tryConsume(','));
        expect('}');
        return object;
    }

    private List<Object> readArray() {
        List<Object> array = new ArrayList<>();
        expect('[');
        skipWhitespace();
        if (peek() == ']') {
            position++;
            return array;
        }
        do {
            array.add(readValue());
            skipWhitespace();
        } while (tryConsume(','));
        expect(']');
        return array;
    }

    private String readString() {
        expect('"');
        StringBuilder result = new StringBuilder();
        char c;
        while ((c = text.charAt(position++)) != '"') {
            if (c != '\\') {
                result.append(c);
                continue;
            }
            char escaped = text.charAt(position++);
            switch (escaped) {
                case 'b' -> result.append('\b');
                case 'f' -> result.append('\f');
                case 'n' -> result.append('\n');
                case 'r' -> result.append('\r');
                case 't' -> result.append('\t');
                case 'u' -> {
                    result.append((char) Integer.parseInt(text.substring(position, position + 4), 16));
                    position += 4;
                }
                default -> result.append(escaped);
            }
        }
        return result.toString();
    }

    private String readNumber() {
        int start = position;
        while (position < text.length() && "+-0123456789.eE".indexOf(text.charAt(position)) >= 0) {
            position++;
        }
        if (start == position) {
            throw new IllegalArgumentException("Unexpected character at " + position);
        }
        return text.substring(start, position);
    }

    private Object readLiteral(String literal, Object value) {
        if (!text.startsWith(literal, position)) {
            throw new IllegalArgumentException("Unexpected character at " + position);
        }
        position += literal.length();
        return value;
    }

    private void skipWhitespace() {
        while (position < text.length() && Character.isWhitespace(text.charAt(position))) {
            position++;
        }
    }

    private char peek() {
        return text.charAt(position);
    }

    private boolean tryConsume(char c) {
        if (position < text.length() && text.charAt(position) == c) {
            position++;
            return true;
        }
        return false;
    }

    private void expect(char c) {
        if (!tryConsume(c)) {
            throw new IllegalArgumentException("Expected '" + c + "' at " + position);
        }
    }
}
