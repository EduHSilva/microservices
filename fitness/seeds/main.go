package seeds

import (
	"embed"
	"health/schemas/workout"

	"gorm.io/gorm"
)

//go:embed json/*.json
var Files embed.FS

func Load(db *gorm.DB) {
	var count int64

	db.Model(&workout.Exercise{}).Count(&count)
	if count == 0 {
		loadExerciseFromFile(db)
	}
}
