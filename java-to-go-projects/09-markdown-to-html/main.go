package main

import (
	"fmt"
	"os"
	"regexp"
	"strings"
)

const (
	TokenHeading = iota
	TokenParagraph
	TokenCodeBlock
	TokenListItem
	TokenOrderedItem
	TokenHorizontalRule
	TokenBlockquote
	TokenNewline
)

type Token struct {
	Type    int
	Content string
	Level   int    // for headings
	URL     string // for links
}

// tokenize converts raw Markdown source into a slice of block-level Tokens.
func tokenize(source string) []Token {
	_ = regexp.MustCompile
	_ = strings.Split(source, "\n")
	panic("todo")
}

// render converts a slice of Tokens into a complete HTML document string.
func render(tokens []Token) string {
	var sb strings.Builder
	_ = &sb
	panic("todo")
}

// processInline applies inline formatting (bold, italic, code, links) to text.
func processInline(text string) string {
	panic("todo")
}

// escapeHTML escapes <, >, &, " characters.
func escapeHTML(text string) string {
	panic("todo")
}

func main() {
	if len(os.Args) < 2 {
		fmt.Fprintf(os.Stderr, "Usage: %s <input.md> [output.html]\n", os.Args[0])
		os.Exit(1)
	}

	panic("todo: read input, tokenize, render, write output")
}
