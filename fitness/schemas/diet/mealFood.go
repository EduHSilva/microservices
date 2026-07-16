package diet

import "gorm.io/gorm"

type MealFood struct {
	gorm.Model
	MealFood    uint   `json:"food"`
	Quantity    int    `json:"quantity"`
	Observation string `json:"observation"`
	MealID      uint   `json:"meal_id"`
}
