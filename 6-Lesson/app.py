import os
import csv
import requests
from flask import Flask, jsonify, request

app = Flask(__name__)


@app.get("/")
def index():
	return (
		"<html><head><title>City Weather</title></head>"
		"<body>"
		"<h1>City Weather</h1>"
		"<form action=\"/city-weather\" method=\"get\">"
		"<label>API Key: <input type=\"text\" name=\"api_key\" /></label>"
		"<button type=\"submit\">View</button>"
		"</form>"
		"<p>Or open <a href=\"/city-weather\">/city-weather</a> if you set an environment variable.</p>"
		"</body></html>"
	)


def read_cities(csv_path):
	cities = []
	with open(csv_path, newline="", encoding="utf-8") as f:
		reader = csv.DictReader(f)
		for row in reader:
			city = (row.get("city") or "").strip()
			population_str = (row.get("population") or "0").replace(",", "").strip()
			try:
				population = int(population_str)
			except ValueError:
				population = None
			if city:
				cities.append({"city": city, "population": population})
	return cities


def fetch_weather_for_city(city, api_key):
	base_url = "https://api.openweathermap.org/data/2.5/weather"
	params = {"q": city, "appid": api_key, "units": "metric"}
	resp = requests.get(base_url, params=params, timeout=10)
	if resp.status_code != 200:
		return {"error": True, "status": resp.status_code, "message": resp.text}
	data = resp.json()
	main = data.get("main") or {}
	weather_list = data.get("weather") or []
	weather_main = weather_list[0].get("main") if weather_list else None
	wind = data.get("wind") or {}
	return {
		"temp_c": main.get("temp"),
		"humidity": main.get("humidity"),
		"weather": weather_main,
		"wind_speed": wind.get("speed"),
		"country": ((data.get("sys") or {}).get("country")),
	}


@app.get("/city-weather")
def city_weather():
	api_key = (request.args.get("api_key") or os.environ.get("OPENWEATHER_API_KEY"))
	if not api_key:
		return jsonify({"error": "OPENWEATHER_API_KEY is not set"}), 500
	csv_path = os.path.join(os.path.dirname(__file__), "cities.csv")
	cities = read_cities(csv_path)
	results = []
	for entry in cities:
		city = entry["city"]
		population = entry["population"]
		try:
			weather = fetch_weather_for_city(city, api_key)
		except requests.RequestException as e:
			weather = {"error": True, "message": str(e)}
		results.append({
			"city": city,
			"population": population,
			"weather": weather
		})
	return jsonify({"results": results})


if __name__ == "__main__":
	app.run(host="0.0.0.0", port=int(os.environ.get("PORT", 5000)), debug=True)
