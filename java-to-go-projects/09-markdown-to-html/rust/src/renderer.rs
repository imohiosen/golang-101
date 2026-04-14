use crate::token::Token;

/// Render a list of Tokens into a full HTML document string.
pub fn render(tokens: &[Token]) -> String {
    todo!()
}

/// Process inline elements (bold, italic, code, links) within text.
fn process_inline(text: &str) -> String {
    todo!()
}

/// Escape HTML special characters.
fn escape_html(text: &str) -> String {
    text.replace('&', "&amp;")
        .replace('<', "&lt;")
        .replace('>', "&gt;")
}
