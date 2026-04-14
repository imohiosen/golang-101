package markdown;

import java.util.*;
import java.util.regex.*;

/**
 * Renders a list of Tokens into an HTML string.
 * Handles nested inline elements within block-level tokens.
 */
public class Renderer {

    private static final Pattern BOLD = Pattern.compile("\\*\\*(.+?)\\*\\*");
    private static final Pattern ITALIC = Pattern.compile("\\*(.+?)\\*");
    private static final Pattern CODE_INLINE = Pattern.compile("`(.+?)`");
    private static final Pattern LINK = Pattern.compile("\\[(.+?)\\]\\((.+?)\\)");

    /**
     * Convert tokens to a full HTML document.
     */
    public String render(List<Token> tokens) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head><meta charset=\"UTF-8\"></head>\n<body>\n");

        boolean inList = false;
        boolean inOrderedList = false;

        for (Token token : tokens) {
            // Close open lists if this token isn't a list item
            if (token.getType() != Token.Type.LIST_ITEM && inList) {
                html.append("</ul>\n");
                inList = false;
            }
            if (token.getType() != Token.Type.ORDERED_ITEM && inOrderedList) {
                html.append("</ol>\n");
                inOrderedList = false;
            }

            switch (token.getType()) {
                case HEADING -> html.append(String.format("<h%d>%s</h%d>\n",
                        token.getLevel(), processInline(token.getContent()), token.getLevel()));

                case PARAGRAPH -> html.append(String.format("<p>%s</p>\n",
                        processInline(token.getContent())));

                case CODE_BLOCK -> html.append(String.format("<pre><code>%s</code></pre>\n",
                        escapeHtml(token.getContent())));

                case HORIZONTAL_RULE -> html.append("<hr>\n");

                case BLOCKQUOTE -> html.append(String.format("<blockquote>%s</blockquote>\n",
                        processInline(token.getContent())));

                case LIST_ITEM -> {
                    if (!inList) {
                        html.append("<ul>\n");
                        inList = true;
                    }
                    html.append(String.format("  <li>%s</li>\n",
                            processInline(token.getContent())));
                }

                case ORDERED_ITEM -> {
                    if (!inOrderedList) {
                        html.append("<ol>\n");
                        inOrderedList = true;
                    }
                    html.append(String.format("  <li>%s</li>\n",
                            processInline(token.getContent())));
                }

                case NEWLINE -> {} // ignored between blocks
            }
        }

        if (inList) html.append("</ul>\n");
        if (inOrderedList) html.append("</ol>\n");

        html.append("</body>\n</html>");
        return html.toString();
    }

    /**
     * Process inline elements: bold, italic, code, links.
     */
    private String processInline(String text) {
        text = escapeHtml(text);
        text = BOLD.matcher(text).replaceAll("<strong>$1</strong>");
        text = ITALIC.matcher(text).replaceAll("<em>$1</em>");
        text = CODE_INLINE.matcher(text).replaceAll("<code>$1</code>");
        text = LINK.matcher(text).replaceAll("<a href=\"$2\">$1</a>");
        return text;
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }
}
