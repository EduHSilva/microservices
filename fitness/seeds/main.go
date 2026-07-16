package seeds

import (
	"embed"
	"health/schemas/diet"
	"health/schemas/workout"

	"gorm.io/gorm"
)

//go:embed data/*.json data/*.xlsx
var Files embed.FS

func Load(db *gorm.DB) {
	var count int64

	db.Model(&workout.Exercise{}).Count(&count)
	if count == 0 {
		loadExerciseFromFile(db)
	}

	db.Model(&diet.Food{}).Count(&count)
	if count == 0 {
		loadFoodFromExcel(db)
	}
}
