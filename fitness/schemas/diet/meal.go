package diet

import "gorm.io/gorm"

type Meal struct {
	gorm.Model
	Name   string     `json:"name"`
	Hour   string     `json:"hour"`
	Foods  []MealFood `json:"foods" gorm:"foreignKey:MealID"`
	UserID string     `json:"user_id" gorm:"type:uuid;not null"`
}
