package main

import (
	"fmt"
	"net/http"
	"strconv"
)

func addHandler(w http.ResponseWriter, r *http.Request) {

	a, erra := strconv.ParseFloat(r.URL.Query().Get("a"), 64)
	b, errb := strconv.ParseFloat(r.URL.Query().Get("b"), 64)

	if erra != nil {
		http.Error(w, "Invalid parameter 'a'", http.StatusBadRequest)
		return
	}
	
	if errb != nil {
		http.Error(w, "Invalid parameter 'b'", http.StatusBadRequest)
		return
	}

	result := a + b

	fmt.Fprintf(w, "Result: %.2f\n", result)

}

func divisionHandler(w http.ResponseWriter, r *http.Request) {

	a, erra := strconv.ParseFloat(r.URL.Query().Get("a"), 64)
	b, errb := strconv.ParseFloat(r.URL.Query().Get("b"), 64)

	if erra != nil {
		http.Error(w, "Invalid parameter 'a'", http.StatusBadRequest)
		return
	}
	
	if errb != nil {
		http.Error(w, "Invalid parameter 'b'", http.StatusBadRequest)
		return	
	}

	if b == 0 {
		http.Error(w, "Division by zero is not allowed", http.StatusBadRequest)
		return
	}

	result := a / b

	fmt.Fprintf(w, "Result: %.2f\n", result)

}

func productHandler(w http.ResponseWriter, r *http.Request) {

	a, erra := strconv.ParseFloat(r.URL.Query().Get("a"), 64)
	b, errb := strconv.ParseFloat(r.URL.Query().Get("b"), 64)

	if erra != nil {
		http.Error(w, "Invalid parameter 'a'", http.StatusBadRequest)
		return
	}
	
	if errb != nil {
		http.Error(w, "Invalid parameter 'b'", http.StatusBadRequest)
		return
	}

	result := a * b

	fmt.Fprintf(w, "Result: %.2f\n", result)
}

func subtractHandler(w http.ResponseWriter, r *http.Request) {

	a, erra := strconv.ParseFloat(r.URL.Query().Get("a"), 64)
	b, errb := strconv.ParseFloat(r.URL.Query().Get("b"), 64)

	if erra != nil {
		http.Error(w, "Invalid parameter 'a'", http.StatusBadRequest)
		return
	}
	
	if errb != nil {
		http.Error(w, "Invalid parameter 'b'", http.StatusBadRequest)
		return
	}

	result := a - b

	fmt.Fprintf(w, "Result: %.2f\n", result)
}

func main() {

	http.HandleFunc("/add", addHandler)
	http.HandleFunc("/divide", divisionHandler)
	http.HandleFunc("/multiply", productHandler)
	http.HandleFunc("/subtract", subtractHandler)


	fmt.Println("Server running on http://localhost:8081")
	err := http.ListenAndServe(":8081", nil)
	if err != nil {
		fmt.Println("Error starting server:", err)
	}
}
