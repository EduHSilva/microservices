package seeds

import (
	"bytes"
	"fmt"
	"health/schemas/diet"
	"log"
	"strconv"
	"strings"

	"github.com/xuri/excelize/v2"
	"gorm.io/gorm"
)

func fixHeader(s string) string {
	replacer := strings.NewReplacer(
		"Ã¡", "á",
		"Ã ", "à",
		"Ã¢", "â",
		"Ã£", "ã",
		"Ã¤", "ä",
		"Ã©", "é",
		"Ãª", "ê",
		"Ã­", "í",
		"Ã³", "ó",
		"Ã´", "ô",
		"Ãµ", "õ",
		"Ãº", "ú",
		"Ã§", "ç",
		"Ã", "Á",
		"Ã‰", "É",
		"Ã", "Í",
		"Ã“", "Ó",
		"Ãš", "Ú",
		"Ã‡", "Ç",
		"Â°", "°",
		"Âº", "º",
		"Âª", "ª",
		"Â", "",
		"ProteÃ­na", "Proteína",
		"LipÃ­dios", "Lipídios",
		"Carboidrato disponÃ­vel", "Carboidrato disponível",
		"Ãlcool", "Álcool",
		"Ãcidos graxos saturados", "Ácidos graxos saturados",
		"Ãcidos graxos monoinsaturados", "Ácidos graxos monoinsaturados",
		"Ãcidos graxos polinsaturados", "Ácidos graxos polinsaturados",
		"CÃ¡lcio", "Cálcio",
		"SÃ³dio", "Sódio",
		"MagnÃ©sio", "Magnésio",
		"FÃ³sforo", "Fósforo",
		"PotÃ¡ssio", "Potássio",
		"SelÃªnio", "Selênio",
		"AdiÃ§Ã£o", "Adição",
		"AÃ§Ãºcar", "Açúcar",
	)
	return replacer.Replace(strings.TrimSpace(s))
}

func loadFoodFromExcel(db *gorm.DB) {
	_ = db // ainda não utilizado

	file := "data/foods.xlsx"

	contents, err := Files.ReadFile(file)
	if err != nil {
		log.Fatal(err)
	}

	f, err := excelize.OpenReader(bytes.NewReader(contents))
	if err != nil {
		log.Fatal(err)
	}
	defer func() {
		_ = f.Close()
	}()

	sheet := f.GetSheetName(0)

	rows, err := f.GetRows(sheet)
	if err != nil {
		log.Fatal(err)
	}

	if len(rows) == 0 {
		fmt.Println("Planilha vazia.")
		return
	}

	// Cabeçalhos
	headers := make([]string, len(rows[0]))
	for i, h := range rows[0] {
		headers[i] = fixMojibake(h)
	}

	fmt.Println("\n=== LINHAS ===")

	for _, row := range rows[1:] {
		for i := range row {
			row[i] = fixMojibake(row[i])
		}
		for _, row := range rows[1:] {

			for i := range row {
				row[i] = fixMojibake(row[i])
			}

			data := rowToMap(headers, row)

			food := diet.Food{
				Code: data["ID"],
				Name: data["ALIMENTO"],

				Calories: parseFloat(data["Energia Kcal"]),

				Protein: parseFloat(data["Proteína"]),
				Carbs:   parseFloat(data["Carboidrato total"]),
				Fat:     parseFloat(data["Lipídios"]),
				Fiber:   parseFloat(data["Fibra alimentar"]),

				Water:       parseFloat(data["Umidade g"]),
				Sodium:      parseFloat(data["Sódio"]),
				Cholesterol: parseFloat(data["Colesterol"]),

				SaturatedFat: parseFloat(data["Ácidos graxos saturados"]),
				MonounsatFat: parseFloat(data["Ácidos graxos monoinsaturados"]),
				PolyunsatFat: parseFloat(data["Ácidos graxos polinsaturados"]),
				TransFat:     parseFloat(data["Ácidos graxos trans"]),

				Calcium:   parseFloat(data["Cálcio"]),
				Iron:      parseFloat(data["Ferro"]),
				Magnesium: parseFloat(data["Magnésio"]),
				Phosphor:  parseFloat(data["Fósforo"]),
				Potassium: parseFloat(data["Potássio"]),
				Zinc:      parseFloat(data["Zinco"]),

				VitaminA: parseFloat(data["Vitamina A (RAE)"]),
				VitaminC: parseFloat(data["Vitamina C"]),
				VitaminD: parseFloat(data["Vitamina D"]),
				VitaminE: parseFloat(data["Alfa-tocoferol (Vitamina E)"]),

				B1:  parseFloat(data["Tiamina"]),
				B2:  parseFloat(data["Riboflavina"]),
				B6:  parseFloat(data["Vitamina B6"]),
				B12: parseFloat(data["Vitamina B12"]),
			}

			if err := db.Create(&food).Error; err != nil {
				log.Printf("Erro ao inserir %s: %v", food.Name, err)
			}
		}
	}
}

func fixMojibake(s string) string {
	if !strings.Contains(s, "Ã") && !strings.Contains(s, "Â") {
		return s
	}

	b := make([]byte, 0, len(s))
	for _, r := range s {
		if r > 255 {
			return s
		}
		b = append(b, byte(r))
	}

	return string(b)
}

func parseFloat(v string) float64 {
	v = strings.TrimSpace(v)
	v = strings.ReplaceAll(v, ",", ".")

	if v == "" {
		return 0
	}

	f, err := strconv.ParseFloat(v, 64)
	if err != nil {
		return 0
	}

	return f
}

func rowToMap(headers, row []string) map[string]string {
	m := make(map[string]string)

	for i, h := range headers {
		if i < len(row) {
			m[h] = row[i]
		}
	}

	return m
}
