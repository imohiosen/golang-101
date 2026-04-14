package markdown;

/**
 * Represents a parsed token from the Markdown source.
 * Each token has a type and associated content/metadata.
 */
public class Token {

    public enum Type {
        HEADING,        // # through ######
        PARAGRAPH,      // Plain text block
        BOLD,           // **text**
        ITALIC,         // *text*
        CODE_INLINE,    // `code`
        CODE_BLOCK,     // ```code block```
        LINK,           // [text](url)
        LIST_ITEM,      // - item  or  * item
        ORDERED_ITEM,   // 1. item
        HORIZONTAL_RULE,// ---  or  ***
        BLOCKQUOTE,     // > text
        NEWLINE         // Empty line (paragraph separator)
    }

    private final Type type;
    private final String content;
    private final int level;      // heading level (1-6) or list depth
    private final String url;     // for LINK tokens

    public Token(Type type, String content) {
        this(type, content, 0, null);
    }

    public Token(Type type, String content, int level) {
        this(type, content, level, null);
    }

    public Token(Type type, String content, int level, String url) {
        this.type = type;
        this.content = content;
        this.level = level;
        this.url = url;
    }

    public Type getType() { return type; }
    public String getContent() { return content; }
    public int getLevel() { return level; }
    public String getUrl() { return url; }

    @Override
    public String toString() {
        return String.format("Token{%s, content='%s', level=%d}", type, content, level);
    }
}
