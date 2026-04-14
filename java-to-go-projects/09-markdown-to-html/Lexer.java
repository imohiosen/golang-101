package markdown;

import java.util.*;
import java.util.regex.*;

/**
 * Tokenizes raw Markdown text into a list of Tokens.
 * Handles block-level elements (headings, lists, code blocks, blockquotes)
 * and inline elements (bold, italic, inline code, links).
 */
public class Lexer {

    private static final Pattern HEADING = Pattern.compile("^(#{1,6})\\s+(.+)");
    private static final Pattern UNORDERED_LIST = Pattern.compile("^[\\-*]\\s+(.+)");
    private static final Pattern ORDERED_LIST = Pattern.compile("^\\d+\\.\\s+(.+)");
    private static final Pattern HORIZONTAL_RULE = Pattern.compile("^(---|\\*\\*\\*|___)\\s*$");
    private static final Pattern BLOCKQUOTE = Pattern.compile("^>\\s?(.*)");
    private static final Pattern CODE_FENCE = Pattern.compile("^```(.*)");

    /**
     * Tokenize the entire markdown source into block-level tokens.
     */
    public List<Token> tokenize(String source) {
        List<Token> tokens = new ArrayList<>();
        String[] lines = source.split("\n");
        int i = 0;

        while (i < lines.length) {
            String line = lines[i];

            // Empty line
            if (line.trim().isEmpty()) {
                tokens.add(new Token(Token.Type.NEWLINE, ""));
                i++;
                continue;
            }

            // Code fence
            Matcher m = CODE_FENCE.matcher(line);
            if (m.matches()) {
                StringBuilder code = new StringBuilder();
                i++;
                while (i < lines.length && !CODE_FENCE.matcher(lines[i]).matches()) {
                    code.append(lines[i]).append("\n");
                    i++;
                }
                i++; // skip closing fence
                tokens.add(new Token(Token.Type.CODE_BLOCK, code.toString()));
                continue;
            }

            // Horizontal rule
            if (HORIZONTAL_RULE.matcher(line).matches()) {
                tokens.add(new Token(Token.Type.HORIZONTAL_RULE, ""));
                i++;
                continue;
            }

            // Heading
            m = HEADING.matcher(line);
            if (m.matches()) {
                int level = m.group(1).length();
                tokens.add(new Token(Token.Type.HEADING, m.group(2), level));
                i++;
                continue;
            }

            // Blockquote
            m = BLOCKQUOTE.matcher(line);
            if (m.matches()) {
                tokens.add(new Token(Token.Type.BLOCKQUOTE, m.group(1)));
                i++;
                continue;
            }

            // Unordered list item
            m = UNORDERED_LIST.matcher(line);
            if (m.matches()) {
                tokens.add(new Token(Token.Type.LIST_ITEM, m.group(1)));
                i++;
                continue;
            }

            // Ordered list item
            m = ORDERED_LIST.matcher(line);
            if (m.matches()) {
                tokens.add(new Token(Token.Type.ORDERED_ITEM, m.group(1)));
                i++;
                continue;
            }

            // Paragraph (default)
            tokens.add(new Token(Token.Type.PARAGRAPH, line));
            i++;
        }

        return tokens;
    }
}
