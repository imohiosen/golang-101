pub enum Token {
    Heading { level: u8, content: String },
    Paragraph { content: String },
    CodeBlock { content: String },
    CodeInline { content: String },
    Bold { content: String },
    Italic { content: String },
    Link { text: String, url: String },
    ListItem { content: String },
    OrderedItem { content: String },
    HorizontalRule,
    Blockquote { content: String },
    Newline,
}
