package main

import (
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestDemoUsesActionPathsAndQueryTarget(t *testing.T) {
	const deviceID = "34020000002000000001"
	var calls []string
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		calls = append(calls, r.URL.Path)
		if r.Method != http.MethodGet || r.ContentLength > 0 {
			t.Errorf("demo must only read: %s %s length=%d", r.Method, r.URL, r.ContentLength)
		}
		w.Header().Set("Content-Type", "application/json")
		switch r.URL.Path {
		case "/openapi/v1/device/list":
			if r.URL.Query().Get("page") != "1" || r.URL.Query().Get("pageSize") != "20" {
				t.Errorf("missing pagination: %s", r.URL.RawQuery)
			}
			_, _ = w.Write([]byte(`{"code":"OK","data":{"items":[{"deviceId":"` + deviceID + `","name":"demo"}],"total":1}}`))
		case "/openapi/v1/channel/list":
			if r.URL.Query().Get("deviceId") != deviceID {
				t.Errorf("deviceId must be a query target: %s", r.URL.RawQuery)
			}
			_, _ = w.Write([]byte(`{"code":"OK","data":{"items":[],"total":0}}`))
		default:
			t.Errorf("obsolete demo path: %s", r.URL.Path)
			http.Error(w, `{"code":"NOT_FOUND"}`, http.StatusNotFound)
		}
	}))
	defer server.Close()
	t.Setenv("UVP_BASE_URL", server.URL)
	t.Setenv("UVP_ACCESS_KEY", "uvp_000102030405060708090a0b0c0d0e0f")
	t.Setenv("UVP_SECRET_KEY", "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8")
	t.Setenv("UVP_AUDIENCE", "uvp-openapi-test-audience-v1")
	if err := demo(); err != nil {
		t.Fatal(err)
	}
	if len(calls) != 2 {
		t.Fatalf("want device and channel reads, got %v", calls)
	}
}

func TestSelfTestReadsRepositoryFixture(t *testing.T) {
	if err := selfTest(); err != nil {
		t.Fatal(err)
	}
}
