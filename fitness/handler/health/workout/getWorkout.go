package workout

import (
	"net/http"

	"health/helper"
	"health/schemas/workout"

	"github.com/gin-gonic/gin"
	"github.com/nicksnyder/go-i18n/v2/i18n"
)

func GetWorkoutHandler(ctx *gin.Context) {
	id := ctx.Query("id")
	getI18n, _ := ctx.Get("i18n")
	locale, _ := ctx.Get("locale")

	if id == "" {
		helper.SendError(ctx, http.StatusBadRequest,
			helper.ErrParamIsRequired("id", "query param").Error())
		return
	}

	work := workout.Workout{}
	if err := db.Where("id = ?", id).Preload("Exercises").Preload("Exercises.Exercise").Find(&work).Error; err != nil {
		helper.SendErrorDefault(ctx, http.StatusInternalServerError, getI18n.(*i18n.Localizer))
		return
	}

	helper.SendSuccess(ctx, ConvertWorkoutToWorkoutResponse(&work, locale))
}
