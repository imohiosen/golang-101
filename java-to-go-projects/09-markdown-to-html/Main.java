package markdown;

import java.nio.file.*;

/**
 * CLI entry point for the Markdown-to-HTML converter.
 *
 * Usage: java Main input.md [output.html]
 *
 * Go structure:
 *   main.go               — CLI, file I/O
 *   markdown/token.go     — Token struct + Type constants
 *   markdown/lexer.go     — Tokenize(source) using regexp
 *   markdown/renderer.go  — Render(tokens) → HTML string
 *                            processInline for bold/italic/code/link
 *
 * Rust structure:
 *   main.rs               — CLI, file I/O
 *   token.rs              — Token enum with variants (Heading{level,content}, etc.)
 *   lexer.rs              — tokenize(source) using regex crate
 *   renderer.rs           — render(tokens) → String, process_inline()
 *
 * Key learning:
 *   - Go: regexp package, string manipulation, iota for constants
 *   - Rust: enum variants with data, regex captures, String ownership in nested transforms
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: md2html <input.md> [output.html]");
            System.exit(1);
        }

        String source = Files.readString(Path.of(args[0]));

        Lexer lexer = new Lexer();
        Renderer renderer = new Renderer();

        var tokens = lexer.tokenize(source);
        String html = renderer.render(tokens);

        if (args.length >= 2) {
            Files.writeString(Path.of(args[1]), html);
            System.out.println("Written to " + args[1]);
        } else {
            System.out.println(html);
        }
    }
}
