package workout

import (
	"net/http"

	"health/helper"
	"health/schemas/workout"

	"github.com/gin-gonic/gin"
	"github.com/nicksnyder/go-i18n/v2/i18n"
)

func UpdateWorkoutHandler(ctx *gin.Context) {
	request := UpdateWorkoutRequest{}

	getI18n, _ := ctx.Get("i18n")
	locale, _ := ctx.Get("locale")

	if err := ctx.BindJSON(&request); err != nil {
		logger.ErrF("validation error: %v", err.Error())
		helper.SendError(ctx, http.StatusBadRequest, err.Error())
		return
	}

	if err := request.Validate(); err != nil {
		logger.ErrF("validation error: %v", err.Error())
		helper.SendError(ctx, http.StatusBadRequest, err.Error())
		return
	}

	id := ctx.Query("id")
	if id == "" {
		helper.SendError(ctx, http.StatusBadRequest,
			helper.ErrParamIsRequired("id", "query param").Error())
		return
	}
	userID, exists := helper.GatewayUserID(ctx)
	if !exists {
		helper.SendErrorDefault(ctx, http.StatusUnauthorized, getI18n.(*i18n.Localizer))
		return
	}

	work := &workout.Workout{}
	if err := db.Where("id = ? AND user_id = ?", id, userID).Preload("Exercises").First(work).Error; err != nil {
		helper.SendErrorDefault(ctx, http.StatusNotFound, getI18n.(*i18n.Localizer))
		return
	}

	if request.Name != "" {
		work.Name = request.Name
	}

	if len(request.Exercises) != 0 {
		if err := db.Where("workout_id = ?", work.ID).Delete(&workout.ExerciseWorkout{}).Error; err != nil {
			helper.SendError(ctx, http.StatusInternalServerError, "Failed to delete associated exercises")
			return
		}

		for _, ex := range request.Exercises {
			newExercise := workout.ExerciseWorkout{
				WorkoutID:   work.ID,
				ExerciseID:  ex.ExerciseID,
				Series:      ex.Series,
				Load:        ex.Load,
				RestSeconds: ex.RestSeconds,
				Repetitions: ex.Repetitions,
				Notes:       ex.Notes,
			}
			if err := db.Create(&newExercise).Error; err != nil {
				logger.ErrF("error updating exercises: %s", err.Error())
				helper.SendError(ctx, http.StatusInternalServerError, err.Error())
				return
			}
		}
	}

	if err := db.Save(&work).Error; err != nil {
		logger.ErrF("error updating: %s", err.Error())
		helper.SendError(ctx, http.StatusInternalServerError, err.Error())
		return
	}

	helper.SendSuccess(ctx, ConvertWorkoutToWorkoutResponse(work, locale))
}
