package io.github.project.openubl.xsender.sunat;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonTest {

    @Test
    void parsesNestedValues() {
        Map<String, Object> json = Json.parseObject("""
                {
                  "cod": "422", "count": -1.5e2, "ok": true, "none": null,
                  "error": {"numError": "2335"},
                  "errors": [{"cod": 1033}, "x", false, []],
                  "empty": {}
                }""").orElseThrow();

        assertEquals("422", json.get("cod"));
        assertEquals("-1.5e2", json.get("count"));
        assertEquals(true, json.get("ok"));
        assertTrue(json.containsKey("none"));
        assertEquals(Map.of("numError", "2335"), json.get("error"));
        assertEquals(Arrays.asList(Map.of("cod", "1033"), "x", false, List.of()), json.get("errors"));
        assertEquals(Map.of(), json.get("empty"));
    }

    @Test
    void parsesEscapes() {
        Map<String, Object> json = Json.parseObject("{\"msg\": \"a\\\"b\\\\c\\n\\u00f1\"}").orElseThrow();

        assertEquals("a\"b\\c\nñ", json.get("msg"));
    }

    @Test
    void rejectsWhatIsNotAJsonObject() {
        assertEquals(Optional.empty(), Json.parseObject("<html>error</html>"));
        assertEquals(Optional.empty(), Json.parseObject("[1, 2]"));
        assertEquals(Optional.empty(), Json.parseObject("{\"a\": 1"));
        assertEquals(Optional.empty(), Json.parseObject("{\"a\": 1} trailing"));
        assertEquals(Optional.empty(), Json.parseObject(""));
    }

    @Test
    void quotesStrings() {
        assertEquals("\"a\\\"b\\\\c\\n\\u0001ñ\"", Json.quote("a\"b\\c\n\u0001ñ"));
    }
}
