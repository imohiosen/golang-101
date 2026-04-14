package main

import (
    "fmt"
    "math"
)

// Define an interface
type Shape interface {
    Area() float64
    Perimeter() float64
}

// Circle type
type Circle struct {
    Radius float64
}

func (c Circle) Area() float64 {
    return math.Pi * c.Radius * c.Radius
}

func (c Circle) Perimeter() float64 {
    return 2 * math.Pi * c.Radius
}

// Rectangle type
type Rectangle struct {
    Width, Height float64
}

func (r Rectangle) Area() float64 {
    return r.Width * r.Height
}

func (r Rectangle) Perimeter() float64 {
    return 2 * (r.Width + r.Height)
}

// A function that accepts any Shape
func PrintShapeInfo(s Shape) {
    fmt.Printf("Type: %T\n", s)
    fmt.Printf("  Area:      %.2f\n", s.Area())
    fmt.Printf("  Perimeter: %.2f\n", s.Perimeter())
}

func main() {
    c := Circle{Radius: 5}
    r := Rectangle{Width: 4, Height: 7}

    PrintShapeInfo(c)
    PrintShapeInfo(r)

    // A slice of shapes (mixed types!)
    shapes := []Shape{c, r, Circle{Radius: 1}}
    fmt.Println("\nAll areas:")
    for _, s := range shapes {
        fmt.Printf("  %.2f\n", s.Area())
    }
}