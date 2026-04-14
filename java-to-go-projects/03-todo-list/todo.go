package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"sync"
)

type Todo struct {
	ID        int64    `json:"id"`
	Title     string `json:"title"`
	Completed bool   `json:"completed"`
}

var (
	todos        = map[int64]Todo{}
	nextID int64 = 1
	mu     sync.Mutex
)

func todoMethodRouter(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodGet:
		todoRouterGet(w, r)
	case http.MethodPost:
		todoRouterPost(w, r)
	case http.MethodPut:
		todoRouterPut(w, r)
	case http.MethodDelete:
		todoRouterDelete(w, r)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

func todoRouterGet(w http.ResponseWriter, r *http.Request) {
	switch r.URL.Path {
		case "/todos/get-all":
			getAllTodosHandler(w, r)
		case "/todos/get-one":
			getOneTodoHandler(w, r)
		default:
			http.Error(w, "Not found", http.StatusNotFound)
	}

}


func todoRouterPost(w http.ResponseWriter, r *http.Request) {
	switch r.URL.Path {
		case "/todos/create":
			createTodoHandler(w, r)
		default:
			http.Error(w, "Not found", http.StatusNotFound)
	}
}

func createTodoHandler(w http.ResponseWriter, r *http.Request) {
	var todo Todo
	json.NewDecoder(r.Body).Decode(&todo)
	mu.Lock()
	todo.ID = nextID
	nextID++
	todos[todo.ID] = todo
	mu.Unlock()
	json.NewEncoder(w).Encode(todo)
}

func getOneTodoHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	mu.Lock()
	defer mu.Unlock()
	id, err := strconv.ParseInt(r.URL.Query().Get("id"), 10, 64)
	if err != nil {
		http.Error(w, "Invalid or missing 'id'", http.StatusBadRequest)
		return
	}

	todo, exists := todos[id]
	if !exists {
		http.Error(w, "Todo not found", http.StatusNotFound)
		return
	}
	
	json.NewEncoder(w).Encode(todo)

}


func getAllTodosHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	mu.Lock()
	// todo to slice 
	todoSlice := make([]Todo, 0, len(todos))
	for _, todo := range todos {
		todoSlice = append(todoSlice, todo)
	}
	json.NewEncoder(w).Encode(todoSlice)
	defer mu.Unlock()
}

func todoRouterPut(w http.ResponseWriter, r *http.Request) {
	
	// update update existing ... throws error if id is missing or invalid
	if r.URL.Path == "/todos/update" {
		updateTodoHandler(w, r)
	} else {
		http.Error(w, "Not found", http.StatusNotFound)
	}
}

func updateTodoHandler(w http.ResponseWriter, r *http.Request) {
	var todo Todo
	json.NewDecoder(r.Body).Decode(&todo)

	if todo.ID == 0 {
		http.Error(w, "Missing 'id'", http.StatusBadRequest)
		return
	}

	mu.Lock()
	defer mu.Unlock()

	if _, exists := todos[todo.ID]; !exists {
		http.Error(w, "Todo not found", http.StatusNotFound)
		return
	}

	todos[todo.ID] = todo
	json.NewEncoder(w).Encode(todo)
}

func todoRouterDelete(w http.ResponseWriter, r *http.Request) {
	if r.URL.Path == "/todos/delete" {
		deleteTodoHandler(w, r)
	} else {
		http.Error(w, "Not found", http.StatusNotFound)
	}
}

func deleteTodoHandler(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.ParseInt(r.URL.Query().Get("id"), 10, 64)

	if err != nil {
		http.Error(w, "Invalid or missing 'id'", http.StatusBadRequest)
		return
	}

	mu.Lock()
	defer mu.Unlock()

	if _, exists := todos[id]; !exists {
		http.Error(w, "Todo not found", http.StatusNotFound)
		return
	}

	delete(todos, id)
	w.WriteHeader(http.StatusNoContent) // 204 No Content
}



func main() {
	// This is just a placeholder to make the package "main" complete.

	todosRouter := todoMethodRouter
	http.HandleFunc("/todos", todosRouter)
	http.HandleFunc("/todos/", todosRouter)  // note trailing slash — catches /todos/1, /todos/2 etc
	fmt.Println("Running server on http://localhost:8888 ...")
	err := http.ListenAndServe(":8889", nil)
	if err != nil {
		fmt.Println("Error starting server:", err)
		panic(err)
	}
}
