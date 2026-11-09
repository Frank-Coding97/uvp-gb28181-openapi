package uvp

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"io"
	"net/http"
	"net/http/httptest"
	"net/url"
	"reflect"
	"strings"
	"testing"
)

const testAK = "uvp_000102030405060708090a0b0c0d0e0f"
const testSK = "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8"
const testAudience = "uvp-openapi-test-audience-v1"

func TestClientActionHTTPTransport(t *testing.T) {
	const deviceID = "34020000002000000001"
	const channelID = "34020000001320000001"
	// Whitespace and field order are intentional: the client must transmit exactly
	// these bytes, including false and zero, rather than re-encode the JSON.
	update := []byte("{\n  \"deviceId\":\"" + deviceID + "\", \"channelId\":\"" + channelID + "\", \"audioEnabled\":false, \"ptzType\":0, \"longitude\":0, \"latitude\":0\n}\n")
	search := []byte(`{"deviceId":"` + deviceID + `","channelId":"` + channelID + `","startTime":"2026-11-09T08:00:00+08:00","endTime":"2026-11-09T09:00:00+08:00","pageSize":20}`)
	cursor := []byte(`{"deviceId":"` + deviceID + `","channelId":"` + channelID + `","cursor":"opaque-next-page"}`)
	cases := []struct {
		name, method, path, canonicalQuery, idempotency string
		query                                           map[string]string
		body                                            []byte
	}{
		{
			name: "GET channel filters and query target", method: http.MethodGet,
			path:           "/openapi/v1/channel/list",
			canonicalQuery: "audioEnabled=false&deviceId=" + deviceID + "&keyword=%E6%91%84%E5%83%8F%E6%9C%BA%20A&page=1&pageSize=20&ptzType=0&status=online&streamTransport=TCP-PASSIVE",
			query: map[string]string{
				"deviceId": deviceID, "keyword": "摄像机 A", "page": "1", "pageSize": "20",
				"audioEnabled": "false", "ptzType": "0", "status": "online", "streamTransport": "TCP-PASSIVE",
			},
		},
		{
			name: "POST JSON target false zero and body HMAC", method: http.MethodPost,
			path: "/openapi/v1/channel/update", body: update, idempotency: "update-example-1",
		},
		{
			name: "POST record search initial target", method: http.MethodPost,
			path: "/openapi/v1/record/search", body: search,
		},
		{
			name: "POST record search targets and cursor without time filters", method: http.MethodPost,
			path: "/openapi/v1/record/search", body: cursor,
		},
	}
	for _, tc := range cases {
		t.Run(tc.name, func(t *testing.T) {
			calls := 0
			server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
				calls++
				body, err := io.ReadAll(r.Body)
				if err != nil {
					t.Error(err)
				}
				if r.Method != tc.method || r.URL.Path != tc.path {
					t.Errorf("wrong route: %s %s", r.Method, r.URL)
				}
				wantQuery := make(url.Values, len(tc.query))
				for key, value := range tc.query {
					wantQuery.Set(key, value)
				}
				if !reflect.DeepEqual(r.URL.Query(), wantQuery) || strings.Contains(r.URL.RawQuery, "+") {
					t.Errorf("query changed: %s", r.URL.RawQuery)
				}
				if string(body) != string(tc.body) {
					t.Errorf("body bytes changed: got %q, want %q", body, tc.body)
				}
				wantType := ""
				if tc.method == http.MethodPost {
					wantType = "application/json"
				}
				if r.Header.Get("Content-Type") != wantType {
					t.Errorf("wrong content type: %q", r.Header.Get("Content-Type"))
				}
				if len(r.TransferEncoding) != 0 || r.ContentLength != int64(len(tc.body)) {
					t.Errorf("body must have exact length, not chunked: length=%d encoding=%v", r.ContentLength, r.TransferEncoding)
				}
				if r.Header.Get(HeaderIdempotency) != tc.idempotency {
					t.Errorf("idempotency key changed")
				}
				verifyWireHMAC(t, r, tc.canonicalQuery, body)
				w.Header().Set("Content-Type", "application/json")
				_, _ = w.Write([]byte(`{"code":"OK","message":"ok","requestId":"local-http","data":{"accepted":true}}`))
			}))
			defer server.Close()
			client, err := NewClient(server.URL, testAK, testSK, testAudience)
			if err != nil {
				t.Fatal(err)
			}
			var data []byte
			if tc.method == http.MethodGet {
				data, err = client.Get(tc.path, tc.query)
			} else {
				data, err = client.Post(tc.path, tc.body, tc.idempotency)
			}
			if err != nil {
				t.Fatal(err)
			}
			if string(data) != `{"accepted":true}` || calls != 1 {
				t.Fatalf("want one successful request, got calls=%d data=%s", calls, data)
			}
		})
	}
}

// Independently reconstruct the server's canonical from the actual wire request;
// do not verify SignRequest by calling SignRequest again.
func verifyWireHMAC(t *testing.T, r *http.Request, canonicalQuery string, body []byte) {
	t.Helper()
	if r.Header.Get(HeaderSignVersion) != "1" || r.Header.Get(HeaderAccessKey) != testAK {
		t.Error("missing authentication headers")
	}
	if len(r.Header.Get(HeaderNonce)) != 32 || r.Header.Get(HeaderTimestamp) == "" {
		t.Error("missing nonce or timestamp")
	}
	digest := sha256.Sum256(body)
	canonical := strings.Join([]string{
		"UVP-HMAC-SHA256/1", testAK, r.Header.Get(HeaderTimestamp), r.Header.Get(HeaderNonce),
		r.Method, r.URL.Path, canonicalQuery, r.Header.Get("Content-Type"),
		hex.EncodeToString(digest[:]), r.Header.Get(HeaderIdempotency), testAudience,
	}, "\n")
	key := make([]byte, 32)
	for i := range key {
		key[i] = byte(i)
	}
	mac := hmac.New(sha256.New, key)
	_, _ = mac.Write([]byte(canonical))
	want := hex.EncodeToString(mac.Sum(nil))
	if !hmac.Equal([]byte(r.Header.Get(HeaderSignature)), []byte(want)) {
		t.Errorf("wire signature does not cover exact query/body: got %s, want %s", r.Header.Get(HeaderSignature), want)
	}
}
