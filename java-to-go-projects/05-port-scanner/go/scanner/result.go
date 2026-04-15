package scanner

import "fmt"

type ScanResult struct {
	Host      string
	Port      int
	Open      bool
	LatencyMs int64
}

func (r ScanResult) String() string {
	if r.Open {
		return fmt.Sprintf("%-5d OPEN  (%dms)", r.Port, r.LatencyMs)
	}
	return fmt.Sprintf("%-5d closed", r.Port)
}
