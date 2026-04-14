package merger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;

/**
 * Deep-merges two JSON trees.
 *
 * Rules:
 *   - Objects: merge recursively (override keys win)
 *   - Arrays:  replaced entirely (not concatenated)
 *   - Scalars: override replaces base
 *   - Null in override removes the key
 */
public class Merger {

    /**
     * Recursively merge override into base.
     * Returns a new tree — does not mutate inputs.
     */
    public static JsonNode merge(JsonNode base, JsonNode override) {
        if (!base.isObject() || !override.isObject()) {
            return override.deepCopy();
        }

        ObjectNode result = base.deepCopy();
        Iterator<Map.Entry<String, JsonNode>> fields = override.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode overrideValue = field.getValue();

            if (overrideValue.isNull()) {
                result.remove(key);
            } else if (result.has(key) && result.get(key).isObject() && overrideValue.isObject()) {
                result.set(key, merge(result.get(key), overrideValue));
            } else {
                result.set(key, overrideValue.deepCopy());
            }
        }

        return result;
    }
}
