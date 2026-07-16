package diet

import (
	"time"

	"health/schemas/diet"
)

type ResponseFood struct {
	Message string           `json:"message"`
	Data    ResponseDataFood `json:"data"`
}

type ResponseDataFood struct {
	ID          uint       `json:"tag_id"`
	Name        string     `json:"food_name"`
	Quantity    int        `json:"quantity"`
	Observation string     `json:"observation"`
	Photo       PhotoField `json:"photo"`
}

type PhotoField struct {
	Thumb string `json:"thumb"`
}

type ResponseData struct {
	ID        uint               `json:"id"`
	CreateAt  time.Time          `json:"createAt"`
	UpdateAt  time.Time          `json:"updateAt"`
	DeletedAt time.Time          `json:"deletedAt,omitempty"`
	Name      string             `json:"name"`
	Hour      string             `json:"hour"`
	Foods     []ResponseDataFood `json:"foods"`
}

type ResponseDiet struct {
	Message string       `json:"message"`
	Data    ResponseData `json:"data"`
}

func ConvertFoodToResponse(food *diet.MealFood) ResponseDataFood {
	return ResponseDataFood{
		ID: food.ID,
		//Name: food.MealFood,
		Quantity:    food.Quantity,
		Observation: food.Observation,
	}
}

type FoodResponse struct {
	ID       uint    `json:"id"`
	Name     string  `json:"name"`
	Calories float64 `json:"calories"`
	Protein  float64 `json:"protein"`
	Carbs    float64 `json:"carbs"`
	Fat      float64 `json:"fat"`
	Fiber    float64 `json:"fiber"`
}

func ConvertFoodsToResponse(food *diet.Food) FoodResponse {
	return FoodResponse{
		ID:   food.ID,
		Name: food.Name,
	}
}

func ConvertMealToResponse(meal *diet.Meal) ResponseData {
	foodResponse := make([]ResponseDataFood, len(meal.Foods))

	for i, food := range meal.Foods {
		foodResponse[i] = ConvertFoodToResponse(&food)
	}

	return ResponseData{
		ID:        meal.ID,
		CreateAt:  meal.CreatedAt,
		UpdateAt:  meal.UpdatedAt,
		DeletedAt: meal.DeletedAt.Time,
		Name:      meal.Name,
		Foods:     foodResponse,
		Hour:      meal.Hour,
	}
}
