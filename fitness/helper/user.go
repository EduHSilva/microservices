package helper

import (
	"strings"

	"github.com/gin-gonic/gin"
)

const UserIDHeader = "X-User-ID"

// GatewayUserID returns the authenticated user's ID forwarded by the gateway.
func GatewayUserID(ctx *gin.Context) (string, bool) {
	userID := strings.TrimSpace(ctx.GetHeader(UserIDHeader))
	return userID, userID != ""
}
