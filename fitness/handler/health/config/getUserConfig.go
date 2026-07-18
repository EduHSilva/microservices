package config

import (
	"health/schemas/config"
	"net/http"

	"health/helper"

	"github.com/gin-gonic/gin"
	"github.com/nicksnyder/go-i18n/v2/i18n"
)

func GetUserConfigHandler(ctx *gin.Context) {
	getI18n, _ := ctx.Get("i18n")
	locale, _ := ctx.Get("locale")

	userID, exists := helper.GatewayUserID(ctx)
	if !exists {
		helper.SendErrorDefault(ctx, http.StatusUnauthorized, getI18n.(*i18n.Localizer))
		return
	}

	userConfig := config.UserConfig{}
	if err := db.Where("user_id = ?", userID).First(&userConfig).Error; err != nil {
		helper.SendErrorDefault(ctx, http.StatusInternalServerError, getI18n.(*i18n.Localizer))
		return
	}

	helper.SendSuccess(ctx, ConvertUserConfigToUserConfigResponse(&userConfig, locale))
}
