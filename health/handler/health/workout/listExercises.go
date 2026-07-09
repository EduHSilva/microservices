package workout

import (
	"net/http"

	"health/helper"
	"health/schemas/workout"

	"github.com/gin-gonic/gin"
	"github.com/nicksnyder/go-i18n/v2/i18n"
)

func GetExercisesHandler(ctx *gin.Context) {
	var exercises []workout.Exercise
	var exercisesResponses []ResponseDataExercise

	getI18n, _ := ctx.Get("i18n")
	locale, _ := ctx.Get("locale")

	if err := db.Order("name").Find(&exercises).Error; err != nil {
		helper.SendErrorDefault(ctx, http.StatusInternalServerError, getI18n.(*i18n.Localizer))
		return
	}

	for _, exercise := range exercises {
		translatedName := exercise.Name
		translatedInstructions := exercise.Instructions

		if locale == "pt_BR" {
			translatedName = exercise.NamePt
			translatedInstructions = exercise.InstructionsPt
		}

		exercisesResponses = append(exercisesResponses, ResponseDataExercise{
			ID:           exercise.ID,
			Name:         translatedName,
			BodyPart:     exercise.BodyPart,
			Target:       exercise.Target,
			Instructions: translatedInstructions,
		})
	}

	helper.SendSuccess(ctx, exercisesResponses)
}
